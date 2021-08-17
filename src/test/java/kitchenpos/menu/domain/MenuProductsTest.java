package kitchenpos.menu.domain;

import kitchenpos.common.domain.price.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.TestFixture.메뉴상품_양념;
import static kitchenpos.util.TestFixture.메뉴상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    private List<MenuProduct> menuProducts;
    private BigDecimal priceSum;

    @BeforeEach
    void setUp() {
        menuProducts = Arrays.asList(메뉴상품_후라이드, 메뉴상품_양념);
        priceSum = 메뉴상품_후라이드.getPricePerQuantity().value()
                .add(메뉴상품_양념.getPricePerQuantity().value());
    }

    @DisplayName("메뉴 상품들의 가격 x 수량을 계산한다.")
    @Test
    void calculateMenuProductsPrice() {
        // given
        MenuProducts actual = new MenuProducts(menuProducts);

        // when
        Price sum = actual.getPriceSum();

        // then
        assertThat(sum.value()).isEqualByComparingTo(priceSum);
    }
}
