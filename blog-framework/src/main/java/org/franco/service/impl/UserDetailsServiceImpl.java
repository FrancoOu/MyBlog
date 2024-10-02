package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.franco.constants.SystemConstants;
import org.franco.domain.entity.LoginUser;
import org.franco.domain.entity.User;
import org.franco.mapper.MenuMapper;
import org.franco.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getUserName, username);

        User user = userMapper.selectOne(queryWrapper);

        if (Objects.isNull(user)){

            throw new UsernameNotFoundException("User does not exist");
        }

        // get permissions for admin user
        if (SystemConstants.ADMIN_USER.equals(user.getType())){
            List<String> perms = menuMapper.selectPermsByUserId(user.getId());

            return new LoginUser(user, perms);
        }

        return new LoginUser(user, null);
    }
}
