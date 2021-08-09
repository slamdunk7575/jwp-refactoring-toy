package kitchenpos.common.domain.quantity;

import kitchenpos.common.exception.kitchenPosServiceException;

public class InvalidQuantityException extends kitchenPosServiceException {

    public InvalidQuantityException() {
        super("수량은 0 이거나 음수 일 수 없습니다.");
    }
}
