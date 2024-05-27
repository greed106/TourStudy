package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrowdedEdge {
    private Integer destination;
    private Double crowdedness;
}
