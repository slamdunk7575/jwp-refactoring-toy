package kitchenpos.product.domain;

import kitchenpos.common.domain.name.Name;
import kitchenpos.common.domain.quantity.Quantity;
import kitchenpos.common.domain.price.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Price calculatePrice(Quantity quantity) {
        return this.price.multiply(quantity.toBigDecimal());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public BigDecimal getPrice() {
        return price.value();
    }
}
