package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.RoleMenu;
import org.franco.domain.vo.MenuVo;
import org.franco.domain.entity.Menu;
import org.franco.domain.vo.RoleMenuVo;
import org.franco.domain.vo.RoutersVo;
import org.franco.service.MenuService;
import org.franco.mapper.MenuMapper;
import org.franco.service.RoleMenuService;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


    @Autowired
    RoleMenuService roleMenuService;

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

    @Override
    public ResponseResult getMenus(String status, String menuName) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status)
                .like(StringUtils.hasText(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getOrderNum);

        return ResponseResult.okResult(list(lambdaQueryWrapper));
    }

    @Override
    public ResponseResult getMenuTreeForRole() {
        MenuMapper menuMapper = getBaseMapper();

        return ResponseResult.okResult(buildMenuTree(menuMapper.selectAllRouterMenu(), 0L));
    }

    @Override
    public ResponseResult getRoleMenuTreeById(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<MenuVo> menuTree = buildMenuTree(menuMapper.selectAllRouterMenu(), 0L);

        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId, id);

        List<Long> menuIds = null;
        if (id == 1L) {
            menuIds = list().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());
        }else {
            menuIds = roleMenuService.list(lambdaQueryWrapper).stream()
                    .map(RoleMenu::getMenuId)
                    .collect(Collectors.toList());
        }
        return ResponseResult.okResult(new RoleMenuVo(menuTree, menuIds));
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




