package kitchenpos.menu.application;

import kitchenpos.common.domain.price.Price;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupMenuService;
import kitchenpos.product.application.ProductMenuService;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final ProductMenuService productMenuService;
    private final MenuGroupMenuService menuGroupMenuService;
    // 객체 참조(예:Menu - MenuGroup)를 끊어냈지만 도메인에 있던 책임들이 자연스럽게 서비스로 이동하게 되었다 (트레이드오프)

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuValidator menuValidator,
                       ProductMenuService productMenuService, MenuGroupMenuService menuGroupMenuService) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuValidator = menuValidator;
        this.productMenuService = productMenuService;
        this.menuGroupMenuService = menuGroupMenuService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu.Builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .menuGroupId(menuRequest.getMenuGroupId())
                .menuProducts(menuProducts)
                .build();

        menuValidator.validateMenuPrice(menu, getProductsPriceSum(menuRequest));
        menuValidator.validateMenuProducts(menuProducts);
        menuValidator.validateExistMenuGroup(menuGroupMenuService.findMenuGroupById(menu.getMenuGroupId()));

        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        List<Long> menuProductIds = menuProductRequests.stream()
                .map(menuProduct -> menuProduct.getProductId())
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(menuProductIds);

        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            menuProducts.add(products.stream()
                    .filter(menuProductRequest::isSameProductId)
                    .map(product -> new MenuProduct(product.getId(), menuProductRequest.getQuantity()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다.")));
        }

        return menuProducts;
    }

    public Price getProductsPriceSum(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(menuProductRequest -> productMenuService.calculatePrice(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .reduce(Price.zero(), Price::add);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
