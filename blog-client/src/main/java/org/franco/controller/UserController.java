package org.franco.controller;


import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;
import org.franco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseResult getUserInfo() {

        return userService.getUserInfo();
    }

    @PutMapping
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }


}
