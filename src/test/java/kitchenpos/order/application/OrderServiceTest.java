package kitchenpos.order.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.TestFixture.메뉴상품_양념;
import static kitchenpos.util.TestFixture.메뉴상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderServiceTest extends BaseServiceTest {

    @Autowired
    private OrderService orderService;

    private OrderLineItemRequest orderLineItemRequest1;
    private OrderLineItemRequest orderLineItemRequest2;

    private List<OrderLineItemRequest> orderLineItemRequests;

    private OrderTable 빈_주문_테이블;
    private OrderTable 비어있지_않은_주문_테이블;

    private Menu 메뉴_후라이드;
    private Menu 메뉴_양념;

    private Order 주문_테이블_조리중;
    private Order 주문_테이블_계산완료;

    private OrderRequest 주문_상태_변경_요청 = new OrderRequest(OrderStatus.MEAL.name());

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        빈_주문_테이블 = new OrderTable(1L, null, 0, true);
        비어있지_않은_주문_테이블 = new OrderTable(9L, null, 5, false);

        MenuGroup 메뉴그룹_한마리_메뉴 = new MenuGroup(2L, "한마리메뉴");

        메뉴_후라이드 = new Menu.Builder()
                .id(1L)
                .name("후라이드")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(메뉴그룹_한마리_메뉴.getId())
                .menuProducts(Arrays.asList(메뉴상품_후라이드))
                .build();

        메뉴_양념 = new Menu.Builder()
                .id(2L)
                .name("양념")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(메뉴그룹_한마리_메뉴.getId())
                .menuProducts(Arrays.asList(메뉴상품_양념))
                .build();

        orderLineItemRequest1 = new OrderLineItemRequest(메뉴_후라이드.getId(), 1);
        orderLineItemRequest2 = new OrderLineItemRequest(메뉴_양념.getId(), 1);
        orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);

        TableGroup 그룹_테이블 = new TableGroup(1L, LocalDateTime.of(2021, 9, 4, 10, 30));
        OrderTable 그룹_지정된_주문_테이블 = new OrderTable(10L, 그룹_테이블.getId(), 0, false);
        OrderTable 그룹_지정되지_않은_주문_테이블 = new OrderTable(11L, 5, false);

        OrderLineItem 주문_아이템_후라이드_1개 = new OrderLineItem(1L, 메뉴_후라이드.getId(), 1);
        OrderLineItem 주문_아이템_양념_1개 = new OrderLineItem(2L, 메뉴_양념.getId(), 1);

        주문_테이블_조리중 = new Order.Builder()
                .id(1L)
                .orderTable(그룹_지정된_주문_테이블)
                .orderLineItems(Arrays.asList(주문_아이템_후라이드_1개, 주문_아이템_양념_1개))
                .orderedTime(LocalDateTime.of(2021, 9, 4, 10, 30))
                .build();

        주문_테이블_계산완료 = new Order.Builder()
                .id(2L)
                .orderTable(그룹_지정되지_않은_주문_테이블)
                .orderLineItems(Arrays.asList(주문_아이템_후라이드_1개, 주문_아이템_양념_1개))
                .orderedTime(LocalDateTime.of(2021, 9, 11, 1, 30))
                .build();
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderRequest orderRequest = new OrderRequest(비어있지_않은_주문_테이블.getId(), orderLineItemRequests);

        // when
        OrderResponse result = orderService.create(orderRequest);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderTableId()).isNotNull();
        assertThat(result.getOrderLineItems().size()).isEqualTo(2);
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isEqualTo(result.getId());
        assertThat(result.getOrderLineItems().get(0).getMenuId()).isEqualTo(메뉴_후라이드.getId());
        assertThat(result.getOrderLineItems().get(1).getOrderId()).isEqualTo(result.getId());
        assertThat(result.getOrderLineItems().get(1).getMenuId()).isEqualTo(메뉴_양념.getId());
    }

    @DisplayName("하나 이상의 주문 항목을 가져야 한다.")
    @Test
    void requireOneMoreOrderLineItem() {
        // given
        OrderRequest emptyOrderLineItem = new OrderRequest(비어있지_않은_주문_테이블.getId(), new ArrayList<>());

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderResponse orderResponse = orderService.create(emptyOrderLineItem);
        }).withMessageMatching("주문은 1개 이상의 메뉴가 포함되어 있어야 합니다.");
    }

    @DisplayName("주문 테이블 상태가 비어있음인 경우 주문을 생성할 수 없다.")
    @Test
    void notCreateStatusIsEmpty() {
        // given
        OrderRequest emptyOrderTableStatus = new OrderRequest(빈_주문_테이블.getId(), orderLineItemRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderResponse orderResponse = orderService.create(emptyOrderTableStatus);
        });
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void findAllOrders() {
        // when
        List<OrderResponse> orderResponses = orderService.findAll();

        // then
        assertThat(orderResponses).isNotEmpty();
        assertThat(orderResponses.get(0).getOrderLineItems()).isNotEmpty();
        assertThat(orderResponses.get(0).getOrderLineItems().get(0).getId()).isNotNull();
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // when
        OrderResponse orderResponse = orderService.changeOrderStatus(주문_테이블_조리중.getId(), 주문_상태_변경_요청);

        // then
        assertThat(orderResponse.getId()).isEqualTo(주문_테이블_조리중.getId());
        assertThat(orderResponse.getOrderStatus()).isEqualTo(주문_상태_변경_요청.getOrderStatus());
    }

    @DisplayName("주문 상태가 완료인 경우 상태를 변경할 수 없다.")
    @Test
    void notChangeStatusIsComplete() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.changeOrderStatus(주문_테이블_계산완료.getId(), 주문_상태_변경_요청);
        }).withMessageMatching("주문 완료 시 주문 상태를 변경할 수 없습니다.");
    }
}
