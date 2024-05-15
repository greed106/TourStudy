package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.ParseMapException;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.Graph;
import com.ymj.tourstudy.pojo.JsonGraph;
import com.ymj.tourstudy.pojo.TourMap;
import com.ymj.tourstudy.service.MapService;
import com.ymj.tourstudy.utils.AVLTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapServiceImpl implements MapService {
    @Autowired
    private GraphMapper graphMapper;
    @Autowired
    private AVLTree<JsonGraph> graphTree;
    @Override
    public TourMap getMap(String name) {
        try{
            JsonGraph jsonGraph = graphTree.search(name);
            return jsonGraph.getMap();
        } catch (Exception e){
            e.printStackTrace();
        }
        throw new ParseMapException("Failed to parse map");
    }
}
