package kitchenpos.menu.domain;

import kitchenpos.common.domain.price.Price;
import kitchenpos.common.domain.quantity.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.util.TestFixture.메뉴상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @DisplayName("메뉴 상품의 가격은 가격 x 수량으로 계산한다.")
    @Test
    void calculateMenuProductPrice() {
        // given
        BigDecimal price = 메뉴상품_후라이드.getProductPrice();
        Quantity quantity = 메뉴상품_후라이드.getQuantity();
        BigDecimal expectedPrice = price.multiply(BigDecimal.valueOf(quantity.value()));

        // when
        Price pricePerQuantity = 메뉴상품_후라이드.getPricePerQuantity();

        // then
        assertThat(pricePerQuantity.value()).isEqualByComparingTo(expectedPrice);
    }

}
