package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.CrowdedEdge;
import com.ymj.tourstudy.pojo.DTO.GetMapRequest;
import com.ymj.tourstudy.pojo.DTO.GetShortestPathRequest;
import com.ymj.tourstudy.pojo.Point;
import com.ymj.tourstudy.pojo.Result;
import com.ymj.tourstudy.pojo.TourMap;
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
}
