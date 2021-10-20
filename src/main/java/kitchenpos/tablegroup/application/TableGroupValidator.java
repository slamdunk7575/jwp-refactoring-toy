package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class TableGroupValidator {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    public void validateOrderTablesToCreateTableGroup(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 포함되어 있습니다.");
        }

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.");
        }

        validateOrderTableEmptyOrAssigned(orderTables);
    }

    public void validateOrderTableEmptyOrAssigned(List<OrderTable> orderTables) {
        orderTables.forEach(this::checkOrderTableEmptyOrAssigned);
    }

    private void checkOrderTableEmptyOrAssigned(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
        }
    }

    public void validateOrderStatusIsCookingOrMeal(List<Order> orders) {
        orders.stream()
                .filter(order -> OrderStatus.NOT_CHANGE_ORDER_STATUS.contains(order.getOrderStatus()))
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
                });
    }
}
