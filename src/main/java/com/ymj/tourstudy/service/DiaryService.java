package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.DTO.GetSortedDiaryRequest;
import com.ymj.tourstudy.pojo.DTO.UploadDiaryRequest;
import com.ymj.tourstudy.pojo.Diary;

import java.util.List;


public interface DiaryService {
    public void uploadDiary(UploadDiaryRequest req);
    public List<Diary> getDiary(String username, String titlePrefix);
    public void addPageViews(String username, String title);
    public List<Diary> getSortedDiary(GetSortedDiaryRequest req);
    public void addScore(String username, String title, Integer score);
}
