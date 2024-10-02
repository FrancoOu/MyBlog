package org.franco.controller;

import org.franco.domain.ResponseResult;
import org.franco.domain.dto.RoleDto;
import org.franco.domain.dto.RoleStatusDto;
import org.franco.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseResult getRoles(Integer pageNum, Integer pageSize, String roleName, String status){

        if (pageNum == null && pageSize == null && !StringUtils.hasText(roleName) && !StringUtils.hasText(status)){
            return ResponseResult.okResult(roleService.list());
        }else {
            return roleService.getRoles(pageNum, pageSize, roleName, status);
        }
    }

//    @GetMapping
//    public ResponseResult getAllRoles(){
//        return ResponseResult.okResult(roleService.list());
//    }

    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable Long id){
        return ResponseResult.okResult(roleService.getById(id));
    }


    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        return roleService.addRole(roleDto);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleDto roleDto){
        return roleService.updateRole(roleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable Long id){
        return roleService.deleteRoleById(id);
    }
    @PutMapping("/status")
    public ResponseResult updateRoleStatus(@RequestBody RoleStatusDto roleStatusDto){
        return roleService.updateRoleStatus(roleStatusDto);
    }

}
