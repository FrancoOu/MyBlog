package org.franco.controller;


import org.franco.domain.ResponseResult;
import org.franco.domain.dto.TagListDto;
import org.franco.domain.entity.Tag;
import org.franco.enums.AppHttpCodeEnum;
import org.franco.exception.SystemException;
import org.franco.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")

public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseResult getTags(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize) && ObjectUtils.isEmpty(tagListDto.getName())){
            return ResponseResult.okResult(tagService.list());
        }else {
            return tagService.getTagsPage(pageNum, pageSize, tagListDto);
        }
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTagById(@PathVariable Long id){
        return tagService.deleteById(id);
    }

    @PutMapping("")
    public ResponseResult updateById(@RequestBody Tag tag){
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.EMPTY_TAG_ERROR);
        }
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

}
