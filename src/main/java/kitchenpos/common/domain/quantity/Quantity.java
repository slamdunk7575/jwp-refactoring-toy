package kitchenpos.common.domain.quantity;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Quantity {

    private static final int MIN_QUANTITY = 0;

    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantity(long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidQuantityException();
        }
    }

    public long value() {
        return quantity;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(this.quantity);
    }
}
