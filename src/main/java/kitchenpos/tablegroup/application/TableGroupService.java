package kitchenpos.tablegroup.application;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.order.application.OrderTableGroupService;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.application.OrderTableTableGroupService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.dao.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableTableGroupService orderTableTableGroupService;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderTableGroupService orderTableGroupService;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableTableGroupService orderTableTableGroupService,
                             TableGroupValidator tableGroupValidator,
                             ApplicationEventPublisher applicationEventPublisher,
                             OrderTableGroupService orderTableGroupService) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableTableGroupService = orderTableTableGroupService;
        this.tableGroupValidator = tableGroupValidator;
        this.applicationEventPublisher = applicationEventPublisher;
        this.orderTableGroupService = orderTableGroupService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableTableGroupService.findOrderTableByIds(orderTableIds);
        tableGroupValidator.validateOrderTablesToCreateTableGroup(savedOrderTables, orderTableIds);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        applicationEventPublisher.publishEvent(new GroupedTablesEvent(orderTableIds, tableGroup.getId()));
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void unGroup(final Long tableGroupId) {
        List<Long> orderTableIds = orderTableTableGroupService.findOrderTableIdsByTableGroupId(tableGroupId);
        List<Order> orders = orderTableGroupService.findOrdersByOrderTableIdIn(orderTableIds);
        tableGroupValidator.validateOrderStatusIsCookingOrMeal(orders);
        applicationEventPublisher.publishEvent(new UngroupedTablesEvent(orderTableIds));
    }
}
