package kitchenpos.product.application;

import kitchenpos.common.domain.price.Price;

public interface ProductMenuService {

    Price calculatePrice(Long productId, Long productCount);
}
