package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;

public interface AuthenticationService {
    ResponseResult login(User user);
    ResponseResult logout();
}
