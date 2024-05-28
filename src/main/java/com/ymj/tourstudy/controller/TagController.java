package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.DTO.TagRequest;
import com.ymj.tourstudy.pojo.Tag;
import com.ymj.tourstudy.service.TagService;
import lombok.extern.slf4j.Slf4j;
import com.ymj.tourstudy.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@Slf4j
public class TagController {
    @Autowired
    private TagService tagService;
    @PostMapping("/tour/tag/get_all_tags")
    public Result getAllTags() {
        log.info("请求获取所有标签");
        return Result.success(tagService.getAllTags());
    }
    @PostMapping("/tour/tag/get_tags_by_tourism")
    public Result getTagsByTourism(@RequestBody TagRequest tagRequest) {
        log.info("请求获取旅游标签: {}", tagRequest);
        return Result.success(tagService.getTagsByTourism(tagRequest.getTourism()));
    }

    @PostMapping("/tour/tag/get_tags_by_diary")
    public Result getTagsByDiary(@RequestBody TagRequest tagRequest) {
        log.info("请求获取日记标签: {}", tagRequest);
        return Result.success(tagService.getTagsByDiary(tagRequest.getUsername(), tagRequest.getTitle()));
    }

    @PostMapping("/tour/tag/get_diary_by_tag")
    public Result getDiaryByTag(@RequestBody TagRequest tagRequest) {
        log.info("请求获取日记: {}", tagRequest);
        return Result.success(tagService.getDiaryByTag(tagRequest.getName()));
    }

    @PostMapping("/tour/tag/get_tourism_by_tag")
    public Result getTourismByTag(@RequestBody TagRequest tagRequest) {
        log.info("请求获取旅游景点: {}", tagRequest);
        return Result.success(tagService.getTourismByTag(tagRequest.getName()));
    }

    @PostMapping("/tour/tag/update_diary_tags")
    public Result updateDiaryTags(@RequestBody TagRequest tagRequest) {
        log.info("请求更新日记标签: {}", tagRequest);
        tagService.updateDiaryTags(tagRequest.getTags(), tagRequest.getUsername(), tagRequest.getTitle());
        return Result.success();
    }

    @PostMapping("/tour/tag/update_tourism_tags")
    public Result updateTourismTags(@RequestBody TagRequest tagRequest) {
        log.info("请求更新旅游标签: {}", tagRequest);
        tagService.updateTourismTags(tagRequest.getTags(), tagRequest.getTourism());
        return Result.success();
    }

}
