package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long id;
    private Long menuId;
    private Long productId;
    private long quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity().value());
    }

    public static List<MenuProductResponse> ofList(MenuProducts menuProducts) {
        return menuProducts.findAll().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
