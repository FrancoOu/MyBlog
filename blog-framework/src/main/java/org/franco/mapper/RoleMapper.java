package org.franco.mapper;

import org.franco.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author franco
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2024-08-16 15:29:57
* @Entity org.franco.domain.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getRoleByUserId(Long id);
}




