package org.franco.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.franco.domain.entity.Menu;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
    private List<MenuVo> menus;


}
