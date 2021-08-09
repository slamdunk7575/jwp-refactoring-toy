package kitchenpos.product.domain;

import kitchenpos.common.domain.name.InvalidNameException;
import kitchenpos.common.domain.price.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        String name = "고추치킨";
        BigDecimal price = BigDecimal.valueOf(17000);

        // when
        Product product = new Product(name, price);

        // then
        assertThat(product).isNotNull();
    }

    @Test
    @DisplayName("상품의 이름과 가격은 필수 정보이다.")
    void requireNameAndPrice() {
        assertThatThrownBy(() -> {
            new Product(null, BigDecimal.valueOf(17000));
        }).isInstanceOf(InvalidNameException.class)
                .hasMessageEndingWith("이름은 필수 정보입니다.");

        assertThatThrownBy(() -> {
            new Product("고추치킨", null);
        }).isInstanceOf(InvalidPriceException.class)
                .hasMessageEndingWith("가격은 0원 이상이어야 합니다.");
    }
}
