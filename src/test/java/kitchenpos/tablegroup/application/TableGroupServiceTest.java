package kitchenpos.tablegroup.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.ordertable.dao.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TableGroupServiceTest extends BaseServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable 빈테이블_1;
    private OrderTable 빈테이블_2;
    private OrderTable 비어있지_않은_테이블_1;

    private TableGroup 그룹_테이블_1;
    private TableGroup 그룹_테이블_2;

    private OrderTable 그룹_지정된_테이블_1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        빈테이블_1 = new OrderTable(1L,null, 0, true);
        빈테이블_2 = new OrderTable(2L, null, 0, true);
        비어있지_않은_테이블_1 = new OrderTable(9L, null, 5, false);

        그룹_테이블_1 = new TableGroup(1L, LocalDateTime.of(2021, 9, 4, 10, 30));
        그룹_테이블_2 = new TableGroup(2L, LocalDateTime.of(2021, 9, 4, 10, 30));

        그룹_지정된_테이블_1 = new OrderTable(10L, 그룹_테이블_1.getId(), 0, false);
    }

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈테이블_1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(빈테이블_2.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1, orderTableRequest2));

        // when
        TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(response.getCreatedDate()).isNotNull();
    }

    @DisplayName("주문 테이블이 2개 이상 있어야 한다.")
    @Test
    void requireTwoOrderTable() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(빈테이블_1.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageMatching("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.");
    }

    @DisplayName("주문 테이블 상태가 비어있음이 아니면 생성할 수 없다.")
    @Test
    void notEmptyOrderTableStatus() {
        // given
        OrderTableRequest orderTableRequestEmpty = new OrderTableRequest(빈테이블_1.getId());
        OrderTableRequest orderTableRequestNotEmpty = new OrderTableRequest(비어있지_않은_테이블_1.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequestEmpty, orderTableRequestNotEmpty));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageMatching("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 그룹_테이블_2.getId();
        List<OrderTable> orderTablesWithGroup = orderTableRepository.findAllByTableGroupId(그룹_테이블_2.getId());
        assertThat(orderTablesWithGroup.size()).isEqualTo(1);

        // when
        tableGroupService.unGroup(tableGroupId);


        // then
        List<OrderTable> orderTablesAfterUnGroup = orderTableRepository.findAllByTableGroupId(그룹_테이블_2.getId());
        assertThat(orderTablesAfterUnGroup.size()).isEqualTo(0);
    }

    @DisplayName("주문 테이블이 조리중 이거나 식사일때는 단체 지정을 해제할 수 없다.")
    @Test
    void unGroupFailWhenCookingOrMeal() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.unGroup(그룹_지정된_테이블_1.getTableGroupId());
        }).withMessageMatching("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
    }
}
