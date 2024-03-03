package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/tour/login")
    public Result login() {
        return Result.success("登录成功");
    }
}
