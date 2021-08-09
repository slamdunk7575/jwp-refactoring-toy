package kitchenpos.common.domain.name;

import kitchenpos.common.exception.kitchenPosServiceException;

public class InvalidNameException extends kitchenPosServiceException {

    public InvalidNameException() {
        super("이름은 필수 정보입니다.");
    }
}
