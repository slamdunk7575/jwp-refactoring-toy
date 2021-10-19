package kitchenpos.menu.application;

import kitchenpos.common.domain.price.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.util.TestFixture.*;
import static kitchenpos.util.TestFixture.메뉴상품_양념;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuValidatorTest {

    private MenuProduct menuProduct_후라이드;
    private MenuProduct menuProduct_양념;
    private List<MenuProduct> menuProducts;
    private BigDecimal totalProductPrice;
    private MenuValidator menuValidator;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuProduct_후라이드 = new MenuProduct(상품_후라이드.getId(), 1);
        menuProduct_양념 = new MenuProduct(상품_양념.getId(), 1);
        menuProducts = Arrays.asList(menuProduct_후라이드, menuProduct_양념);
        totalProductPrice = 상품_후라이드.getPrice().multiply(BigDecimal.valueOf(메뉴상품_후라이드.getQuantity().value()))
                .add(상품_양념.getPrice().multiply(BigDecimal.valueOf(메뉴상품_양념.getQuantity().value())));
        menuValidator = new MenuValidator();
        menuGroup = new MenuGroup(5L, "추천메뉴");
    }

    @DisplayName("메뉴 가격은 메뉴에 속한 상품들 가격의 합보다 크지 않아야 한다.")
    @Test
    void createMenuPriceGreaterThanSum() {
        // given
        BigDecimal wrongPrice = totalProductPrice.add(BigDecimal.valueOf(10000));
        Menu menu = new Menu.Builder()
                .name("가격이 잘못된 메뉴")
                .price(wrongPrice)
                .menuGroupId(menuGroup.getId())
                .menuProducts(menuProducts)
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuValidator.validateMenuPrice(menu, new Price(totalProductPrice));
        }).withMessageMatching("메뉴 가격이 속한 상품들 가격 합보다 비쌉니다.");
    }

    @DisplayName("메뉴에 1개 이상의 상품이 포함되어야 한다.")
    @Test
    void requireOneOrMoreMenuProducts() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuValidator.validateMenuProducts(new ArrayList<>());
        }).withMessageMatching("메뉴에 1개 이상의 상품이 포함되어야 합니다.");
    }

    @DisplayName("메뉴 그룹이 등록되어 있지 않으면 메뉴를 생성할 수 없다.")
    @Test
    void notExistMenuGroup() {
        Optional<MenuGroup> emptyMenuGroup = Optional.empty();

        assertThatThrownBy(() -> {
            menuValidator.validateExistMenuGroup(emptyMenuGroup);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 메뉴 그룹 입니다.");
    }
}
