package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.ParseMapException;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.Graph;
import com.ymj.tourstudy.pojo.JsonGraph;
import com.ymj.tourstudy.pojo.TourMap;
import com.ymj.tourstudy.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapServiceImpl implements MapService {
    @Autowired
    private GraphMapper graphMapper;
    @Override
    public TourMap getMap(String name) {
        JsonGraph jsonGraph = graphMapper.getGraphByName(name);
        try{
            return jsonGraph.getMap();
        } catch (Exception e){
            e.printStackTrace();
        }
        throw new ParseMapException("Failed to parse map");
    }
}
