package com.ymj.tourstudy.config;

import com.alibaba.fastjson.JSON;
import com.ymj.tourstudy.mapper.DiaryMapper;
import com.ymj.tourstudy.pojo.CompressedDiary;
import com.ymj.tourstudy.pojo.Diary;
import com.ymj.tourstudy.utils.HuffmanResult;
import com.ymj.tourstudy.utils.HuffmanUtils;
import com.ymj.tourstudy.utils.TrieTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class TrieTreeConfig {
    @Autowired
    DiaryMapper diaryMapper;

    @Bean
    public TrieTree<Diary> diaryTrieTree() {
        TrieTree<Diary> trieTree = new TrieTree<>();
        try {
            List<CompressedDiary> compressedDiaries = diaryMapper.getAllCompressedDiaries();
            if (compressedDiaries == null || compressedDiaries.isEmpty()) {
                log.error("No compressed diaries found or failed to fetch from the database.");
                return trieTree;
            }
            for (CompressedDiary compressedDiary : compressedDiaries) {
                String compressedContent = compressedDiary.getCompressedContent();
                if (compressedContent == null) {
                    log.warn("Encountered null compressed content.");
                    continue;
                }
                HuffmanResult result = JSON.parseObject(compressedContent, HuffmanResult.class);
                if (result == null) {
                    log.warn("Failed to parse HuffmanResult from content.");
                    continue;
                }
                String content = HuffmanUtils.decode(result);
                if (content == null) {
                    log.warn("Decoded content is null.");
                    continue;
                }
                Diary diary = JSON.parseObject(content, Diary.class);
                if (diary == null) {
                    log.warn("Failed to parse Diary from decoded content.");
                    continue;
                }
                String key = diary.getUsername() + "@" + diary.getTitle();
                trieTree.insert(key, diary);
            }
        } catch (Exception e) {
            log.error("Error initializing TrieTree", e);
        }
        return trieTree;
    }
}
