package kitchenpos.ordertable.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

// @Embeddable
public class OrderTables {

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void updateTableGroup(Long tableGroupId) {
        orderTables.forEach(orderTable ->  orderTable.updateTableGroup(tableGroupId));
    }

    public void unGroup() {
        this.orderTables.forEach(OrderTable::unGroup);
    }
}
