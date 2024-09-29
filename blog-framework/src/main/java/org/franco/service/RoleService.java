package org.franco.service;

import org.franco.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author franco
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2024-08-16 15:29:57
*/
public interface RoleService extends IService<Role> {

    List<String> getRoleByUserId(Long id);
}
