package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.DTO.TagRequest;
import lombok.extern.slf4j.Slf4j;
import com.ymj.tourstudy.pojo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class TagController {
    @PostMapping("/tour/tag/diary/set")
    public Result setDiaryTag(@RequestBody TagRequest request) {
        log.info("设置日记标签请求：" + request);
        // TODO: 设置日记标签
        return Result.success();
    }


}
