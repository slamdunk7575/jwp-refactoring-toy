package kitchenpos.common.domain.name;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {

    private String name;

    protected Name() {
    }

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new InvalidNameException();
        }
    }

    public String value() {
        return name;
    }
}
