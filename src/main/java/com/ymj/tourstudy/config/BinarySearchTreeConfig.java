package com.ymj.tourstudy.config;

import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.pojo.Tag;
import com.ymj.tourstudy.pojo.Tourism;
import com.ymj.tourstudy.utils.BinarySearchTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class BinarySearchTreeConfig {
    @Autowired
    GraphMapper graphMapper;
    @Bean
    public BinarySearchTree<Tourism> tourismBinarySearchTree() {
        BinarySearchTree<Tourism> binarySearchTree = new BinarySearchTree<>();
        try {
            List<Tourism> tourisms = graphMapper.getAllTourism();
            if (tourisms == null || tourisms.isEmpty()) {
                log.error("No tourism found or failed to fetch from the database.");
                return binarySearchTree;
            }
            for (Tourism tourism : tourisms) {
                binarySearchTree.insert(tourism.getName(), tourism);
            }
        } catch (Exception e) {
            log.error("Error initializing BinarySearchTree", e);
        }
        return binarySearchTree;
    }
}
