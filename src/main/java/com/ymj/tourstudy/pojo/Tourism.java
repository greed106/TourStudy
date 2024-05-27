package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tourism {
    //旅游景点的名称
    private String name;
    //拓扑图的名称
    private String graph;
    //景点的描述
    private String description;
    //浏览量
    private Integer views;
    //评分人数
    private Integer ratings;
    //评分
    private Double score;
}
