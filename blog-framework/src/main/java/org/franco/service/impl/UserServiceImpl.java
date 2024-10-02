package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.AdminUserDto;
import org.franco.domain.entity.ArticleTag;
import org.franco.domain.entity.Role;
import org.franco.domain.entity.User;
import org.franco.domain.entity.UserRole;
import org.franco.domain.vo.PageVo;
import org.franco.domain.vo.UserEditVo;
import org.franco.domain.vo.UserInfo;
import org.franco.domain.vo.UserVo;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.RoleService;
import org.franco.service.UserRoleService;
import org.franco.service.UserService;
import org.franco.mapper.UserMapper;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

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

    @Override
    public ResponseResult getUsers(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper
                .like(StringUtils.hasText(userName), User::getUserName, userName)
                .eq(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber)
                .eq(StringUtils.hasText(status), User::getStatus, status);

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        List<UserVo> userVoList = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);
        PageVo pageVo = new PageVo(userVoList, page.getTotal());


        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addUser(AdminUserDto adminUserDto) {
        if (!StringUtils.hasText(adminUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_USERNAME);
        }

        if (!StringUtils.hasText(adminUserDto.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_PASSWORD);
        }

        if (!StringUtils.hasText(adminUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_EMAIL);
        }

        if (!StringUtils.hasText(adminUserDto.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_NICKNAME);
        }

        // See if there is duplicate username or email
        if (userNameExist(adminUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (emailExist(adminUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        User newUser = BeanCopyUtils.copyBean(adminUserDto, User.class);

        // Hash the password
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());

        newUser.setPassword(encodedPassword);

        save(newUser);

        // Add role ids
        List<UserRole> userRoles = adminUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(newUser.getId(), roleId))
                .collect(Collectors.toList());

        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteAdminUserById(Long id) {
        removeById(id);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        userRoleService.remove(queryWrapper);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateAdminUser(AdminUserDto adminUserDto) {
        User updatedUser = BeanCopyUtils.copyBean(adminUserDto, User.class);
        updateById(updatedUser);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, adminUserDto.getId());
        userRoleService.remove(queryWrapper);

        List<UserRole> userRoles = adminUserDto.getRoleIds().stream()
                .map(id -> new UserRole(adminUserDto.getId(), id))
                .collect(Collectors.toList());

        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {

        UserVo user = BeanCopyUtils.copyBean(getById(id), UserVo.class);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<Long> roleIds = userRoleService.list(queryWrapper).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        List<Role> roles = roleService.list();

        UserEditVo userEditVo = new UserEditVo(roleIds, roles, user);

        return ResponseResult.okResult(userEditVo);
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




