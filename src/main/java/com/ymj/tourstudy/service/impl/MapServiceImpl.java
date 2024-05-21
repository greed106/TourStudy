package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.ParseMapException;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.Graph;
import com.ymj.tourstudy.pojo.JsonGraph;
import com.ymj.tourstudy.pojo.Point;
import com.ymj.tourstudy.pojo.TourMap;
import com.ymj.tourstudy.service.MapService;
import com.ymj.tourstudy.utils.AVLTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Point> getShortestPath(String name, Integer[] indexes) {
        try{
            JsonGraph jsonGraph = graphTree.search(name);
            Graph graph = jsonGraph.getGraph();
            List<Point> points = graph.getPointsByIndexes(indexes);
            if(points.size() < 2){
                throw new ParseMapException("Invalid indexes");
            } else if(points.size() == 2){
                return graph.dijkstra(points.get(0), points.get(1));
            } else {
                return graph.getShortestPathThroughPoints(points);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        throw new ParseMapException("Failed to parse map");
    }
}
