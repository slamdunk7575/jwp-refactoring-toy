package kitchenpos.order.dao;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderTableId(Long orderTableId);

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);
}
