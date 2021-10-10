package kitchenpos.menu.domain;

import kitchenpos.common.domain.price.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Price getPriceSum() {
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
