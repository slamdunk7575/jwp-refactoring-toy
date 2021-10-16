package kitchenpos.order.application;

import kitchenpos.order.domain.Order;

import java.util.List;

public interface OrderTableGroupService {
    List<Order> findOrdersByOrderTableIdIn(List<Long> orderTableIds);
}
