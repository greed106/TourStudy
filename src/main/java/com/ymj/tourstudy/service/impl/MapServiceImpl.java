package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.NotFoundException;
import com.ymj.tourstudy.exception.ParseMapException;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.mapper.TagMapper;
import com.ymj.tourstudy.pojo.*;
import com.ymj.tourstudy.pojo.DTO.AddTourismScoreRequest;
import com.ymj.tourstudy.pojo.DTO.GetNearestPointsRequest;
import com.ymj.tourstudy.pojo.DTO.GetSortedResultRequest;
import com.ymj.tourstudy.service.MapService;
import com.ymj.tourstudy.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
    @Autowired
    private TagMapper tagMapper;
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
    public List<Point> getNearestPoints(GetNearestPointsRequest req) {
        String name = req.getName();
        int index = req.getIndex();
        int length = req.getLength();
        List<String> keywords = req.getKeywords();
        try{
            JsonGraph jsonGraph = graphTree.search(name);
            Graph graph = jsonGraph.getGraph();
            List<Point> filteredPoints = new ArrayList<>();
            if(keywords != null && !keywords.isEmpty()){
                for(Point point : graph.getNearestPoints(index, length)){
                    if(MatchUtils.acAutomatonMatch(keywords, point.getName())){
                        filteredPoints.add(point);
                    }
                }
                return filteredPoints;
            }else{
                return graph.getNearestPoints(index, length);
            }
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
    @Override
    public List<Tourism> getSortedTourism(GetSortedResultRequest req) {
        List<String> tags = req.getTags();
        MySet<Tourism> tourismSet = new MySet<>();
        if(tags != null && !tags.isEmpty()) {
            for(String tag : tags){
                List<String> tourismNames = tagMapper.getTourismNameByTag(tag);
                List<Tourism> tempTourisms = new ArrayList<>();
                for(String tourismName : tourismNames) {
                    Tourism tourism = getTourismByName(tourismName);
                    tempTourisms.add(tourism);
                }
                for(Tourism tourism : tempTourisms) {
                    tourismSet.add(tourism.getName(), tourism);
                }
            }
        }else{
            for(Tourism tourism : getAllTourism()) {
                tourismSet.add(tourism.getName(), tourism);
            }
        }

        List<String> keywords = req.getKeywords();
        List<Tourism> tourismList = new ArrayList<>();
        if(keywords == null || keywords.isEmpty()) {
            tourismList.addAll(tourismSet.values());
        }else{
            for(Tourism tourism : tourismSet.values()) {
                String name = tourism.getName();
                String description = tourism.getDescription();
                boolean isNameMatch = false;
                boolean isDescriptionMatch = MatchUtils.acAutomatonMatch(keywords, description);
                for(String keyword : keywords) {
                    if(MatchUtils.kmpMatch(keyword, name) != -1){
                        isNameMatch = true;
                        break;
                    }
                }
                if(isNameMatch || isDescriptionMatch) {
                    tourismList.add(tourism);
                }
            }
        }

        Tourism[] tourismArray = tourismList.toArray(new Tourism[0]);
        Comparator<Tourism> comparator = Tourism.getComparator(req.isViews(), req.isScore());
        if(req.getLength() <= 0) {
            SortUtils.quickSort(tourismArray, comparator.reversed());
        }else{
            tourismArray = SortUtils.getLastN(tourismArray, req.getLength(), comparator);
            SortUtils.reverse(tourismArray);
        }
        return Arrays.asList(tourismArray);
    }
}
