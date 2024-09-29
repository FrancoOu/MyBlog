package org.franco.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.franco.domain.ResponseResult;
import org.franco.domain.dto.TagListDto;
import org.franco.domain.entity.Tag;
import org.franco.domain.vo.PageVo;
import org.franco.domain.vo.TagVo;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.TagService;
import org.franco.mapper.TagMapper;
import org.franco.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author franco
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-08-15 14:14:58
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ResponseResult getTagsPage(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // set query
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        // set pagination
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        // put data in pageVo
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        System.out.println(tagListDto);
        if (!StringUtils.hasText(tagListDto.getName()) || !StringUtils.hasText(tagListDto.getRemark())) {
            throw new SystemException(AppHttpCodeEnum.EMPTY_TAG_ERROR);
        }
        Tag newTag = new Tag();
        newTag.setName(tagListDto.getName());
        newTag.setRemark(tagListDto.getRemark());
        save(newTag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Long id) {
        Tag tag = getById(id);

        TagVo tagVo = BeanCopyUtils.copyBean(tag,TagVo.class);

        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult deleteById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}




