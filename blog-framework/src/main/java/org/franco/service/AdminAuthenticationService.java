package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;

public interface AdminAuthenticationService {

    ResponseResult login(User user);

    ResponseResult getUserInfo();

    ResponseResult getRouters();

    ResponseResult logout();
}
