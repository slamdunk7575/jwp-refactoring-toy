package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.NumberOfGuests;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void checkOrderTableEmptyOrAssigned() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
        }
    }

    public void updateTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void updateEmpty(boolean empty) {
        validateOrderTableGroup();
        this.empty = empty;
    }

    private void validateOrderTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("그룹 지정이 되어있어 상태를 변경할 수 없습니다.");
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

    public Long getTableGroupId() {
        if (Objects.nonNull(tableGroupId)){
            return tableGroupId;
        }
        return null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }
}
