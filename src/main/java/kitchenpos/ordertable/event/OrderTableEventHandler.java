package kitchenpos.ordertable.event;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.ordertable.dao.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void groupedTables(GroupedTablesEvent groupedTablesEvent) {
        new OrderTables(orderTableRepository.findAllByIdIn(groupedTablesEvent.getOrderTableIds()))
                .updateTableGroup(groupedTablesEvent.getTableGroupId());
    }

    @EventListener
    public void ungroupedTables(UngroupedTablesEvent ungroupedTablesEvent) {
        new OrderTables(orderTableRepository.findAllByIdIn(ungroupedTablesEvent.getOrderTableIds()))
                .unGroup();
    }
}
