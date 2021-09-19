package kitchenpos.menu.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.common.domain.price.InvalidPriceException;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class MenuServiceTest extends BaseServiceTest {

    @Autowired
    private MenuService menuService;

    private MenuProductRequest menuProductRequest_후라이드;
    private MenuProductRequest menuProductRequest_양념;
    private List<MenuProductRequest> menuProductRequests;

    private String 새로운_메뉴_name;
    private BigDecimal 새로운_메뉴_price;
    private Long 새로운_메뉴_menu_group_id;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuProductRequest_후라이드 = new MenuProductRequest(1L, 1);
        menuProductRequest_양념 = new MenuProductRequest(2L, 1);
        menuProductRequests = Arrays.asList(menuProductRequest_후라이드, menuProductRequest_양념);
        새로운_메뉴_name = "후라이드+양념";
        새로운_메뉴_price = BigDecimal.valueOf(25000);
        새로운_메뉴_menu_group_id = 1L;
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenu() {
        // given
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_name, 새로운_메뉴_price, 새로운_메뉴_menu_group_id, menuProductRequests);

        // when
        MenuResponse result = menuService.create(menuRequest);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(새로운_메뉴_name);
        assertThat(result.getPrice()).isEqualByComparingTo(새로운_메뉴_price);
        assertThat(result.getMenuGroupId()).isEqualTo(1L);
        assertThat(result.getMenuProducts().size()).isEqualTo(2);
        assertThat(result.getMenuProducts().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getMenuProducts().get(1).getProductId()).isEqualTo(2L);
    }

    @DisplayName("메뉴 등록시 가격은 필수정보이다.")
    @Test
    void requireMenuPrice() {
        // given
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_name, null, 새로운_메뉴_menu_group_id, menuProductRequests);

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴 그룹이 등록되어 있어야 한다.")
    @Test
    void notExistMenuGroup() {
        // given
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_name, 새로운_메뉴_price, 0L, menuProductRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse menuResponse = menuService.create(menuRequest);
        }).withMessageMatching("등록되지 않은 메뉴 그룹 입니다.");
    }


    @DisplayName("상품이 등록되어 있어야 한다.")
    @Test
    void notExistProduct() {
        // given
        MenuProductRequest notExistProduct = new MenuProductRequest(0L, 1);
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_name, 새로운_메뉴_price, 새로운_메뉴_menu_group_id, Arrays.asList(notExistProduct));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse menuResponse = menuService.create(menuRequest);
        }).withMessageMatching("등록되지 않은 상품입니다.");
    }

    @DisplayName("메뉴 가격이 속한 상품들 가격의 합보다 크지 않아야 한다.")
    @Test
    void createMenuPriceGreaterThanSum() {
        // given
        BigDecimal wrongPrice = 새로운_메뉴_price.add(BigDecimal.valueOf(10000));
        MenuRequest menuRequest = new MenuRequest(새로운_메뉴_name, wrongPrice, 새로운_메뉴_menu_group_id, menuProductRequests);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuResponse menuResponse = menuService.create(menuRequest);
        }).withMessageMatching("메뉴 가격이 속한 상품들 가격 합보다 비쌉니다.");
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void findAllMenus() {
        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        assertThat(menuResponses).isNotEmpty();
        assertThat(menuResponses.stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toList()))
                .containsAll(Arrays.asList("후라이드치킨", "양념치킨", "반반치킨", "간장치킨", "순살치킨"));
    }
}
