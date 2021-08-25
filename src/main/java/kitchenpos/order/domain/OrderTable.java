package kitchenpos.order.domain;

import com.sun.nio.sctp.IllegalReceiveException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void checkOrderTableEmptyOrAssigned() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalReceiveException("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다. ");
        }
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public boolean isNotComplete() {
        return orders.hasNotComplete();
    }

    public void unGroup() {
        if (orders.hasNotComplete()) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 그룹해제 할 수 없습니다.");
        }
        this.tableGroup = null;
    }

    public void updateEmpty(boolean empty) {
        checkOrderTableGroup();
        checkOrderTableStatus();
        this.empty = empty;
    }

    private void checkOrderTableStatus() {
        if (isNotComplete()) {
            throw new IllegalArgumentException("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
        }
    }

    private void checkOrderTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalReceiveException("그룹 지정이 되어있어 상태를 변경할 수 없습니다.");
        }
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        checkOrderTableNotEmpty();
        this.numberOfGuests.update(numberOfGuests);
    }

    private void checkOrderTableNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 주문 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }
}
