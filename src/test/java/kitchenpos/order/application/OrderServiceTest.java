package kitchenpos.order.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.TestFixture.메뉴상품_양념;
import static kitchenpos.util.TestFixture.메뉴상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

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
                .menuGroup(메뉴그룹_한마리_메뉴)
                .menuProducts(Arrays.asList(메뉴상품_후라이드))
                .build();

        메뉴_양념 = new Menu.Builder()
                .id(2L)
                .name("양념")
                .price(BigDecimal.valueOf(16000))
                .menuGroup(메뉴그룹_한마리_메뉴)
                .menuProducts(Arrays.asList(메뉴상품_양념))
                .build();

        orderLineItemRequest1 = new OrderLineItemRequest(메뉴_후라이드.getId(), 1);
        orderLineItemRequest2 = new OrderLineItemRequest(메뉴_양념.getId(), 1);
        orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);

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


}
