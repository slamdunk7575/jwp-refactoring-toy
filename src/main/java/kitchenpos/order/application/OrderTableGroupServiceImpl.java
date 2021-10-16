package kitchenpos.order.application;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTableGroupServiceImpl implements OrderTableGroupService {

    private final OrderRepository orderRepository;

    public OrderTableGroupServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findOrdersByOrderTableIdIn(List<Long> orderTableIds) {
        return orderRepository.findByOrderTableIdIn(orderTableIds);
    }
}
