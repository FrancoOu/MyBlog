package org.franco.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.entity.RoleMenu;
import org.franco.service.RoleMenuService;
import org.franco.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author franco
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2024-10-02 13:05:00
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




