package org.franco.service;

import org.franco.domain.ResponseResult;
import org.franco.domain.dto.TagListDto;
import org.franco.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author franco
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-08-15 14:14:58
*/
public interface TagService extends IService<Tag> {

    ResponseResult getTagsPage(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult getTagById(Long id);

    ResponseResult deleteById(Long id);
}
