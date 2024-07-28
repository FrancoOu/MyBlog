package org.franco.controller;



import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Article;
import org.franco.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticles")
    public ResponseResult<List<Article>> getHotArticles() {
        return articleService.getHotArticles();
    }

    @GetMapping("")
    public ResponseResult getArticles(Integer pageNum,Integer pageSize, Long categoryId) {

        return articleService.getArticles(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }
}