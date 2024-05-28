package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.pojo.Tag;
import com.ymj.tourstudy.pojo.Tourism;
import com.ymj.tourstudy.service.DiaryService;
import com.ymj.tourstudy.service.MapService;
import com.ymj.tourstudy.service.TagService;
import com.ymj.tourstudy.utils.MultiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private MapService mapService;
    @Autowired
    private MultiMap<String, Tag> diaryTagMultiMap;
    @Autowired
    private MultiMap<Tourism, Tag> tourismTagMultiMap;
    @Override
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagMapper.getAllTags()) {
            tags.add(new Tag(tagName));
        }
        return tags;
    }

    @Override
    public List<Tag> getTagsByTourism(String name) {
        return tourismTagMultiMap.get(mapService.getTourismByName(name));
    }

    @Override
    public List<Tag> getTagsByDiary(String username, String title) {
        return diaryTagMultiMap.get(username + "@" + title);
    }

    @Override
    public List<Diary> getDiaryByTag(String tagName) {
        List<String> diaryNames = tagMapper.getDiaryNameByTag(tagName);
        List<Diary> diaries = new ArrayList<>();
        for(String diaryName : diaryNames) {
            String[] diaryInfo = diaryName.split("@");
            Diary diary = diaryService.getDiaryByTitle(diaryInfo[0], diaryInfo[1]);
            diaries.add(diary);
        }
        return diaries;
    }

    @Override
    public List<Tourism> getTourismByTag(String tagName) {
        List<String> tourismNames = tagMapper.getTourismNameByTag(tagName);
        List<Tourism> tourisms = new ArrayList<>();
        for(String tourismName : tourismNames) {
            Tourism tourism = mapService.getTourismByName(tourismName);
            tourisms.add(tourism);
        }
        return tourisms;
    }

    @Override
    @Transactional
    public void updateDiaryTags(List<String> tagNames, String username, String title) {
        String diaryName = username + "@" + title;
        List<Tag> originalTags = new ArrayList<>(diaryTagMultiMap.get(diaryName));
        for(Tag tag : originalTags) {
            tagMapper.deleteDiaryTag(tag.getName(), diaryName);
        }
        for(String tagName : tagNames) {
            tagMapper.insertDiaryTag(tagName, diaryName);
        }
        // 移除原有MultiMap中的Tag，并替换为新的
        for(Tag tag : originalTags) {
            diaryTagMultiMap.remove(diaryName, tag);
        }
        for(String tagName : tagNames) {
            diaryTagMultiMap.put(diaryName, new Tag(tagName));
            if(!isTagExist(tagName)) {
                tagMapper.insertTag(tagName);
            }
        }
    }

    @Override
    public void updateTourismTags(List<String> tagName, String name) {
        List<Tag> originalTags = new ArrayList<>(tourismTagMultiMap.get(mapService.getTourismByName(name)));
        for(Tag tag : originalTags) {
            tagMapper.deleteTourismTag(tag.getName(), name);
        }
        for(String tag : tagName) {
            tagMapper.insertTourismTag(tag, name);
        }
        // 移除原有MultiMap中的Tag，并替换为新的
        for(Tag tag : originalTags) {
            tourismTagMultiMap.remove(mapService.getTourismByName(name), tag);
        }
        for(String tag : tagName) {
            tourismTagMultiMap.put(mapService.getTourismByName(name), new Tag(tag));
            if(!isTagExist(tag)) {
                tagMapper.insertTag(tag);
            }
        }
    }

    @Override
    public boolean isTagExist(String tagName) {
        return tagMapper.getAllTags().contains(tagName);
    }

    @Override
    public boolean isDiaryTagExist(String tagName, String username, String title) {
        return diaryTagMultiMap.get(username + "@" + title).contains(new Tag(tagName));
    }

    @Override
    public boolean isTourismTagExist(String tagName, String name) {
        return tagMapper.getTagsByTourismName(name).contains(tagName);
    }
}
