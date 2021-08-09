package kitchenpos.ordertable.application;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(10, true);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // given
        when(orderTableDao.save(orderTable)).thenReturn(this.orderTable);

        // when
        OrderTable createdOrderTable = tableService.create(this.orderTable);

        // then
        assertThat(createdOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(createdOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
        assertThat(createdOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllOrderTableList() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables.get(0).getId()).isEqualTo(orderTable.getId());
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(orderTables.get(0).isEmpty()).isEqualTo(orderTable.isEmpty());
        assertThat(orderTables.get(0).getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블의 등록 가능 상태를 변경할 수 있다.")
    @Test
    void changeOrderTableStatus() {
        // given
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when
        OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        // then
        assertThat(updatedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @DisplayName("주문이 조리중 이거나 식사중 일대는 상태를 변경할 수 없다.")
    @Test
    void notChangeStatus() {
        // given
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
           tableService.changeEmpty(orderTable.getId(), orderTable);
        });
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 등록한다.")
    @Test
    void updateNumberOfGuests() {
        // given
        orderTable.setEmpty(false);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(10);

        // when
        tableService.changeNumberOfGuests(this.orderTable.getId(), updateOrderTable);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(updateOrderTable.getNumberOfGuests());
    }

    @DisplayName("방문한 손님 수는 0명 미만으로 입력할 수 없다.")
    @Test
    void zeroOrMoreNumberOfGuests() {
        // given
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(-10);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
           tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable);
        });
    }

    @DisplayName("주문 테이블 상태가 비어있음인 경우 등록할 수 없다.")
    @Test
    void notEmptyOrderTableStatus() {
        // given
        orderTable.setEmpty(true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        OrderTable updatedOrderTable = new OrderTable();
        updatedOrderTable.setNumberOfGuests(10);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
           tableService.changeNumberOfGuests(orderTable.getId(), updatedOrderTable);
        });
    }
}