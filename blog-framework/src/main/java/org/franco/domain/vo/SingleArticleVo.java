package org.franco.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleArticleVo {
    private Long id;

    private String title;

    private String summary;

    private String thumbnail;

    private String content;

    private Long viewCount;

    private long categoryId;

    private String categoryName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+10")
    private Date createTime;
}
