package org.franco.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAdminVo {
    private Long id;

    private String title;

    private String summary;

    private String thumbnail;

    private String content;

    private Long viewCount;

    private Long categoryId;

    private Date createTime;

    private String isComment;

    private String status;

    private String isTop;
}
