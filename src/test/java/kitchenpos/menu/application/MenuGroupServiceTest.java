package kitchenpos.menu.application;

import kitchenpos.BaseServiceTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupServiceTest extends BaseServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");

        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(menuGroupResponse.getId()).isNotNull();
        assertThat(menuGroupResponse.getName()).isEqualTo("추천메뉴");
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void findAllMenuGroups() {
        // when
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.findAll();

        // then
        assertThat(menuGroupResponses).isNotEmpty();
        assertThat(menuGroupResponses.stream()
                .map(response -> response.getName())
                .collect(Collectors.toList()))
                .containsAll(Arrays.asList("두마리메뉴", "한마리메뉴",
                        "순살파닭두마리메뉴", "신메뉴"));
    }
    
}
