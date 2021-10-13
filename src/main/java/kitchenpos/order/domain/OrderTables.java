package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderTables {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTable(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTable(List<OrderTable> orderTables) {
        checkOrderTableSize(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.checkOrderTableEmptyOrAssigned();
        }
    }

    private void checkOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.");
        }
    }

    public void updateTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable ->  orderTable.updateTableGroup(tableGroup.getId()));
    }

    public void unGroup() {
        this.orderTables.forEach(OrderTable::unGroup);
    }

    public List<OrderTable> findAll() {
        return orderTables;
    }

    public boolean isSameSize(int otherSize) {
        return this.orderTables.size() == otherSize;
    }

    public void validateOrderTableStatus() {
        boolean hasNotCompleteOrder = orderTables.stream()
                .anyMatch(OrderTable::isNotComplete);

        if (hasNotCompleteOrder) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 그룹 지정은 해제할 수 없습니다.");
        }
    }
}
