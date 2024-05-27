package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.CrowdedEdge;
import com.ymj.tourstudy.pojo.Point;
import com.ymj.tourstudy.pojo.TourMap;

import java.util.List;


public interface MapService {
     TourMap getMap(String name);
     List<Point> getShortestPath(String name, Integer[] indexes);
     List<List<CrowdedEdge>> getCrowdedEdges(String name);
     List<Point> getCrowdedShortestPath(String name, Integer[] indexes);
     List<Point> getNearestPoints(String name, Integer index, Integer length);
     List<String> getMapNames();
}
