package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.orderTable = validateOrderTable(builder.orderTable);
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        this.orderLineItems = validateOrderLineItems(builder.orderLineItems);
    }

    private OrderTable validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
        return orderTable;
    }

    private OrderLineItems validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문은 1개 이상의 메뉴가 포함되어 있어야 합니다.");
        }
        return new OrderLineItems(orderLineItems);
    }

    public boolean isNotComplete() {
        return OrderStatus.COMPLETION != orderStatus;
    }

    public void updateOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItems.updateOrder(this);
        this.orderLineItems = orderLineItems;
    }

    public void updateOrderStatus(String orderStatus) {
        checkOrderStatus();
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    private void checkOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("주문 완료 시 주문 상태를 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public static class Builder {
        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

}
