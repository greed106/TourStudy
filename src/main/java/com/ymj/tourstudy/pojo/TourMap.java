package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourMap {
    private String name;
    private List<Point> points;
    private String picture;
}
