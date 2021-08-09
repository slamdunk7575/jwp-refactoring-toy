package kitchenpos.common.domain.price;

import kitchenpos.common.exception.kitchenPosServiceException;

public class InvalidPriceException extends kitchenPosServiceException {

    public InvalidPriceException() {
        super("가격은 0원 이상이어야 합니다.");
    }
}
