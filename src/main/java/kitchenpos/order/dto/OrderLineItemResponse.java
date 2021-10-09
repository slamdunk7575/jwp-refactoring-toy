package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long id;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem, Long orderId) {
        return new OrderLineItemResponse(orderLineItem.getId(),
                orderId,
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems, Long orderId) {
        return orderLineItems.stream()
                .map(orderLineItem -> of(orderLineItem, orderId))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
