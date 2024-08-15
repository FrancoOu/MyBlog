package org.franco.controller;

import io.swagger.annotations.Api;
import org.franco.annotation.SystemLog;
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
@Api(tags = "Authentication", description = "Interfaces for Authentication")

public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    @SystemLog(controllerDescription = "User log in")
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
