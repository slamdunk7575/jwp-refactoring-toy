package kitchenpos.ordertable.event;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class OrderTableEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private OrderTableEventHandler orderTableEventHandler;

    @DisplayName("단체지정 이벤트 핸들러가 처리되는 것을 확인")
    @Test
    void groupedTablesEvent() {
        // when
        applicationEventPublisher.publishEvent(new GroupedTablesEvent(Arrays.asList(1L, 2L), 1L));

        // then
        verify(orderTableEventHandler).groupedTables(any());
    }

    @DisplayName("단체해제 이벤트 핸들러가 처리되는 것을 확인")
    @Test
    void unGroupedTablesEvent() {
        // when
        applicationEventPublisher.publishEvent(new UngroupedTablesEvent(Arrays.asList(1L, 2L)));

        // then
        verify(orderTableEventHandler).ungroupedTables(any());
    }
}
