package org.franco.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.entity.UserRole;
import org.franco.service.UserRoleService;
import org.franco.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author franco
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2024-10-02 15:42:23
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




