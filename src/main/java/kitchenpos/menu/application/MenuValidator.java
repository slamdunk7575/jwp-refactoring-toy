package kitchenpos.menu.application;

import kitchenpos.common.domain.price.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class MenuValidator {

    public void validateMenuPrice(Menu menu, Price menuPrice) {
        if (menuPrice.isExpensive(menu.getMenuProductsPriceSum())) {
            throw new IllegalArgumentException("메뉴 가격이 속한 상품들 가격 합보다 비쌉니다.");
        }
    }

    public void validateMenuProducts(List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("메뉴에 1개 이상의 상품이 포함되어야 합니다.");
        }
    }
}
