package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.NotFoundException;
import com.ymj.tourstudy.exception.ParseMapException;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.*;
import com.ymj.tourstudy.pojo.DTO.AddTourismScoreRequest;
import com.ymj.tourstudy.service.MapService;
import com.ymj.tourstudy.utils.AVLTree;
import com.ymj.tourstudy.utils.BinarySearchTree;
import com.ymj.tourstudy.utils.RedBlackTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapServiceImpl implements MapService {
    @Autowired
    private GraphMapper graphMapper;
    @Autowired
    private AVLTree<JsonGraph> graphTree;
    @Autowired
    private RedBlackTree<CrowdedGraph> crowdedGraphTree;
    @Autowired
    private BinarySearchTree<Tourism> tourismTree;
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
        throw new NotFoundException("Shortest path not found");
    }

    @Override
    public List<List<CrowdedEdge>> getCrowdedEdges(String name) {
        CrowdedGraph graph = crowdedGraphTree.search(name);
        if(graph == null){
            throw new NotFoundException("Graph not found");
        }
        return graph.getCrowdedEdges();
    }

    @Override
    public List<Point> getCrowdedShortestPath(String name, Integer[] indexes) {
        try{
            CrowdedGraph graph = crowdedGraphTree.search(name);
            List<Point> points = graph.getPointsByIndexes(indexes);
            if(points.size() < 2){
                throw new ParseMapException("Invalid indexes");
            } else if(points.size() == 2){
                return graph.dijkstra(points.get(0), points.get(1));
            } else {
                return graph.getShortestPathThroughPoints(points);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NotFoundException("Shortest path not found");
    }

    @Override
    public List<Point> getNearestPoints(String name, Integer index, Integer length) {
        try{
            JsonGraph jsonGraph = graphTree.search(name);
            Graph graph = jsonGraph.getGraph();
            return graph.getNearestPoints(index, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NotFoundException("Nearest points not found");
    }

    @Override
    public List<String> getMapNames() {
        return graphMapper.getGraphNames();
    }

    @Override
    public List<Tourism> getAllTourism() {
        return graphMapper.getAllTourism();
    }

    @Override
    public Tourism getTourismByName(String name) {
        return tourismTree.search(name);
    }

    @Override
    public void addScore(AddTourismScoreRequest req) {
        Tourism tourism = tourismTree.search(req.getName());
        if(tourism == null){
            throw new NotFoundException("Tourism not found");
        }
        double baseScore = tourism.getScore();
        int ratings = tourism.getRatings();
        double newScore = (baseScore * ratings + req.getScore()) / (ratings + 1);
        tourism.setScore(newScore);
        tourism.setRatings(ratings + 1);
        graphMapper.updateTourism(tourism);
    }

    @Override
    public void addPageViews(String name) {
        Tourism tourism = tourismTree.search(name);
        if(tourism == null){
            throw new NotFoundException("Tourism not found");
        }
        tourism.setViews(tourism.getViews() + 1);
        graphMapper.updateTourism(tourism);
    }
}
