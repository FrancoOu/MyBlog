package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);
}
