package kitchenpos.menu.domain;

import kitchenpos.common.domain.price.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MenuTest {

    private MenuProduct menuProduct_후라이드;
    private MenuProduct menuProduct_양념;
    private List<MenuProduct> menuProducts;
    private BigDecimal totalProductPrice;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuProduct_후라이드 = new MenuProduct(상품_후라이드.getId(), 1);
        menuProduct_양념 = new MenuProduct(상품_양념.getId(), 1);
        menuProducts = Arrays.asList(menuProduct_후라이드, menuProduct_양념);
        totalProductPrice = 상품_후라이드.getPrice().multiply(BigDecimal.valueOf(메뉴상품_후라이드.getQuantity().value()))
                .add(상품_양념.getPrice().multiply(BigDecimal.valueOf(메뉴상품_양념.getQuantity().value())));
        menuGroup = new MenuGroup(5L, "추천메뉴");
    }

    @DisplayName("메뉴의 가격과 메뉴에 속한 상품들 가격을 비교할 수 있다.")
    @Test
    void isMenuPriceGreaterThan() {
        // given
        BigDecimal rightPrice = totalProductPrice.subtract(BigDecimal.valueOf(25000));

        Menu menu = new Menu.Builder()
                .name("추천메뉴")
                .price(rightPrice)
                .menuGroupId(menuGroup.getId())
                .menuProducts(menuProducts)
                .build();

        // when
        boolean menuPriceCompareResult = menu.isMenuPriceGreaterThan(new Price(totalProductPrice));

        // then
        assertThat(menuPriceCompareResult).isFalse();
    }
}
