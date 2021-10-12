package kitchenpos.menu.application;

import kitchenpos.common.domain.price.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Component
public class MenuValidator {

    public void validateMenuPrice(Menu menu, Price productPrice) {
        if (menu.isMenuPriceGreaterThan(productPrice)) {
            throw new IllegalArgumentException("메뉴 가격이 속한 상품들 가격 합보다 비쌉니다.");
        }
    }

    public void validateMenuProducts(List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("메뉴에 1개 이상의 상품이 포함되어야 합니다.");
        }
    }

    public void validateExistMenuGroup(Optional<MenuGroup> menuGroupOptional) {
        menuGroupOptional.orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 그룹 입니다."));
    }
}
