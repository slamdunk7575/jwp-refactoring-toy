package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;

public interface OrderTableTableGroupService {

    List<OrderTable> findOrderTableByIds(List<Long> orderTableIds);

    List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId);
}
