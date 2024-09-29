package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Article;
import org.franco.domain.entity.Category;
import org.franco.domain.vo.CategoryAdminVo;
import org.franco.domain.vo.CategoryVo;
import org.franco.service.ArticleService;
import org.franco.service.CategoryService;
import org.franco.mapper.CategoryMapper;
import org.franco.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}




