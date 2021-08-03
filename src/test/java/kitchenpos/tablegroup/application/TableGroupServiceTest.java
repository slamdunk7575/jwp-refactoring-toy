package kitchenpos.tablegroup.application;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = new OrderTable(10, true);
        secondOrderTable = new OrderTable(20, true);
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createdTableGroup.getId()).isEqualTo(tableGroup.getId());
        assertThat(createdTableGroup.getOrderTables()).isEqualTo(tableGroup.getOrderTables());
        assertThat(createdTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("주문 테이블이 2개 이상 있어야 한다.")
    @Test
    void requireLeastTwoOrderTables() {
        // given
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable));

        // when
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @DisplayName("주문 테이블 상태가 비어있음이 아니면 생성할 수 없다.")
    @Test
    void notEmptyOrderTableStatus() {
        // given
        firstOrderTable.setEmpty(false);
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @DisplayName("단체 지정이 되어 있으면 생성할 수 없다.")
    @Test
    void alreadyExistTableGroupId() {
        // given
        firstOrderTable.setTableGroupId(1L);
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderTableDao.findAllByIdIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId())))
                .willReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));
        firstOrderTable.setTableGroupId(1L);
        secondOrderTable.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(firstOrderTable.getId()).isNull();
        assertThat(secondOrderTable.getId()).isNull();
    }

    @DisplayName("주문 테이블이 조리중이거나 식사중일때는 단체 지정을 해제할 수 없다.")
    @Test
    void ungroupFailWhenIsCookingOrMeal() {
        // given
        TableGroup tableGroup = new TableGroup(Arrays.asList(firstOrderTable, secondOrderTable));
        firstOrderTable.setTableGroupId(1L);
        secondOrderTable.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
       });
    }
}
