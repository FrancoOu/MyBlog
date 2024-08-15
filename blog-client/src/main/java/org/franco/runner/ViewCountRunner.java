package org.franco.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.checkerframework.checker.units.qual.A;
import org.franco.constants.SystemConstants;
import org.franco.domain.entity.Article;
import org.franco.mapper.ArticleMapper;
import org.franco.service.ArticleService;
import org.franco.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Override
    // Set the view count in Redis on start
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.select(Article::getId, Article::getViewCount);


        Map<String, Integer> viewCountMap =  articleService.list(lambdaQueryWrapper).stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));


        redisCache.setCacheMap(SystemConstants.VIEW_COUNT_MAP_KEY, viewCountMap);
    }
}
