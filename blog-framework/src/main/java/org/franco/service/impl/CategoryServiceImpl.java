package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.CategoryDto;
import org.franco.domain.entity.Article;
import org.franco.domain.entity.Category;
import org.franco.domain.vo.CategoryAdminVo;
import org.franco.domain.vo.CategoryVo;
import org.franco.domain.vo.PageVo;
import org.franco.service.ArticleService;
import org.franco.service.CategoryService;
import org.franco.mapper.CategoryMapper;
import org.franco.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author franco
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2024-07-22 11:20:05
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategories() {
//        get articles that are published
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_PUBLISHED);
        List<Article> articleList = articleService.list(articleWrapper);

//        get categories
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        List<Category> categories = listByIds(categoryIds);

//        get all categories
        categories = categories.stream()
                .filter(category -> category.getStatus().equals(SystemConstants.CATEGORY_PUBLISHED))
                .collect(Collectors.toList());

        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getAllCategoryForAdmin() {
        // get published categories
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getName, Category::getId, Category::getDescription);
        queryWrapper.eq(Category::getStatus, SystemConstants.CATEGORY_PUBLISHED);

        List<Category> categories = list(queryWrapper);

        List<CategoryAdminVo> categoryAdminVos = BeanCopyUtils.copyBeanList(categories, CategoryAdminVo.class);

        return ResponseResult.okResult(categoryAdminVos);

    }

    @Override
    public ResponseResult getPagedCategories(Integer pageNum, Integer pageSize, String name, String status) {
        // set query
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper
                .like(StringUtils.hasText(name), Category::getName, name)
                .eq(StringUtils.hasText(status), Category::getStatus, status);


        // set pagination
        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,lambdaQueryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getCategoryById(Long id) {

        CategoryAdminVo categoryAdminVo = BeanCopyUtils.copyBean(getById(id),CategoryAdminVo.class);

        return ResponseResult.okResult(categoryAdminVo);
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category newCategory = BeanCopyUtils.copyBean(categoryDto, Category.class);

        save(newCategory);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateCategory(CategoryDto categoryDto) {

        Category updatedCategory = BeanCopyUtils.copyBean(categoryDto, Category.class);

        updateById(updatedCategory);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {

        removeById(id);

        return ResponseResult.okResult();
    }
}




