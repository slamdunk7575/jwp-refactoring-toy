package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.dto.OrderTableRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables = new ArrayList<>();

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        if (Objects.nonNull(orderTables)) {
            this.orderTables = orderTables;
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
