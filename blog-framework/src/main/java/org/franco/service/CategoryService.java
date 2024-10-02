package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.dto.CategoryDto;
import org.franco.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author franco
* @description 针对表【category(分类表)】的数据库操作Service
* @createDate 2024-07-22 11:20:05
*/
public interface CategoryService extends IService<Category> {
    ResponseResult getCategories();

    ResponseResult getAllCategoryForAdmin();

    ResponseResult getPagedCategories(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult getCategoryById(Long id);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult updateCategory(CategoryDto categoryDto);

    ResponseResult deleteCategoryById(Long id);
}
