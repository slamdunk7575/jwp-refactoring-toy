package kitchenpos.ui;

import kitchenpos.order.application.OrderTableService;
import org.springframework.web.bind.annotation.*;

@RestController
public class TableRestController {
    private final OrderTableService orderTableService;

    public TableRestController(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    /*@PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = orderTableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok()
                .body(orderTableService.findAll());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable final Long orderTableId,
                                                  @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok()
                .body(orderTableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                           @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok()
                .body(orderTableService.changeNumberOfGuests(orderTableId, orderTable));
    }*/
}
