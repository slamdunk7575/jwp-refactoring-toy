package kitchenpos.order.application;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.dao.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = findOrderTables(tableGroupRequest.getOrderTableIds());
        return TableGroupResponse.of(tableGroupRepository.save(new TableGroup(orderTables)));
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        if (!savedOrderTables.isSameSize(orderTableIds.size())) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 포함되어 있습니다.");
        }
        return savedOrderTables;
    }

    @Transactional
    public void unGroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 그룹입니다."));
        checkOrderTableStatus(tableGroup.getOrderTables());
        tableGroup.unGroup();
    }

    private void checkOrderTableStatus(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables,
                OrderStatus.NOT_CHANGE_ORDER_STATUS)) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 그룹 지정은 해제할 수 없습니다.");
        }
    }
}
