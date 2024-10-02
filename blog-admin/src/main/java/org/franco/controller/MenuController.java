package org.franco.controller;

import org.franco.domain.ResponseResult;
import org.franco.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @GetMapping
    public ResponseResult getMenus(String status, String menuName){

        return menuService.getMenus(status, menuName);
    }

//    @PostMapping
//    public ResponseResult addMenu(@ResponseBody MenuDto menuDto){
//
//    }

    @GetMapping("/menuTree/{id}")
    public ResponseResult getRoleMenuTreeById(@PathVariable Long id){
        return menuService.getRoleMenuTreeById(id);
    }
    @GetMapping("/tree")
    public ResponseResult getMenuTree(){
        return menuService.getMenuTreeForRole();
    }

}
