package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class TableGroupTest {

    private TableGroup 그룹_테이블_1;
    private OrderTable 빈테이블_1;
    private OrderTable 빈테이블_2;
    private OrderTable 비어있지_않은_주문_테이블_3;
    private OrderTable 그룹_지정된_테이블_1;

    @BeforeEach
    void setUp() {
        그룹_테이블_1 = new TableGroup(1L, LocalDateTime.of(2020, 9, 4, 10, 30));
        빈테이블_1 = new OrderTable(1L, null, 0, true);
        빈테이블_2 = new OrderTable(2L, null, 0, true);
        비어있지_않은_주문_테이블_3 = new OrderTable(2L, null, 0, false);
        그룹_지정된_테이블_1 = new OrderTable(10L, 그룹_테이블_1, 0, false);
    }

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        OrderTables orderTable = new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2));

        // when
        그룹_테이블_1.updateOrderTables(orderTable);

        // then
        assertThat(그룹_테이블_1).isNotNull();
        assertThat(빈테이블_1.getTableGroupId()).isNotNull();
        assertThat(빈테이블_2.getTableGroupId()).isNotNull();
    }

    @DisplayName("주문 테이블 상태가 비어있음이 아니면 단체 지정을 할 수 없다.")
    @Test
    void isNotEmptyStatus() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderTables orderTables = new OrderTables(Arrays.asList(빈테이블_1, 비어있지_않은_주문_테이블_3));
            그룹_테이블_1.updateOrderTables(orderTables);
        }).withMessageMatching("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
    }

    @DisplayName("이미 그룹이 지정되어 있으면 단체 지정을 할 수 없다.")
    @Test
    void alreadyGroupAssigned() {
        assertThatThrownBy(() -> {
            OrderTables orderTables = new OrderTables(Arrays.asList(빈테이블_1, 그룹_지정된_테이블_1));
            그룹_테이블_1.updateOrderTables(orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있지 않거나 이미 그룹이 지정된 테이블은 그룹 지정을 할 수 없습니다.");
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unGroup() {
        // given
        그룹_테이블_1.updateOrderTables(new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2)));

        // when
        그룹_테이블_1.unGroup();

        // then
        assertThat(빈테이블_1.getTableGroupId()).isNull();
        assertThat(빈테이블_2.getTableGroupId()).isNull();
    }
}
