package kitchenpos.ordertable.application;

import kitchenpos.ordertable.dao.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableTableGroupServiceImpl implements OrderTableTableGroupService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableTableGroupServiceImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<OrderTable> findOrderTableByIds(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    @Override
    public List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findByTableGroupId(tableGroupId).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
