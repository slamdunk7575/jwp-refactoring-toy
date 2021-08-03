package kitchenpos.order.application;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItem orderLineItem;

    private Order order;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem(1L, 1);
        order = new Order(1L, Arrays.asList(orderLineItem));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createOrder() {
        // given
        Long menuId = orderLineItem.getMenuId();
        given(menuDao.countByIdIn(Arrays.asList(menuId))).willReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        // when
        Order createdOrder = orderService.create(this.order);

        // then
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
        assertThat(createdOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(createdOrder.getOrderLineItems()).isEqualTo(order.getOrderLineItems());
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(createdOrder.getOrderedTime()).isNotNull();
    }

    @DisplayName("하나 이상의 주문 항목을 가져야 한다.")
    @Test
    void requireLeastOneOrderLineItem() {
        // given
        Order order = new Order(1L, null);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
           orderService.create(order);
        });
    }

    @DisplayName("주문 테이블 상태가 비어있음인 경우 생성할 수 없다.")
    @Test
    void notCreateStatusIsEmpty() {
        // given
        Long menuId = orderLineItem.getMenuId();
        given(menuDao.countByIdIn(Arrays.asList(menuId))).willReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        orderTable.setEmpty(true);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void findAllOrders() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(order));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
        assertThat(orders.get(0).getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(orders.get(0).getOrderLineItems()).isEqualTo(order.getOrderLineItems());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(orders.get(0).getOrderedTime()).isEqualTo(order.getOrderedTime());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        order.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        Order updatedOrder = orderService.changeOrderStatus(this.order.getId(), order);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 상태가 완료인 경우 변경할 수 없다.")
    @Test
    void notChangeStatusIsComplete() {
        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.changeOrderStatus(this.order.getId(), newOrder);
        });
    }
}
