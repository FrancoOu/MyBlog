package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.dto.RoleDto;
import org.franco.domain.dto.RoleStatusDto;
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

    ResponseResult getRoles(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult updateRoleStatus(RoleStatusDto roleStatusDto);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult updateRole(RoleDto roleDto);

    ResponseResult deleteRoleById(Long id);
}
