package org.franco.service.impl;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.LoginUser;
import org.franco.domain.entity.User;
import org.franco.domain.vo.UserInfo;
import org.franco.domain.vo.UserLoginVo;
import org.franco.service.LoginService;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.JwtUtil;
import org.franco.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authentication)) {
            throw new RuntimeException("Wrong username or password");
        }
        // create JWT using user id
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        // store user info into redis
        redisCache.setCacheObject("login:"+userId, loginUser);
        UserInfo userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfo.class);

        UserLoginVo userLoginVo = new UserLoginVo(jwt, userInfo);

        return ResponseResult.okResult(userLoginVo);
    }

}
