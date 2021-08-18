package kitchenpos.menu.domain;

import kitchenpos.common.domain.price.Price;
import kitchenpos.common.domain.quantity.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }

    public Price getPricePerQuantity() {
        return product.calculatePrice(quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public BigDecimal getProductPrice() {
        return this.product.getPrice();
    }
}
