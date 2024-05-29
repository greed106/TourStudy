package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

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
    static public Comparator<Tourism> getComparator(boolean isSortedByViews, boolean isSortedByScore) {
        if (isSortedByScore && isSortedByViews) {
            // 进行综合的排序，从低到高
            return Comparator.comparing(Tourism::getViews).thenComparing(Tourism::getScore);
        } else if (isSortedByViews) {
            // 根据浏览量排序，从低到高
            return Comparator.comparing(Tourism::getViews);
        } else if (isSortedByScore) {
            // 根据评分排序，从低到高
            return Comparator.comparing(Tourism::getScore);
        } else {
            // 根据字典序排序，从高到低
            return Comparator.comparing(Tourism::getName).reversed();
        }
    }
}
