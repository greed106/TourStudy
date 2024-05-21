package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.Point;
import com.ymj.tourstudy.pojo.TourMap;

import java.util.List;


public interface MapService {
     TourMap getMap(String name);
     List<Point> getShortestPath(String name, Integer[] indexes);
}
