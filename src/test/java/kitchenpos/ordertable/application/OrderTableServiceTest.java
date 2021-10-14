package kitchenpos.ordertable.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class OrderTableServiceTest extends BaseServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    private OrderTable 빈_테이블;
    private OrderTable 그룹_지정된_테이블;
    private OrderTable 그룹_지정되지_않은_테이블;
    private TableGroup 그룹_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        그룹_테이블 = new TableGroup(2L, LocalDateTime.of(2021, 9, 4, 10, 30));
        빈_테이블 = new OrderTable(1L, null, 0, true);
        그룹_지정된_테이블 = new OrderTable(12L, 그룹_테이블.getId(), 0, false);
        그룹_지정되지_않은_테이블 = new OrderTable(13L, null, 3, false);
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createOrderTable() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        // when
        OrderTableResponse result = orderTableService.create(orderTableRequest);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllOrderTable() {
        // when
        List<OrderTableResponse> responses = orderTableService.findAll();

        // then
        assertThat(responses.size()).isEqualTo(13);
        assertThat(responses.stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList())).containsAll(Arrays.asList(1L, 2L, 3L, 4L, 13L));
    }

    @DisplayName("빈 테이블로 설정 또는 해지할 수 있다.")
    @Test
    void changeOrderTableStatus() {
        // given
        Long emptyTableId = 빈_테이블.getId();
        boolean changedStatus = !빈_테이블.isEmpty();
        OrderTableRequest changeEmptyRequest = new OrderTableRequest(changedStatus);

        // when
        OrderTableResponse result = orderTableService.changeEmpty(emptyTableId, changeEmptyRequest);

        // then
        assertThat(result.getId()).isEqualTo(emptyTableId);
        assertThat(result.isEmpty()).isEqualTo(changedStatus);
    }

    @DisplayName("단체 지정이 되어 있다면 상태를 변경할 수 없다.")
    @Test
    void alreadyExistOrderTableGroup() {
        // given
        Long orderTableId = 그룹_지정된_테이블.getId();
        OrderTableRequest changeEmptyRequest = new OrderTableRequest(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderTableResponse response = orderTableService.changeEmpty(orderTableId, changeEmptyRequest);
        }).withMessageMatching("그룹 지정이 되어있어 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문이 조리중 이거나 식사중 일때는 상태를 변경할 수 없다.")
    @Test
    void notChangeStatusWhenCookingOrMeal() {
        // given
        Long orderTableId = 그룹_지정되지_않은_테이블.getId();
        OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTableId, changeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중 또는 식사중인 주문 테이블의 상태는 변경할 수 없습니다.");
    }

    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        Long orderTableId = 그룹_지정된_테이블.getId();
        int changeNumberOfGuests = 20;
        OrderTableRequest changeNumberOfGuestsRequest = new OrderTableRequest(changeNumberOfGuests);

        // when
        OrderTableResponse result = orderTableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest);

        // then
        assertThat(result.getId()).isEqualTo(orderTableId);
        assertThat(result.getNumberOfGuests()).isEqualTo(changeNumberOfGuests);
    }

    @DisplayName("방문한 손님 수는 0명 미만으로 입력할 수 없다.")
    @Test
    void requireNumberOfGuestsZeroOrMore() {
        // given
        Long orderTableId = 그룹_지정된_테이블.getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(-10);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderTableService.changeNumberOfGuests(orderTableId, orderTableRequest);
        }).withMessageMatching("손님 수는 0 보다 작을 수 없습니다.");
    }

    @DisplayName("주문 테이블 상태가 비어있음인 경우 등록할 수 없다.")
    @Test
    void emptyOrderTableStatus() {
        // given
        Long orderTableId = 빈_테이블.getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(5);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderTableService.changeNumberOfGuests(orderTableId, orderTableRequest);
        }).withMessageMatching("빈 주문 테이블의 손님 수는 변경할 수 없습니다.");
    }

}
