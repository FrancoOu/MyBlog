package org.franco.controller;

import org.franco.domain.ResponseResult;
import org.franco.domain.dto.LinkDto;
import org.franco.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    LinkService linkService;

    @GetMapping
    public ResponseResult getPagedLinks(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.getPagedLinks(pageNum, pageSize, name, status);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id){
        return ResponseResult.okResult(linkService.getById(id));
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody LinkDto linkDto){

        return linkService.addLink(linkDto);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable Long id){
        linkService.removeById(id);

        return ResponseResult.okResult();
    }
}
