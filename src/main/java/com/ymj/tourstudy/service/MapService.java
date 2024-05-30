package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.CrowdedEdge;
import com.ymj.tourstudy.pojo.DTO.AddTourismScoreRequest;
import com.ymj.tourstudy.pojo.DTO.GetNearestPointsRequest;
import com.ymj.tourstudy.pojo.DTO.GetSortedResultRequest;
import com.ymj.tourstudy.pojo.Point;
import com.ymj.tourstudy.pojo.TourMap;
import com.ymj.tourstudy.pojo.Tourism;

import java.util.List;


public interface MapService {
     TourMap getMap(String name);
     List<Point> getShortestPath(String name, Integer[] indexes);
     List<List<CrowdedEdge>> getCrowdedEdges(String name);
     List<Point> getCrowdedShortestPath(String name, Integer[] indexes);
     List<Point> getNearestPoints(GetNearestPointsRequest req);
     List<String> getMapNames();
     List<Tourism> getAllTourism();
     Tourism getTourismByName(String name);
     void addScore(AddTourismScoreRequest req);
     void addPageViews(String name);
     List<Tourism> getSortedTourism(GetSortedResultRequest req);
}
