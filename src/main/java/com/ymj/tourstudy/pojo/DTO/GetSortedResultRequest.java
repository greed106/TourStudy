package com.ymj.tourstudy.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSortedResultRequest {
    //是否根据浏览量排序返回结果
    private boolean views;
    //是否根据评分排序返回结果
    private boolean score;
    //返回结果的最大个数
    private int length;
    //关键词指用户在搜索框中输入的内容
    private List<String> keywords;
    //用户选择的一些标签
    private List<String> tags;
}
