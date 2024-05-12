package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diary {
    //外键
    private String username;
    private String title;
    private String content;
    private LocalDate createdTime;
    private LocalDate updatedTime;
    //浏览量
    private Integer pageViews;
    //评分人数
    private Integer ratings;
    //评分
    private Double score;
}
