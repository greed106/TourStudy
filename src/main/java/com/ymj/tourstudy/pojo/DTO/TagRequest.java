package com.ymj.tourstudy.pojo.DTO;

import com.ymj.tourstudy.pojo.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagRequest {
    private String name;
    private String username;
    private String title;
    private String tourism;
    private List<String> tags;
}
