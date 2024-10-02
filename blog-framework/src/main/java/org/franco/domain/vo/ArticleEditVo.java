package org.franco.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleEditVo {

    private Long id;

    private Long categoryId;

    private String content;

    private String createdBy;

    private Date createTime;

    private String delFlag;

    private String isComment;

    private String isTOp;

    private String status;

    private String summary;

    private List<Long> tags;

    private String thumbnail;

    private String title;

    private Long updatedBy;

    private Date updateTime;

    private Long viewCount;


}
