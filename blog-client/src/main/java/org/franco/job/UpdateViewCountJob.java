package org.franco.job;

import io.swagger.models.auth.In;
import org.franco.constants.SystemConstants;
import org.franco.domain.entity.Article;
import org.franco.service.ArticleService;
import org.franco.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    /**
     * Update view count every 10 minutes
     */
    @Scheduled(cron = "* 0/10 * * * ?")
    public void updateViewCount(){
        // Get the view count from redis
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_MAP_KEY);

        // Convert view counts in redis to Article lists so that we can do batch update
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> {
                    Article article = new Article();
                    article.setId(Long.valueOf(entry.getKey()));
                    article.setViewCount(entry.getValue().longValue());
                    return article;
                })
                .collect(Collectors.toList());

        // Update the data in database
        articleService.updateBatchById(articles);
    }
}
