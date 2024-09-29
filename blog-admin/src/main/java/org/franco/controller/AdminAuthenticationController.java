package org.franco.controller;


import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.AdminAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdminAuthenticationController {

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminAuthenticationService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){

        return adminAuthenticationService.logout();
    }

    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(){
        return adminAuthenticationService.getUserInfo();
    }

    @GetMapping("/routers")
    public ResponseResult getRouters(){
        return adminAuthenticationService.getRouters();
    }
}
