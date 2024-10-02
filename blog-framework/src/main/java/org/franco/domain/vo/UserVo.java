package org.franco.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private String avatar;

    private Date createTime;

    private String email;

    private Long id;

    private String nickName;

    private String phonenumber;

    private String sex;

    private String status;

    private Long updatedBy;

    private Date updateTime;

    private String userName;

}
