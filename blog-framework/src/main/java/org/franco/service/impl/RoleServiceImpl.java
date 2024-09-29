package org.franco.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.entity.Role;
import org.franco.service.RoleService;
import org.franco.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author franco
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2024-08-16 15:29:57
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public List<String> getRoleByUserId(Long id) {

        //if admin
        if (id == 1L) {
            List<String> roles = new ArrayList<>();
            roles.add("admin");
        }
        return getBaseMapper().getRoleByUserId(id);
    }
}




