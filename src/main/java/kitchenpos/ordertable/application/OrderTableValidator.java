package kitchenpos.ordertable.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderTableValidator {

    public void validateOrderStatusIsCookingOrMeal(Optional<Order> orderOptional) {
        orderOptional.ifPresent(this::validateOrderTableStatus);
    }

    private void validateOrderTableStatus(Order order) {
        if (OrderStatus.NOT_CHANGE_ORDER_STATUS.contains(order.getOrderStatus())) {
            throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
        }
    }
}
