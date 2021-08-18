package kitchenpos.menu.domain;

import kitchenpos.common.domain.name.InvalidNameException;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupTest {

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroup() {
        // given
        String name = "추천메뉴";

        // when
        MenuGroup menuGroup = new MenuGroup(name);

        // then
        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("메뉴 그룹 이름은 필수정보이다.")
    @Test
    void requireMenuGroupName() {
        // when & then
        assertThatThrownBy(() -> new MenuGroup(""))
                .isInstanceOf(InvalidNameException.class).hasMessage("이름은 필수 정보입니다.");
    }
}
