package kitchenpos.menu.dto;

import kitchenpos.product.domain.Product;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public boolean isSameProductId(Product product) {
        return this.productId.equals(product.getId());
    }

}
