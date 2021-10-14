package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderTableRestController {

    private final OrderTableService orderTableService;

    public OrderTableRestController(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = orderTableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> findAll() {
        return ResponseEntity.ok()
                .body(orderTableService.findAll());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, orderTableRequest));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, orderTableRequest));

    }

}
