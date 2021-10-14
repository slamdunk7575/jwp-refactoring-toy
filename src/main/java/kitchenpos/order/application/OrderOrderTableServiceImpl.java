package kitchenpos.order.application;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderOrderTableServiceImpl implements OrderOrderTableService {

    private final OrderRepository orderRepository;

    public OrderOrderTableServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId);
    }
}
