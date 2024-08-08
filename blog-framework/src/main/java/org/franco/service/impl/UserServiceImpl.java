package org.franco.service.impl;

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
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author franco
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-07-29 21:05:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();

        User user = getById(userId);

        UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);

        return ResponseResult.okResult(userInfo);

    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        Long userId = SecurityUtils.getUserId();
        if (!Objects.equals(userId, user.getId())){
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        updateById(user);

        return ResponseResult.okResult();
    }
}




