package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author franco
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-07-29 21:05:55
*/
public interface UserService extends IService<User> {

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);
}
