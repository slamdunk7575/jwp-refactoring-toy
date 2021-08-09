package kitchenpos.common.domain.price;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private static final int MIN_PRICE = 0;

    private BigDecimal price = BigDecimal.ZERO;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
            throw new InvalidPriceException();
        }
    }

    public static Price zero() {
        return new Price(BigDecimal.ZERO);
    }

    public void add(Price otherPrice) {
        this.price = this.price.add(otherPrice.value());
    }

    public Price multiply(BigDecimal quantity) {
        return new Price(price.multiply(quantity));
    }

    public boolean isExpensive(Price otherPrice) {
        return this.price.compareTo(otherPrice.value()) > MIN_PRICE;
    }

    public BigDecimal value() {
        return this.price;
    }
}
