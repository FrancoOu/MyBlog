package org.franco.controller;


import org.franco.domain.ResponseResult;
import org.franco.domain.dto.ArticleDto;
import org.franco.domain.dto.ArticleListDto;
import org.franco.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @GetMapping
    public ResponseResult getArticles(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.getPagedArticles(pageNum, pageSize, articleListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id){
        return articleService.getArticleByIdForEdit(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable Long id){
        return articleService.deleteArticleById(id);
    }
}
