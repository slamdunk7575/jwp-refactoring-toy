package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupValidatorTest {

    private TableGroupValidator tableGroupValidator = new TableGroupValidator();

    @DisplayName("그룹 지정 생성시 유효성 검사")
    @TestFactory
    List<DynamicTest> createTableGroup() {
        OrderTable 비어있는_주문_테이블 = new OrderTable(3L, 0,  true);
        OrderTable 비어있지_않은_주문_테이블 = new OrderTable(9L, 5,  false);
        OrderTable 등록되지_않은_주문_테이블 = new OrderTable(14L, 7,  false);

        return Arrays.asList(
                DynamicTest.dynamicTest("등록되지 않은 테이블이 포함되어 있는 경우 그룹 지정을 할 수 없다.", () -> {
                    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                        tableGroupValidator.validateOrderTablesToCreateTableGroup(Arrays.asList(비어있는_주문_테이블, 비어있지_않은_주문_테이블, 등록되지_않은_주문_테이블),
                                Arrays.asList(비어있는_주문_테이블.getId(), 비어있지_않은_주문_테이블.getId()));
                    }).withMessageMatching("등록되지 않은 테이블이 포함되어 있습니다.");
                }),

                DynamicTest.dynamicTest("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.", () -> {
                    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                        tableGroupValidator.validateOrderTablesToCreateTableGroup(Arrays.asList(비어있는_주문_테이블), Arrays.asList(비어있는_주문_테이블.getId()));
                    }).withMessageMatching("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.");
                })
        );
    }

    @DisplayName("비어있지 않거나 이미 그룹이 지정된 테이블 유효성 검증")
    @TestFactory
    List<DynamicTest> validateOrderTableEmptyOrAssigned() {
        OrderTable 비어있는_주문_테이블 = new OrderTable(3L, 0,  true);
        OrderTable 비어있지_않은_주문_테이블 = new OrderTable(9L, 5,  false);

        return Arrays.asList(
                DynamicTest.dynamicTest("비어있지 않은 테이블이 존재할 경우 그룹 지정을 할 수 없다.", () -> {
                  // when
                  비어있는_주문_테이블.unGroup();
                  비어있는_주문_테이블.updateEmpty(false);

                  // then
                  assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                      tableGroupValidator.validateOrderTableEmptyOrAssigned(Arrays.asList(비어있는_주문_테이블, 비어있지_않은_주문_테이블));
                  }).withMessageMatching("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
                }),
                DynamicTest.dynamicTest("이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없다", () -> {
                    // when
                    비어있는_주문_테이블.updateTableGroup(1L);

                    // then
                    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                        tableGroupValidator.validateOrderTableEmptyOrAssigned(Arrays.asList(비어있는_주문_테이블, 비어있지_않은_주문_테이블));
                    }).withMessageMatching("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
                })
        );
    }

    @DisplayName("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없다.")
    @Test
    void validateOrderStatusIsCookingOrMeal() {
        // given
        Order order = new Order.Builder()
                .id(1L)
                .orderTable(new OrderTable(1L, 0, false))
                .orderLineItems(Arrays.asList(new OrderLineItem(1L, 1L, 1),
                        new OrderLineItem(2L, 2L, 1)))
                .orderedTime(LocalDateTime.of(2021, 9, 4, 10, 30))
                .build();

        // when
        order.updateOrderStatus(OrderStatus.MEAL.name());

        // then
        assertThatThrownBy(() -> tableGroupValidator.validateOrderStatusIsCookingOrMeal(Arrays.asList(order)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
    }
}
