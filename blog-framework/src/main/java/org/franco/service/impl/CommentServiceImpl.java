package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Comment;
import org.franco.domain.vo.CommentVo;
import org.franco.domain.vo.PageVo;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.CommentService;
import org.franco.mapper.CommentMapper;
import org.franco.service.UserService;
import org.franco.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author franco
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2024-07-29 18:46:25
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult getComments(String type, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();


        lambdaQueryWrapper
                // filter comment type
                .eq(Comment::getType, type)
                // Get comments according to article id
                .eq(SystemConstants.ARTICLE_COMMENT.equals(type), Comment::getArticleId, articleId)
                // filter root comments
                .eq(Comment::getRootId, SystemConstants.ROOT_COMMENT)
                .orderByDesc(Comment::getCreateTime);



        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        List<CommentVo> commentVoList = convertCommentVo(page.getRecords());

        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> childrenComments = getChildren(commentVo.getId());
            commentVo.setChildren(childrenComments);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {

        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_CONTENT_ERROR);
        }
        save(comment);

        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
                .eq(Comment::getRootId, id)
                .orderByDesc(Comment::getCreateTime);

        List<Comment> comments = list(queryWrapper);

        return convertCommentVo(comments);

    }

    // add usernames to original vo
    private List<CommentVo> convertCommentVo(List<Comment> list) {
        List<CommentVo> commentVo = BeanCopyUtils.copyBeanList(list, CommentVo.class);

        commentVo.stream()
                .map(comment -> {
                    comment.setUserName(userService.getById(comment.getCreatedBy()).getNickName());
                    comment.setAvatar(userService.getById(comment.getCreatedBy()).getAvatar());
                    if (comment.getToCommentUserId() != -1L) {
                        comment.setToCommentUserName(userService.getById(comment.getToCommentUserId()).getNickName());
                    }
                    return comment;
                })
                .collect(Collectors.toList());

        return commentVo;

    }

}




