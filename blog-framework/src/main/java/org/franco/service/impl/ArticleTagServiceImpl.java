package org.franco.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.entity.ArticleTag;
import org.franco.service.ArticleTagService;
import org.franco.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author franco
* @description 针对表【article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2024-09-29 11:59:53
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




