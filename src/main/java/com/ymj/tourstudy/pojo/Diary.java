package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;

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
    static public Comparator<Diary> getComparator(boolean isSortedByViews, boolean isSortedByScore) {
        if (isSortedByScore && isSortedByViews) {
            // 进行综合的排序，从低到高
            return Comparator.comparing(Diary::getPageViews).thenComparing(Diary::getScore);
        } else if (isSortedByViews) {
            // 根据浏览量排序，从低到高
            return Comparator.comparing(Diary::getPageViews);
        } else if (isSortedByScore) {
            // 根据评分排序，从低到高
            return Comparator.comparing(Diary::getScore);
        } else {
            // 根据字典序排序，从高到低
            return Comparator.comparing(Diary::getTitle).reversed();
        }
    }
}
