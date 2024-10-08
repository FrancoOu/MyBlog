package org.franco.controller;


import com.alibaba.excel.EasyExcel;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.CategoryDto;
import org.franco.domain.entity.Category;
import org.franco.domain.vo.CategorySheetVo;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.service.CategoryService;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseResult getAllCategory(Integer pageNum, Integer pageSize, String name, String status){

        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize)){
            return categoryService.getAllCategoryForAdmin();

        }else {
            return categoryService.getPagedCategories(pageNum, pageSize, name, status);

        }
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id) {

        return categoryService.getCategoryById(id);

    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){

        return categoryService.addCategory(categoryDto);

    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable Long id){
        return categoryService.deleteCategoryById(id);

    }

    @PreAuthorize("@permissionService.hasPermission('content:category:export')")
    @GetMapping("/sheet")
    public void getCategorySheet(HttpServletResponse response){
        try {
            // set request header
            WebUtils.setDownLoadHeader("Category.xlsx", response);
            // get data
            List<Category> categoryVoList = categoryService.list();
            List<CategorySheetVo> categorySheetVos = BeanCopyUtils.copyBeanList(categoryVoList, CategorySheetVo.class);

            EasyExcel.write(response.getOutputStream(), CategorySheetVo.class).autoCloseStream(Boolean.FALSE).sheet("Category")
                    .doWrite(categorySheetVos);

            // write data into sheet
        } catch (Exception e) {
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.FILE_DOWNLOAD_ERROR);
            WebUtils.renderString(response, responseResult);
        }

    }

}
