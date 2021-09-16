package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class OrderTableTest {

    private TableGroup 등록된_그룹 = new TableGroup(1L, LocalDateTime.now());
    private OrderTable 그룹이_지정된_빈_테이블;
    private OrderTable 그룹이_지정되지_않은_빈_테이블;
    private OrderTable 그룹이_지정되지_않은_비어있지_않은_테이블;

    @BeforeEach
    void setUp() {
        그룹이_지정된_빈_테이블 = new OrderTable(2L, 등록된_그룹, 0, true);
        그룹이_지정되지_않은_빈_테이블 = new OrderTable(1L, null, 0, true);
        그룹이_지정되지_않은_비어있지_않은_테이블 = new OrderTable(3L, null, 0, false);
    }

    @DisplayName("테이블 그룹 변경시, 주문 테이블 상태가 비어있지 않음으로 변경되어야 한다.")
    @Test
    void updateTableGroup() {
        // when
        그룹이_지정되지_않은_빈_테이블.updateTableGroup(등록된_그룹);

        // then
        assertThat(그룹이_지정되지_않은_빈_테이블.getTableGroupId()).isNotNull();
        assertThat(그룹이_지정되지_않은_빈_테이블.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 상태를 변경할 수 있다")
    @Test
    void updateEmpty() {
        // when
        그룹이_지정되지_않은_빈_테이블.updateEmpty(false);

        // then
        assertThat(그룹이_지정되지_않은_빈_테이블.isEmpty()).isFalse();
    }

    @DisplayName("그룹이 지정된 주문테이블 상태는 변경할 수 없다.")
    @Test
    void updateEmptyExistedGroupTable() {
        // when & then
        assertThatThrownBy(() -> 그룹이_지정된_빈_테이블.updateEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹 지정이 되어있어 상태를 변경할 수 없습니다.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            그룹이_지정된_빈_테이블.updateEmpty(false);
        }).withMessageMatching("그룹 지정이 되어있어 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블 손님 수를 변경할 수 있다.")
    @Test
    void updateNumberOfGuests() {
        // when
        int num = 그룹이_지정되지_않은_비어있지_않은_테이블.getNumberOfGuests();
        그룹이_지정되지_않은_비어있지_않은_테이블.updateNumberOfGuests(num + 3);

        // then
        assertThat(그룹이_지정되지_않은_비어있지_않은_테이블.getNumberOfGuests()).isEqualTo(num + 3);
    }

    @DisplayName("테이블 손님 수 변경시, 테이블 상태가 비어있다면 변경 할 수 없다.")
    @Test
    void updateNumberOfGuestWhenEmpty() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            그룹이_지정되지_않은_빈_테이블.updateNumberOfGuests(10);
        }).withMessageMatching("빈 주문 테이블의 손님 수는 변경할 수 없습니다.");
    }

}
