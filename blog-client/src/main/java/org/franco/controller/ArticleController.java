package org.franco.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Article;
import org.franco.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "Article", description = "Interfaces for Article")

public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticles")
    public ResponseResult<List<Article>> getHotArticles() {
        return articleService.getHotArticles();
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "Page Number"),
            @ApiImplicitParam(name = "pageSize", value = "Number of Items in a single Page"),
            @ApiImplicitParam(name = "categoryId", value = " Category ID")
    }
    )
    public ResponseResult getArticles(Integer pageNum, Integer pageSize, Long categoryId) {

        return articleService.getArticles(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PutMapping("/viewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }
}