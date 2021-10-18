package kitchenpos.product.application;

import kitchenpos.common.domain.price.Price;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductMenuServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductMenuServiceImpl productMenuService;


    @DisplayName("상품과 수량을 통해 금액을 계산할 수 있다.")
    @Test
    void calculateProductPrice() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(new Product("후라이드", BigDecimal.valueOf(16_000))));

        // then
        assertThat(productMenuService.calculatePrice(1L, 2L)).isEqualTo(new Price(BigDecimal.valueOf(32_000)));
    }

    @DisplayName("금액 계산시 상품을 찾지 못하는 경우 예외 발생")
    @Test
    void CalculateProductPriceNotFoundProduct() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        // then
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> {
            Price price = productMenuService.calculatePrice(1L, 2L);
        }).withMessageMatching("상품이 존재하지 않습니다.");
    }
}
