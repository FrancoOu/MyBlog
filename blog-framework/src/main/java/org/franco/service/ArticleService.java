package org.franco.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.ArticleDto;
import org.franco.domain.dto.ArticleListDto;
import org.franco.domain.entity.Article;

/**
* @author franco
* @description 针对表【article(文章表)】的数据库操作Service
* @createDate 2024-07-19 16:27:05
*/
public interface ArticleService extends IService<Article> {

    ResponseResult getHotArticles();

    ResponseResult getArticles(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleById(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto articleDto);

    ResponseResult getPagedArticles(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getArticleByIdForEdit(Long id);

    ResponseResult updateArticle(ArticleDto articleDto);

    ResponseResult deleteArticleById(Long id);
}
