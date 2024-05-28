package com.ymj.tourstudy.config;

import com.ymj.tourstudy.mapper.DiaryMapper;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.pojo.CompressedDiary;
import com.ymj.tourstudy.pojo.Tag;
import com.ymj.tourstudy.pojo.Tourism;
import com.ymj.tourstudy.utils.MultiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class MultiMapConfig {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    DiaryMapper diaryMapper;
    @Autowired
    GraphMapper graphMapper;
    static int CAPACITY = 128;
    @Bean
    public MultiMap<String, Tag> diaryTagMultiMap() {
        MultiMap<String, Tag> multiMap = new MultiMap<>(CAPACITY);
        try {
            List<CompressedDiary> compressedDiaries = diaryMapper.getAllCompressedDiaries();
            if (compressedDiaries == null || compressedDiaries.isEmpty()) {
                log.error("No compressed diaries found or failed to fetch from the database.");
                return multiMap;
            }
            for (CompressedDiary compressedDiary : compressedDiaries) {
                List<Tag> tags = null;
                String diaryName = compressedDiary.getUsername() + "@" + compressedDiary.getTitle();
                for (String tagName : tagMapper.getTagsByDiaryName(diaryName)) {
                    multiMap.put(diaryName, new Tag(tagName));
                }
            }
        } catch (Exception e) {
            log.error("Error initializing MultiMap", e);
        }
        return multiMap;
    }
    @Bean
    public MultiMap<Tourism, Tag> tourismTagMultiMap() {
        MultiMap<Tourism, Tag> multiMap = new MultiMap<>(CAPACITY);
        try {
            List<Tourism> tourisms = graphMapper.getAllTourism();
            if (tourisms == null || tourisms.isEmpty()) {
                log.error("No tourisms found or failed to fetch from the database.");
                return multiMap;
            }
            for (Tourism tourism : tourisms) {
                List<Tag> tags = null;
                for (String tagName : tagMapper.getTagsByTourismName(tourism.getName())) {
                    multiMap.put(tourism, new Tag(tagName));
                }
            }
        } catch (Exception e) {
            log.error("Error initializing MultiMap", e);
        }
        return multiMap;
    }
}
