package org.franco.mapper;

import org.franco.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author franco
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2024-08-16 15:27:31
* @Entity org.franco.domain.entity.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long id);
}




