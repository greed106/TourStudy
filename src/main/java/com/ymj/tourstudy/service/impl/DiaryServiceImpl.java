package com.ymj.tourstudy.service.impl;

import com.alibaba.fastjson.JSON;
import com.ymj.tourstudy.mapper.DiaryMapper;
import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.pojo.CompressedDiary;
import com.ymj.tourstudy.pojo.DTO.UploadDiaryRequest;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.service.DiaryService;
import com.ymj.tourstudy.utils.HuffmanUtils;
import com.ymj.tourstudy.utils.TrieTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
        // 如果数据库中已经存在该日记，则更新
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
            tagMapper.setDiaryTag(tag, key);
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
}
