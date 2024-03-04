package com.ymj.tourstudy.controller;

import com.ymj.tourstudy.pojo.Result;
import com.ymj.tourstudy.pojo.User;
import com.ymj.tourstudy.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;
    @PostMapping("/tour/login")
    public Result login(@RequestBody User user) {
        log.info("用户{}请求登录",user.getUsername());
        User valid = loginService.validLogin(user.getUsername(), user.getPassword());
        String jwt = loginService.getJwt(user.getUsername());
        log.info("用户{}登录成功，下发jwt",user.getUsername());
        return Result.success(jwt);
    }

}
