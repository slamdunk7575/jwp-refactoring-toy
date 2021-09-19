package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = new Menu.Builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .menuGroup(findMenuGroup(menuRequest.getMenuGroupId()))
                .menuProducts(findMenuProducts(menuRequest.getMenuProducts()))
                .build();

        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 그룹 입니다."));
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
                    .map(product -> new MenuProduct(product, menuProductRequest.getQuantity()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다.")));
        }

        return menuProducts;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
