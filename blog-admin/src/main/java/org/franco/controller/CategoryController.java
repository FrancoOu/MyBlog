package org.franco.controller;


import org.franco.domain.ResponseResult;
import org.franco.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseResult getAllCategory(){
        return categoryService.getAllCategoryForAdmin();
    }
}
