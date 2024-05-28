package com.ymj.tourstudy.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTourismScoreRequest {
    private String name;
    private Integer score;
}
