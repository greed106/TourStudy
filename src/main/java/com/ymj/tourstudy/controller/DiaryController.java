package com.ymj.tourstudy.controller;


import com.ymj.tourstudy.pojo.DTO.*;
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

    @PostMapping("/tour/diary/get_diary")
    public Result getDiary(@RequestBody GetDiaryRequest request){
        String username = request.getUsername();
        String titlePrefix = request.getTitle();
        log.info("获取日记请求："+username+" "+titlePrefix);

        List<Diary> diaries = diaryService.getDiaryByPrefix(username, titlePrefix);

        return Result.success(diaries);
    }

    @PostMapping("/tour/diary/add_page_views")
    public Result addPageViews(@RequestBody AddDiaryPageViewsRequest request){
        String username = request.getUsername();
        String title = request.getTitle();
        log.info("增加日记浏览量请求："+username+" "+title);

        diaryService.addPageViews(username, title);

        return Result.success();
    }

    @PostMapping("/tour/diary/get_sorted_diary")
    public Result getSortedDiary(@RequestBody GetSortedDiaryRequest request){
        log.info("获取排序后的日记请求："+request);

        List<Diary> diaries = diaryService.getSortedDiary(request);

        return Result.success(diaries);
    }

    @PostMapping("/tour/diary/add_score")
    public Result addScore(@RequestBody AddDiaryScoreRequest request){
        String username = request.getUsername();
        String title = request.getTitle();
        Integer score = request.getScore();
        log.info("增加日记评分请求："+username+" "+title+" "+score);

        diaryService.addScore(username, title, score);

        return Result.success();
    }

    @PostMapping("/tour/diary/get_matched_diary")
    public Result getMatchDiary(@RequestBody GetMatchDiaryRequest request){
        log.info("获取匹配日记请求："+request);

        List<Diary> diaries = diaryService.getMatchedDiary(request);

        return Result.success(diaries);
    }
}
