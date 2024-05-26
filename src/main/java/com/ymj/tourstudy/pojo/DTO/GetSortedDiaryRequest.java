package com.ymj.tourstudy.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSortedDiaryRequest {
    private Boolean views;
    private Boolean score;
    public String getSortBy() {
        if(views) {
            return "views";
        } else if(score) {
            return "score";
        } else {
            return "createdTime";
        }
    }
}
