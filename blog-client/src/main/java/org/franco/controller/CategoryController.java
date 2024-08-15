package org.franco.controller;

import io.swagger.annotations.Api;
import org.franco.domain.ResponseResult;
import org.franco.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(tags = "Category", description = "Interfaces for Category")

public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseResult getCategories() {
        return categoryService.getCategories();
    }
}
