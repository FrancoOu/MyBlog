package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.franco.domain.vo.RoutersVo;

import java.util.List;

/**
* @author franco
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2024-08-16 15:27:31
*/
public interface MenuService extends IService<Menu> {

    List<String> getPermsByUserId(Long id);

    RoutersVo getRouterMenuTreeByUserId(Long id);

    ResponseResult getMenus(String status, String menuName);

    ResponseResult getMenuTreeForRole();

    ResponseResult getRoleMenuTreeById(Long id);
}
