package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;
import org.franco.domain.vo.UserInfo;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.UserService;
import org.franco.mapper.UserMapper;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
* @author franco
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-07-29 21:05:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();

        User user = getById(userId);

        UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);

        return ResponseResult.okResult(userInfo);

    }


    //TODO: the old avatar should be deleted on google cloud storage
    @Override
    public ResponseResult updateUserInfo(User user) {
        Long userId = SecurityUtils.getUserId();
        if (!Objects.equals(userId, user.getId())){
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        updateById(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult createUser(User user) {

        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_USERNAME);
        }

        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_PASSWORD);
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_EMAIL);
        }

        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_NICKNAME);
        }

        // See if there is duplicate username or email
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // Hash the password
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        save(user);

        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getUserName,userName);

        return count(queryWrapper) > 0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getEmail,email);

        return count(queryWrapper) > 0;
    }
}




