package com.ymj.tourstudy.pojo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer uuid;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdTime;
    private LocalDateTime lastLoginTime;
}
