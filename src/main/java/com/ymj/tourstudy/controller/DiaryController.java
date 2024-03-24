package com.ymj.tourstudy.controller;


import com.ymj.tourstudy.pojo.DTO.UploadDiaryRequest;
import com.ymj.tourstudy.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@Slf4j
public class DiaryController {



    @PostMapping("/tour/diary/upload")
    public Result uploadDiary(@RequestBody UploadDiaryRequest request){

        log.info("上传日记请求："+request);

        return Result.success();
    }
}
