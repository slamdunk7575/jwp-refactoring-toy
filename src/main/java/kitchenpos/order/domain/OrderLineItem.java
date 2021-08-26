package kitchenpos.order.domain;

import kitchenpos.common.domain.quantity.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Quantity quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(Long id, Menu menu, Quantity quantity) {
        this.id = id;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Long getOrder() {
        return order.getId();
    }

    public Long getMenu() {
        return menu.getId();
    }

    public Long getQuantity() {
        return quantity.value();
    }
}