package com.ymj.tourstudy.config;

import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.CrowdedGraph;
import com.ymj.tourstudy.pojo.JsonCrowdedGraph;
import com.ymj.tourstudy.utils.RedBlackTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class RedBlackTreeConfig {
    @Autowired
    GraphMapper graphMapper;
    @Bean
    public RedBlackTree<CrowdedGraph> graphRedBlackTree() {
        RedBlackTree<CrowdedGraph> redBlackTree = new RedBlackTree<>();
        try {
            List<JsonCrowdedGraph> crowdedGraphs = graphMapper.getAllCrowdedGraphs();
            if (crowdedGraphs == null || crowdedGraphs.isEmpty()) {
                log.error("No graphs found or failed to fetch from the database.");
                return redBlackTree;
            }
            for (JsonCrowdedGraph json : crowdedGraphs) {
                redBlackTree.insert(json.getName(), json.getCrowdedGraph());
            }
        } catch (Exception e) {
            log.error("Error initializing RedBlackTree", e);
        }
        return redBlackTree;
    }
}
