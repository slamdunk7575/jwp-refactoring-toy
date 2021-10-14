package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.ordertable.dao.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final OrderRepository orderRepository,
                        final MenuRepository menuRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        Order order = new Order.Builder()
                .orderTable(findOrderTable(orderRequest.getOrderTableId()))
                .orderLineItems(findOrderLineItems(orderRequest.getOrderLineItemRequests()))
                .build();
        return OrderResponse.of(orderRepository.save(order));
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블 입니다."));
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(item -> new OrderLineItem(findMenu(item), item.getQuantity()))
                .collect(Collectors.toList());
        return orderLineItems;
    }

    private Long findMenu(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 입니다."));
        return menu.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 입니다."));

        savedOrder.updateOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
