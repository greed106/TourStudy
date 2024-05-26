package com.ymj.tourstudy.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddScoreRequest {
    private String username;
    private String title;
    private Integer score;
}
