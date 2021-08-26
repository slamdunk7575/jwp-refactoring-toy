package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final OrderTables orderTables) {
        updateOrderTables(orderTables);
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void updateOrderTables(OrderTables orderTables) {
        orderTables.updateTableGroup(this);
        this.orderTables = orderTables;
    }

    public void unGroup() {
        orderTables.checkOrderTableStatus();
        orderTables.unGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
