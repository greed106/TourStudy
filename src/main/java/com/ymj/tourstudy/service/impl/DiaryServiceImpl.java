package com.ymj.tourstudy.service.impl;

import com.alibaba.fastjson.JSON;
import com.ymj.tourstudy.exception.NotFoundException;
import com.ymj.tourstudy.mapper.DiaryMapper;
import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.mapper.UserMapper;
import com.ymj.tourstudy.pojo.CompressedDiary;
import com.ymj.tourstudy.pojo.DTO.GetMatchDiaryRequest;
import com.ymj.tourstudy.pojo.DTO.GetSortedDiaryRequest;
import com.ymj.tourstudy.pojo.DTO.UploadDiaryRequest;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.service.DiaryService;
import com.ymj.tourstudy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DiaryServiceImpl implements DiaryService {
    @Autowired
    private TrieTree<Diary> trieTree;
    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private TagMapper tagMapper;
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
            tagMapper.insertDiaryTag(tag, key);
        }
    }

    @Override
    public List<Diary> getDiary(String username, String titlePrefix) {
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
    public List<Diary> getSortedDiary(GetSortedDiaryRequest req){
        List<String> keys = userMapper.getAllUsernames();
        List<Diary> diaries = new ArrayList<>();
        for(String username : keys){
            List<Diary> userDiaries = getDiary(username, "");
            diaries.addAll(userDiaries);
        }
        Diary[] diaryArray = diaries.toArray(new Diary[0]);
        // 进行排序
        SortUtils.quickSort(diaryArray, (d1, d2) -> {
            if(req.getSortBy().equals("views")){
                return d2.getPageViews() - d1.getPageViews();
            }else if(req.getSortBy().equals("score")){
                return (int)(d2.getScore() - d1.getScore());
            }else{
                return d2.getCreatedTime().compareTo(d1.getCreatedTime());
            }
        });
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
    public List<Diary> getMatchedDiary(GetMatchDiaryRequest req) {
        List<String> keys = userMapper.getAllUsernames();
        List<Diary> diaries = new ArrayList<>();
        for(String username : keys){
            List<Diary> userDiaries = getDiary(username, "");
            for(Diary diary : userDiaries){
                int patternSize = req.getKeyword().size();
                if(patternSize == 1 && MatchUtils.bmMatch(req.getKeyword().get(0), diary.getContent()) != -1){
                    diaries.add(diary);
                }else if(patternSize > 1 && MatchUtils.acAutomatonMatch(req.getKeyword(), diary.getContent())){
                    diaries.add(diary);
                }
            }
        }
        return diaries;
    }
}
