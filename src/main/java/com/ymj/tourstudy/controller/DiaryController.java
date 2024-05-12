package com.ymj.tourstudy.controller;


import com.ymj.tourstudy.pojo.DTO.GetDiaryRequest;
import com.ymj.tourstudy.pojo.DTO.UploadDiaryRequest;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.pojo.Result;
import com.ymj.tourstudy.service.DiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@Slf4j
public class DiaryController {

    @Autowired
    private DiaryService diaryService;


    @PostMapping("/tour/diary/upload")
    public Result uploadDiary(@RequestBody UploadDiaryRequest request){

        log.info("上传日记请求："+request);

        diaryService.uploadDiary(request);

        return Result.success();
    }

    @PostMapping("/tour/diary/get")
    public Result getDiary(@RequestBody GetDiaryRequest request){
        String username = request.getUsername();
        String titlePrefix = request.getTitlePrefix();
        log.info("获取日记请求："+username+" "+titlePrefix);

        List<Diary> diaries = diaryService.getDiary(username, titlePrefix);

        return Result.success(diaries);
    }
}
