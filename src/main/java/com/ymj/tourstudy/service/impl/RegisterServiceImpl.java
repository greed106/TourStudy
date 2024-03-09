package com.ymj.tourstudy.service.impl;

import com.ymj.tourstudy.exception.DuplicateEmailException;
import com.ymj.tourstudy.exception.DuplicateUsernameException;
import com.ymj.tourstudy.exception.WrongCaptchaException;
import com.ymj.tourstudy.mapper.UserMapper;
import com.ymj.tourstudy.pojo.User;
import com.ymj.tourstudy.service.RegisterService;
import com.ymj.tourstudy.utils.EmailVerificationUtils;
import com.ymj.tourstudy.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 检测用户名是否存在
     *
     * @param username 用户名
     * @return 返回是否合法
     */
    @Override
    public boolean isValidUsername(String username) {
        User valid = userMapper.getUserByUsername(username);
        if(valid != null){
            throw new DuplicateUsernameException("This username has been registered");
        }
        return true;
    }

    /**
     * 检测邮箱是否存在
     *
     * @param email 邮箱
     * @return 返回是否合法
     */
    @Override
    public boolean isValidEmail(String email) {
        if(email == null || email.equals("")){
            throw new DuplicateEmailException("Email cannot be empty");
        }
        User valid = userMapper.getUserByEmail(email);
        if(valid != null){
            throw new DuplicateEmailException("This email has been registered");
        }
        return true;
    }

    /**
     * 检测验证码是否合法
     *
     * @param email    邮箱，用jwt解析出来的邮箱信息做校验
     * @param jwt      用jwt构成的验证码，包含邮箱信息
     * @param username 用户名
     * @return 返回是否合法
     */
    @Override
    public boolean isValidCode(String email, String jwt, String username) {
        boolean isValid = true;
        try{
            Claims claims = JwtUtils.parseJWT(jwt);
            String emailParsed = (String)claims.get("email");
            String usernameParsed = (String)claims.get("username");
            isValid = username.equals(usernameParsed) && email.equals(emailParsed);
        }catch (Exception e){
            isValid = false;
        }
        if(!isValid){
            throw new WrongCaptchaException("Invalid captcha");
        }
        return true;
    }

    /**
     * 注册用户
     *
     * @param user 用户信息
     */
    @Override
    public void register(User user) {
        user.setCreatedTime(LocalDate.now());
        user.setLastLoginTime(LocalDate.now());
        userMapper.insertUser(user);
    }
}
