package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MenuTest {

    private MenuProduct menuProduct_후라이드;
    private MenuProduct menuProduct_양념;
    private List<MenuProduct> menuProducts;
    private BigDecimal totalProductPrice;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuProduct_후라이드 = new MenuProduct(상품_후라이드, 1);
        menuProduct_양념 = new MenuProduct(상품_양념, 1);
        menuProducts = Arrays.asList(menuProduct_후라이드, menuProduct_양념);
        totalProductPrice = 메뉴상품_후라이드.getProductPrice().multiply(BigDecimal.valueOf(메뉴상품_후라이드.getQuantity().value()))
                .add(메뉴상품_양념.getProductPrice().multiply(BigDecimal.valueOf(메뉴상품_양념.getQuantity().value())));
        menuGroup = new MenuGroup(5L, "추천메뉴");
    }

    /*@DisplayName("메뉴 가격은 메뉴에 속한 상품들 가격의 합보다 크지 않아야 한다.")
    @Test
    void createMenuPriceGreaterThanSum() {
        // given
        BigDecimal wrongPrice = totalProductPrice.add(BigDecimal.valueOf(10000));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            new Menu.Builder()
                    .name("가격이 잘못된 메뉴")
                    .price(wrongPrice)
                    .menuGroupId(menuGroup.getId())
                    .menuProducts(menuProducts)
                    .build();
        }).withMessageMatching("메뉴 가격이 속한 상품들 가격 합보다 비쌉니다.");
    }*/
}
