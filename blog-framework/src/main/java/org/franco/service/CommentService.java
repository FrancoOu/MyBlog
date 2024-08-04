package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author franco
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2024-07-29 18:46:25
*/
public interface CommentService extends IService<Comment> {

    ResponseResult getComments(String type, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
