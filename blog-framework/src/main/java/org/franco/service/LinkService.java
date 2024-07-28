package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author franco
* @description 针对表【link(友链)】的数据库操作Service
* @createDate 2024-07-24 14:39:48
*/
public interface LinkService extends IService<Link> {

    ResponseResult getLinks();
}
