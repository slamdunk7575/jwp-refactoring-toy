package kitchenpos.menu.domain;

import kitchenpos.common.domain.price.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void updateMenu(Menu menu) {
        validateMenuPrice(new Price(menu.getPrice()));
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    private void validateMenuPrice(Price menuPrice) {
        if (menuPrice.isExpensive(getPriceSum())) {
            throw new IllegalArgumentException("메뉴 가격이 속한 상품들 가격 합보다 비쌉니다.");
        }
    }

    protected Price getPriceSum() {
        Price sum = Price.zero();
        for (MenuProduct menuProduct : menuProducts) {
            sum.add(menuProduct.getPricePerQuantity());
        }
        return sum;
    }

    public List<MenuProduct> findAll() {
        return Collections.unmodifiableList(menuProducts);
    }
}
