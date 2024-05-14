package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.DTO.GetMapRequest;
import com.ymj.tourstudy.pojo.Result;
import com.ymj.tourstudy.pojo.TourMap;
import com.ymj.tourstudy.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
