package org.franco.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuVo {
    private List<MenuVo> menus;
    private List<Long> checkedKeys;
}
