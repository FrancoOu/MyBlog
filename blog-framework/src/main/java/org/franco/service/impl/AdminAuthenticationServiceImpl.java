package org.franco.service.impl;

import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.vo.AdminUserInfoVo;
import org.franco.domain.vo.MenuVo;
import org.franco.domain.vo.RoutersVo;
import org.franco.domain.vo.UserInfo;
import org.franco.domain.entity.LoginUser;
import org.franco.domain.entity.User;
import org.franco.service.AdminAuthenticationService;
import org.franco.service.MenuService;
import org.franco.service.RoleService;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.JwtUtil;
import org.franco.utils.RedisCache;
import org.franco.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminAuthenticationServiceImpl implements AdminAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult login(User user) {
        // authenticate user
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
        redisCache.setCacheObject(SystemConstants.ADMIN_USER_LOGIN_KEY + userId, loginUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult getUserInfo() {

        // Get current loggedin user
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = loginUser.getUser();

        //Get perms and roles
        List<String> perms = menuService.getPermsByUserId(user.getId());
        List<String> roleKeyList = roleService.getRoleByUserId(user.getId());

        UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfo);

        return ResponseResult.okResult(adminUserInfoVo);

    }

    @Override
    public ResponseResult getRouters() {
        // Get current loggedin user
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = loginUser.getUser();
        RoutersVo routersVo = menuService.getRouterMenuTreeByUserId(user.getId());
        return ResponseResult.okResult(routersVo);
    }

    @Override
    public ResponseResult logout() {
        // get current user id
        Long userId = SecurityUtils.getUserId();
        // delete data in redis
        redisCache.deleteObject(SystemConstants.ADMIN_USER_LOGIN_KEY + userId);
        return ResponseResult.okResult();
    }


//    @Override
//    public ResponseResult logout() {
//        // get token
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        // get user id
//        Long id = loginUser.getUser().getId();
//        // delete user info stored in redis
//        redisCache.deleteObject("login:" + id);
//
//        return ResponseResult.okResult();
//    }

}
