package org.franco.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.ArticleDto;
import org.franco.domain.dto.ArticleListDto;
import org.franco.domain.entity.Article;
import org.franco.domain.entity.ArticleTag;
import org.franco.domain.entity.Category;
import org.franco.domain.entity.Tag;
import org.franco.domain.vo.*;
import org.franco.mapper.ArticleMapper;
import org.franco.service.ArticleService;
import org.franco.service.ArticleTagService;
import org.franco.service.CategoryService;
import org.franco.utils.BeanCopyUtils;
import org.franco.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

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

    @Override
    public ResponseResult getArticleById(Long id) {
        Article article = getById(id);

        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT_MAP_KEY, String.valueOf(id));
        //Get view count from redis
        article.setViewCount(viewCount.longValue());
        SingleArticleVo singleArticleVo = BeanCopyUtils.copyBean(article, SingleArticleVo.class);
        Long categoryId = article.getCategoryId();

        Category category = categoryService.getById(categoryId);
        if (category != null) {
            singleArticleVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(singleArticleVo);
    }

    /**
     * update the view count
     * @param id article id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT_MAP_KEY, id.toString(), 1);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(ArticleDto articleDto) {
        Article newArticle = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(newArticle);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tag-> new ArticleTag(newArticle.getId(), tag))
                .collect(Collectors.toList());

        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getPagedArticles(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        // set query
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(articleListDto.getSummary()), Article::getSummary, articleListDto.getSummary());
        queryWrapper.like(StringUtils.hasText(articleListDto.getTitle()), Article::getTitle, articleListDto.getTitle());

        // set pagination
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        List<ArticleAdminVo> articleAdminVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleAdminVo.class);

        // put data in pageVo
        PageVo pageVo = new PageVo(articleAdminVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleByIdForEdit(Long id) {
        Article article = getById(id);
        ArticleEditVo articleEditVo = BeanCopyUtils.copyBean(article, ArticleEditVo.class);
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, id);
        List<Long> tagIds = articleTagService.list(lambdaQueryWrapper).stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        articleEditVo.setTags(tagIds);
        return ResponseResult.okResult(articleEditVo);
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(ArticleDto articleDto) {

        // update article
        Article updatedArticle = BeanCopyUtils.copyBean(articleDto, Article.class);
        updateById(updatedArticle);

        // delete original tags
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleDto.getId());
        articleTagService.remove(queryWrapper);

        // add new tags
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tag-> new ArticleTag(updatedArticle.getId(), tag))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteArticleById(Long id) {
        // delete article
        removeById(id);

        // delete original tags
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        articleTagService.remove(queryWrapper);

        return ResponseResult.okResult();
    }
}




