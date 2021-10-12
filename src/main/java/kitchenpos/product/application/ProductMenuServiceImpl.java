package kitchenpos.product.application;

import kitchenpos.common.domain.price.Price;
import kitchenpos.common.domain.quantity.Quantity;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductMenuServiceImpl implements ProductMenuService {

    private final ProductRepository productRepository;

    public ProductMenuServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Price calculatePrice(Long productId, Long productCount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        return product.calculatePrice(Quantity.of(productCount));
    }
}
