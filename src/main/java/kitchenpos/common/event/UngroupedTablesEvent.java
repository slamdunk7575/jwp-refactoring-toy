package kitchenpos.common.event;

import java.util.List;

public class UngroupedTablesEvent {

    private List<Long> orderTableIds;

    public UngroupedTablesEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
