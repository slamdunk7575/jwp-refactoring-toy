package kitchenpos.ordertable.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OrderTableValidatorTest {

    private OrderTableValidator orderTableValidator = new OrderTableValidator();

    @DisplayName("주문 테이블의 상태 변경시 검증")
    @TestFactory
    List<DynamicTest> validateOrderTableStatus() {
        return Arrays.asList(
                DynamicTest.dynamicTest("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없다.", () -> {
                    // given
                    Order order = new Order.Builder()
                            .id(1L)
                            .orderTable(new OrderTable(1L, 0, false))
                            .orderLineItems(Arrays.asList(new OrderLineItem(1L, 1L, 1),
                                    new OrderLineItem(2L, 2L, 1)))
                            .orderedTime(LocalDateTime.of(2021, 9, 4, 10, 30))
                            .build();

                    order.updateOrderStatus(OrderStatus.MEAL.name());

                    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                        orderTableValidator.validateOrderStatusIsCookingOrMeal(Optional.of(order));
                    }).withMessageMatching("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
                })
        );
    }

}
