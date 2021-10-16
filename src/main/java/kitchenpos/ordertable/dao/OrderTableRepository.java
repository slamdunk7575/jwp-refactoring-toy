package kitchenpos.ordertable.dao;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    @Query("select o from OrderTable o where o.tableGroupId = :tableGroupId")
    List<OrderTable> findByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
