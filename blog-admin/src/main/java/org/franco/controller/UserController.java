package org.franco.controller;

import org.franco.domain.ResponseResult;
import org.franco.domain.dto.AdminUserDto;
import org.franco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseResult getUsers(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status){

        return userService.getUsers(pageNum, pageSize, userName, phonenumber, status);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseResult addAdminUser(@RequestBody AdminUserDto adminUserDto){
        return userService.addUser(adminUserDto);
    }

    @PutMapping()
    public ResponseResult updateAdminUser(@RequestBody AdminUserDto adminUserDto){
        return userService.updateAdminUser(adminUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteAdminUserById(@PathVariable Long id){
        return userService.deleteAdminUserById(id);
    }

}
