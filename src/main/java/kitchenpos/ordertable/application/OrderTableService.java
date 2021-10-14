package kitchenpos.ordertable.application;

import kitchenpos.order.application.OrderOrderTableService;
import kitchenpos.ordertable.dao.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;
    private final OrderOrderTableService orderOrderTableService;

    public OrderTableService(OrderTableRepository orderTableRepository,
                             OrderTableValidator orderTableValidator,
                             OrderOrderTableService orderOrderTableService) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
        this.orderOrderTableService = orderOrderTableService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAll() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        orderTableValidator.validateOrderStatusIsCookingOrMeal(orderOrderTableService.findOrderByOrderTableId(orderTableId));
        savedOrderTable.updateEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블 입니다."));
    }
}
