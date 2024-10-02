package org.franco.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.franco.domain.entity.Role;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditVo {

    private List<Long> roleIds;

    private List<Role> roles;
    private UserVo user;
}
