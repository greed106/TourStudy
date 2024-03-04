package com.ymj.tourstudy.pojo;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;
    private String password;
    private String email;
    private LocalDate createdTime;
    private LocalDate lastLoginTime;

}
