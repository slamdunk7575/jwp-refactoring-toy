package kitchenpos.product.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.common.domain.price.InvalidPriceException;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class ProductServiceTest extends BaseServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        ProductRequest productRequest = new ProductRequest("고추치킨", new BigDecimal(17000));

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse.getId()).isNotNull();
        assertThat(productResponse.getName()).isEqualTo("고추치킨");
        assertThat(productResponse.getPrice()).isEqualTo(BigDecimal.valueOf(17000));
    }


    @DisplayName("상품 생성시 가격은 필수 정보이다.")
    @Test
    void nullPriceException() {
        assertThatExceptionOfType(InvalidPriceException.class).isThrownBy(() -> {
            productService.create(new ProductRequest("고추치킨", null));
        }).withMessageMatching("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void negativePriceException() {
        assertThatThrownBy(() -> productService.create(new ProductRequest("고추치킨", new BigDecimal(-100))))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findProductList() {
        // when
        List<ProductResponse> results = productService.list();

        // then
        assertThat(results).isNotEmpty();
        assertThat(results.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList()))
                .containsAll(Arrays.asList("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"));
    }

}
