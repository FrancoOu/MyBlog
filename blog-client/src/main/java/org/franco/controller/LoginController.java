package org.franco.controller;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return authenticationService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {

        return authenticationService.logout();
    }
}
