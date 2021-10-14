package kitchenpos.order.application;

import kitchenpos.order.domain.Order;

import java.util.Optional;

public interface OrderOrderTableService {

    Optional<Order> findOrderByOrderTableId(Long orderTableId);
}
