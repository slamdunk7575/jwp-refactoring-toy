package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;

import java.util.Optional;

public interface MenuGroupMenuService {
    Optional<MenuGroup> findMenuGroupById(Long menuGroupId);
}
