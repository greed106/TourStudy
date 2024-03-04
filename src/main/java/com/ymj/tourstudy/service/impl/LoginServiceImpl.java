package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.WrongUsernameOrPasswordException;
import com.ymj.tourstudy.mapper.UserMapper;
import com.ymj.tourstudy.pojo.User;
import com.ymj.tourstudy.service.LoginService;
import com.ymj.tourstudy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 验证登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回得到的实体user
     */
    @Override
    public User validLogin(String username, String password) {
        User valid = userMapper.getUserByUsernameAndPassword(username, password);
        if(valid == null){
            throw new WrongUsernameOrPasswordException("用户名或密码错误");
        }
        userMapper.updateLastLoginTime(username, LocalDate.now());
        valid.setLastLoginTime(LocalDate.now());
        return valid;
    }

    /**
     * 下放jwt
     *
     * @param username 用户名会含在jwt中，待验证时进行解析
     * @return 返回jwt
     */
    @Override
    public String getJwt(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        Long expire = 3*60*60*1000L; //三个小时
        return JwtUtils.generateJwt(claims, expire);
    }
}
