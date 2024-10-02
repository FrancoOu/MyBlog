package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.RoleDto;
import org.franco.domain.dto.RoleStatusDto;
import org.franco.domain.entity.ArticleTag;
import org.franco.domain.entity.Role;
import org.franco.domain.entity.RoleMenu;
import org.franco.domain.vo.PageVo;
import org.franco.service.RoleMenuService;
import org.franco.service.RoleService;
import org.franco.mapper.RoleMapper;
import org.franco.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author franco
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2024-08-16 15:29:57
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public List<String> getRoleByUserId(Long id) {

        //if admin
        if (id == 1L) {
            List<String> roles = new ArrayList<>();
            roles.add("admin");
        }
        return getBaseMapper().getRoleByUserId(id);
    }

    @Override
    public ResponseResult getRoles(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(StringUtils.hasText(status), Role::getStatus, status)
                .like(StringUtils.hasText(roleName), Role::getRoleName, roleName)
                .orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRoleStatus(RoleStatusDto roleStatusDto) {
        Role role = getById(roleStatusDto.getRoleId());

        role.setStatus(roleStatusDto.getStatus());

        updateById(role);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleDto roleDto) {
        Role newRole = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(newRole);
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                .map(menu-> new RoleMenu(newRole.getId(), menu))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateRole(RoleDto roleDto) {
        Role updatedRole = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(updatedRole);

        // delete original role menu
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleDto.getId());
        roleMenuService.remove(queryWrapper);

        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                .map(menu-> new RoleMenu(updatedRole.getId(), menu))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteRoleById(Long id) {
        removeById(id);

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        roleMenuService.remove(queryWrapper);
        return ResponseResult.okResult();
    }
}




