package org.franco.controller;


import org.franco.domain.ResponseResult;
import org.franco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseResult getUserInfo() {

        return userService.getUserInfo();
    }


}
