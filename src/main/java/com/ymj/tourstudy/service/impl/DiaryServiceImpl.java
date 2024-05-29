package com.ymj.tourstudy.service.impl;

import com.alibaba.fastjson.JSON;
import com.ymj.tourstudy.exception.NotFoundException;
import com.ymj.tourstudy.mapper.DiaryMapper;
import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.mapper.UserMapper;
import com.ymj.tourstudy.pojo.CompressedDiary;
import com.ymj.tourstudy.pojo.DTO.GetMatchDiaryRequest;
import com.ymj.tourstudy.pojo.DTO.GetSortedDiaryRequest;
import com.ymj.tourstudy.pojo.DTO.GetSortedResultRequest;
import com.ymj.tourstudy.pojo.DTO.UploadDiaryRequest;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.service.DiaryService;
import com.ymj.tourstudy.service.TagService;
import com.ymj.tourstudy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class DiaryServiceImpl implements DiaryService {
    @Autowired
    private TrieTree<Diary> trieTree;
    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserMapper userMapper;

    private Diary createDiary(UploadDiaryRequest req){
        String username = req.getUsername();
        String title = req.getTitle();
        String content = req.getContent();

        LocalDate createdTime = LocalDate.now();
        LocalDate updatedTime = LocalDate.now();

        return new Diary(username, title, content,
                createdTime, updatedTime, 0, 0, 0.0);
    }
    @Transactional
    @Override
    public void uploadDiary(UploadDiaryRequest req) {
        // 创建Diary对象
        Diary diary = createDiary(req);
        // 将Diary对象插入TrieTree
        String key = diary.getUsername() + "@" + diary.getTitle();
        trieTree.insert(key, diary);

        // 将Diary对象压缩后插入数据库
        String compressedContent = JSON.toJSONString(
            HuffmanUtils.encode(JSON.toJSONString(diary))
        );
        CompressedDiary compressedDiary = new CompressedDiary(
            diary.getUsername(),
            diary.getTitle(), compressedContent
        );
        // 如果数据库中已经存在重名的日记，则将其更新
        if(diaryMapper.getCompressedDiary(diary.getUsername(), diary.getTitle()) != null){
            diaryMapper.updateCompressedDiary(diary.getUsername(), diary.getTitle(), compressedContent);
        }else{
            diaryMapper.setCompressedDiary(compressedDiary);
        }

        String[] tags = req.getTags();
        if(tags == null || tags.length == 0){
            return;
        }
        for(String tag : tags){
            tagService.insertDiaryTag(tag, key);
        }
    }

    @Override
    public List<Diary> getDiaryByPrefix(String username, String titlePrefix) {
        String prefix = username + "@" + titlePrefix;
        List<String> keys = trieTree.keysWithPrefix(prefix);
        List<Diary> diaries = new ArrayList<>();
        // 遍历keys，获取对应的Diary对象
        for(String key : keys){
            diaries.add(trieTree.search(key));
        }
        return diaries;
    }

    @Override
    public Diary getDiaryByTitle(String username, String title) {
        String key = username + "@" + title;
        Diary diary = trieTree.search(key);
        if(diary == null){
            throw new NotFoundException("Diary not found");
        }
        return diary;
    }

    @Override
    public void addPageViews(String username, String title) {
        String key = username + "@" + title;
        Diary diary = trieTree.search(key);
        if(diary == null){
            throw new NotFoundException("Diary not found");
        }
        diary.setPageViews(diary.getPageViews() + 1);
        trieTree.insert(key, diary);
        String compressedContent = JSON.toJSONString(
            HuffmanUtils.encode(JSON.toJSONString(diary))
        );

        diaryMapper.updateCompressedDiary(username, title, compressedContent);
    }

    @Override
    public List<Diary> getSortedDiary(GetSortedResultRequest req){
        List<String> tags = req.getTags();
        MySet<Diary> diarySet = new MySet<>();
        if(tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                List<Diary> tempDiaries = tagService.getDiaryByTag(tag);
                for (Diary diary : tempDiaries) {
                    diarySet.add(diary.getUsername() + "@" + diary.getTitle(), diary);
                }
            }
        }else{
            for(Diary diary : getAllDiaries()) {
                diarySet.add(diary.getUsername() + "@" + diary.getTitle(), diary);
            }
        }

        List<String> keywords = req.getKeywords();
        List<Diary> diaryList = new ArrayList<>();
        if(keywords == null || keywords.isEmpty()) {
            diaryList.addAll(diarySet.values());
        }else{
            for(Diary diary : diarySet.values()) {
                String title = diary.getTitle();
                String content = diary.getContent();
                boolean isTitleMatch = false;
                boolean isContentMatch = MatchUtils.acAutomatonMatch(keywords, content);
                for(String keyword : keywords){
                    if(MatchUtils.bmMatch(keyword, title) != -1){
                        isTitleMatch = true;
                        break;
                    }
                }
                if(isTitleMatch || isContentMatch){
                    diaryList.add(diary);
                }
            }
        }

        Diary[] diaryArray = diaryList.toArray(new Diary[0]);
        Comparator<Diary> comparator = Diary.getComparator(req.isViews(), req.isScore());
        if(req.getLength() <= 0){
            SortUtils.quickSort(diaryArray, comparator.reversed());
        }else{
            diaryArray = SortUtils.getLastN(diaryArray, req.getLength(), comparator);
            SortUtils.reverse(diaryArray);
        }

        return Arrays.asList(diaryArray);
    }

    @Override
    public void addScore(String username, String title, Integer score) {
        String key = username + "@" + title;
        Diary diary = trieTree.search(key);
        if(diary == null){
            throw new NotFoundException("Diary not found");
        }
        Integer ratings = diary.getRatings();
        Double baseScore = diary.getScore() * ratings;
        diary.setRatings(ratings + 1);
        diary.setScore((baseScore + score) / diary.getRatings());

        trieTree.insert(key, diary);
        String compressedContent = JSON.toJSONString(
            HuffmanUtils.encode(JSON.toJSONString(diary))
        );

        diaryMapper.updateCompressedDiary(username, title, compressedContent);
    }

    @Override
    public List<Diary> getAllDiaries() {
        List<String> usernames = userMapper.getAllUsernames();
        List<Diary> diaries = new ArrayList<>();
        for(String username : usernames){
            List<Diary> tempDiaries = getDiaryByPrefix(username, "");
            diaries.addAll(tempDiaries);
        }
        return diaries;
    }

}
