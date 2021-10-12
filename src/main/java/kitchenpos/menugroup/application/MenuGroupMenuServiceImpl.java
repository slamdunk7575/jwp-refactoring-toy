package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dao.MenuGroupRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuGroupMenuServiceImpl implements MenuGroupMenuService{

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupMenuServiceImpl(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public Optional<MenuGroup> findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId);
    }
}
