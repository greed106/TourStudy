package com.ymj.tourstudy.config;

import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.JsonGraph;
import com.ymj.tourstudy.utils.AVLTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class AVLTreeConfig {
    @Autowired
    GraphMapper graphMapper;
    @Bean
    public AVLTree<JsonGraph> graphAVLTree() {
        AVLTree<JsonGraph> avlTree = new AVLTree<>();
        try {
            List<JsonGraph> jsonGraphs = graphMapper.getAllGraphs();
            if (jsonGraphs == null || jsonGraphs.isEmpty()) {
                log.error("No graphs found or failed to fetch from the database.");
                return avlTree;
            }
            for (JsonGraph jsonGraph : jsonGraphs) {
                avlTree.insert(jsonGraph.getName(), jsonGraph);
            }
        } catch (Exception e) {
            log.error("Error initializing AVLTree", e);
        }
        return avlTree;
    }
}
