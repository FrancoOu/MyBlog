package org.franco.controller;



import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Article;
import org.franco.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}