package org.franco.controller;

import io.swagger.annotations.Api;
import org.franco.domain.ResponseResult;
import org.franco.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@Api(tags = "Link", description = "Interfaces for Link")

public class LinkController {

    @Autowired
    private LinkService linkService;
    @GetMapping
    public ResponseResult getLinks() {

        return linkService.getLinks();
    }
}
