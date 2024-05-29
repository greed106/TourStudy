package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.DTO.GetSortedResultRequest;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.pojo.Tag;
import com.ymj.tourstudy.pojo.Tourism;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    List<Tag> getTagsByTourism(String name);
    List<Tag> getTagsByDiary(String username, String title);
    List<Diary> getDiaryByTag(String tagName);
    List<Tourism> getTourismByTag(String tagName);
    void updateDiaryTags(List<String> tagName, String username, String title);
    void updateTourismTags(List<String> tagName, String name);
    boolean isTagExist(String tagName);
    boolean isDiaryTagExist(String tagName, String username, String title);
    boolean isTourismTagExist(String tagName, String name);
    void insertDiaryTag(String tagName, String key);
}
