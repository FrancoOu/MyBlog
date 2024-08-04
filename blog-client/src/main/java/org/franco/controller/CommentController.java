package org.franco.controller;


import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Comment;
import org.franco.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseResult getComments(Long articleId, Integer pageNum, Integer pageSize) {

        return commentService.getComments(SystemConstants.ARTICLE_COMMENT,articleId, pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/linkComments")
    public ResponseResult getLinkComment(Integer pageNum, Integer pageSize){

        return commentService.getComments(SystemConstants.FRIEND_LINK_COMMENT, null, pageNum, pageSize);
    }

}
