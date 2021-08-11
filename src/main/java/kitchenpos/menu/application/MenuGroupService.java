package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroup));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> findAll() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }
}
