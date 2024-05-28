package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.*;
import com.ymj.tourstudy.pojo.DTO.*;
import com.ymj.tourstudy.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@Slf4j
public class MapController {

    @Autowired
    private MapService mapService;
    @PostMapping("/tour/map/get_map")
    public Result getMap(@RequestBody GetMapRequest request) {
        log.info("请求获取地图: {}", request.getName());
        TourMap map = mapService.getMap(request.getName());
        return Result.success(map);
    }

    @PostMapping("/tour/map/get_shortest_path")
    public Result getShortestPath(@RequestBody GetShortestPathRequest request) {
        log.info("请求获取最短路径: {}", request);
        List<Point> path = mapService.getShortestPath(request.getName(), request.getIndexes());
        return Result.success(path);
    }

    @PostMapping("/tour/map/get_crowded_shortest_path")
    public Result getCrowdedShortestPath(@RequestBody GetShortestPathRequest request) {
        log.info("请求获取最短路径: {}", request);
        List<Point> path = mapService.getCrowdedShortestPath(request.getName(), request.getIndexes());
        return Result.success(path);
    }

    @PostMapping("/tour/map/get_crowded_edges")
    public Result getCrowdedEdges(@RequestBody GetMapRequest request) {
        log.info("请求获取拥挤边: {}", request.getName());
        List<List<CrowdedEdge>> crowdedEdges = mapService.getCrowdedEdges(request.getName());
        return Result.success(crowdedEdges);
    }

    @PostMapping("/tour/map/get_nearest_points")
    public Result getNearestPoints(@RequestBody GetNearestPointsRequest req) {
        log.info("请求获取最近点: {}", req.getName());
        List<Point> nearestPoints = mapService.getNearestPoints(req.getName(), req.getIndex(), req.getLength());
        return Result.success(nearestPoints);
    }

    @PostMapping("/tour/map/get_map_names")
    public Result getMapNames() {
        log.info("请求获取所有地图名称");
        List<String> mapNames = mapService.getMapNames();
        return Result.success(mapNames);
    }

    @PostMapping("/tour/map/get_all_tourism")
    public Result getAllTourism() {
        log.info("请求获取所有旅游景点");
        List<Tourism> tourism = mapService.getAllTourism();
        return Result.success(tourism);
    }

    @PostMapping("/tour/map/get_tourism")
    public Result getTourismByName(@RequestBody GetMapRequest request) {
        log.info("请求获取旅游景点: {}", request.getName());
        Tourism tourism = mapService.getTourismByName(request.getName());
        return Result.success(tourism);
    }

    @PostMapping("/tour/map/add_score")
    public Result addScore(@RequestBody AddTourismScoreRequest req) {
        log.info("请求添加评分: {}", req);
        mapService.addScore(req);
        return Result.success();
    }

    @PostMapping("/tour/map/add_page_views")
    public Result addPageViews(@RequestBody AddTourismPageViewsRequest request) {
        log.info("请求添加浏览量: {}", request.getName());
        mapService.addPageViews(request.getName());
        return Result.success();
    }
}
