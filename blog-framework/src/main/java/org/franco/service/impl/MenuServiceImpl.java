package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.constants.SystemConstants;
import org.franco.domain.vo.MenuVo;
import org.franco.domain.entity.Menu;
import org.franco.domain.vo.RoutersVo;
import org.franco.service.MenuService;
import org.franco.mapper.MenuMapper;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author franco
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2024-08-16 15:27:31
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Override
    public List<String> getPermsByUserId(Long id) {
        // if admin, return all permissions
        if (id == 1L) {
            LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .in(Menu::getMenuType, SystemConstants.SYS_MENU_TYPE_MENU, SystemConstants.SYS_MENU_TYPE_BUTTON)
                    .eq(Menu::getStatus, SystemConstants.SYS_MENU_STATUS_NORMAL);
            List<Menu> menus = list(lambdaQueryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public RoutersVo getRouterMenuTreeByUserId(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        // whether the current user is admin
        if(SecurityUtils.isAdmin()){
            // get all menus
            menus = menuMapper.selectAllRouterMenu();
        }else {
            menus = menuMapper.selectRouterMenuTreeByUserId(id);
        }

        return new RoutersVo(buildMenuTree(menus, 0L));
    }

    public List<MenuVo> buildMenuTree(List<Menu> menus, Long parentId){
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        List<MenuVo> menuTree = menuVos.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> {
                    menu.setChildren(getChildren(menu, menuVos));
                    return menu;
                })
                .collect(Collectors.toList());

        return menuTree;

    }

    public List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus){

        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m-> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}




