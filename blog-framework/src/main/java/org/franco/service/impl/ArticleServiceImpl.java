package org.franco.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Article;
import org.franco.domain.entity.Category;
import org.franco.domain.vo.DisplayedArticleVo;
import org.franco.domain.vo.HotArticleVo;
import org.franco.domain.vo.PageVo;
import org.franco.mapper.ArticleMapper;
import org.franco.service.ArticleService;
import org.franco.service.CategoryService;
import org.franco.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author franco
* @description 针对表【article(文章表)】的数据库操作Service实现
* @createDate 2024-07-19 16:27:05
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    public ResponseResult getHotArticles(){

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        need to find published articles and sort by views and 10 articles max
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_PUBLISHED)
                .select(Article::getId, Article::getTitle, Article::getViewCount)
                .orderByDesc(Article::getViewCount);

        Page<Article> page = new Page<>(1, 10);

        page(page, queryWrapper);

        List<Article> articleList = page.getRecords();

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articleList, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    };

    public ResponseResult getArticles(Integer pageNum, Integer pageSize, Long categoryId){

        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();

        //get articles by category if categoryId is not null
        articleQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);

        // find published articles
        articleQueryWrapper
                .eq(Article::getStatus, SystemConstants.ARTICLE_PUBLISHED)
                .orderByDesc(Article::getIsTop);

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, articleQueryWrapper);

        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
        categoryQueryWrapper
                .eq(Objects.nonNull(categoryId) && categoryId > 0, Category::getId, categoryId)
                .select(Category::getId, Category::getName);

        List<Category> categoryList = categoryService.list(categoryQueryWrapper);
        Map<Long, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        // set category name for each article

        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article -> article.setCategoryName(categoryMap.get(article.getCategoryId())))
                .collect(Collectors.toList());


        List<DisplayedArticleVo> displayedArticleVos = BeanCopyUtils.copyBeanList(articles, DisplayedArticleVo.class);

        // put articles into pageVo with total number of articles

        PageVo pageVo = new PageVo(displayedArticleVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }
}




