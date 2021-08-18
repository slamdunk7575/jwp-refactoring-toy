package kitchenpos.menu.application;

import kitchenpos.application.MenuService;
import kitchenpos.common.domain.quantity.Quantity;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Menu menu;

    private MenuGroup menuGroup;

    private MenuProduct menuProductFried;

    private MenuProduct menuProductSeasoned;

    @BeforeEach
    void setUp() {
        menuProductFried = new MenuProduct(new Product("후라이드", new BigDecimal(10000)), Quantity.of(1L));
        menuProductSeasoned = new MenuProduct(new Product("양념", new BigDecimal(10000)), Quantity.of(1L));
        menuGroup = new MenuGroup("추천메뉴");
        menu = new Menu.Builder()
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .menuGroup(menuGroup)
                .menuProducts(Arrays.asList(menuProductFried, menuProductSeasoned))
                .build();
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        // given
        Product product = new Product("후라이드", new BigDecimal(10000));

        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuProductRepository.save(menuProductFried)).thenReturn(menuProductFried);
        when(menuProductRepository.save(menuProductSeasoned)).thenReturn(menuProductSeasoned);

        // when
        Menu createMenu = menuService.create(this.menu);

        // then
        assertThat(createMenu.getId()).isEqualTo(menu.getId());
        assertThat(createMenu.getName()).isEqualTo(menu.getName());
        assertThat(createMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(createMenu.getMenuGroup()).isEqualTo(menu.getMenuGroup());
        assertThat(createMenu.getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

    @DisplayName("메뉴의 가격은 0원 이상이어야 한다.")
    @Test
    void createMenuPriceException() {
        // given
        Menu banBanMenu = new Menu.Builder()
                .name("후라이드+양념")
                .price(BigDecimal.valueOf(-10000))
                .menuGroup(menuGroup)
                .menuProducts(Arrays.asList(menuProductFried, menuProductSeasoned))
                .build();

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuService.create(banBanMenu);
        });
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품 가격의 합보다 크지 않아야 한다.")
    @Test
    void createMenuPriceOverException() {
        // given
        Product product = new Product("후라이드", new BigDecimal(5000));

        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuService.create(menu);
        });
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void findAllMenus() {
        // given
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(0).getId()).isEqualTo(menu.getId());
        assertThat(menus.get(0).getName()).isEqualTo(menu.getName());
        assertThat(menus.get(0).getPrice()).isEqualTo(menu.getPrice());
        assertThat(menus.get(0).getMenuGroup()).isEqualTo(menu.getMenuGroup());
        assertThat(menus.get(0).getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

}
