package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.constants.SystemConstants;
import org.franco.domain.ResponseResult;
import org.franco.domain.entity.Link;
import org.franco.domain.vo.LinkVo;
import org.franco.service.LinkService;
import org.franco.mapper.LinkMapper;
import org.franco.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author franco
* @description 针对表【link(友链)】的数据库操作Service实现
* @createDate 2024-07-24 14:39:48
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{

    @Override
    public ResponseResult getLinks() {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // filter based on the status of the link ('0' for approved)
        linkLambdaQueryWrapper
                .select(Link::getId, Link::getName, Link::getAddress, Link::getDescription, Link::getLogo)
                .eq(Link::getStatus, SystemConstants.LINK_STATUS_APPROVED);

        List<LinkVo> linkVoList = BeanCopyUtils.copyBeanList(this.list(linkLambdaQueryWrapper), LinkVo.class);

        return ResponseResult.okResult(linkVoList);
    }
}




