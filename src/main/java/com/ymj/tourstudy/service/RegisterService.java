package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.User;

public interface RegisterService {
    /**
     * 检测用户名是否存在
     * @param username 用户名
     * @return 返回是否合法
     */
    boolean isValidUsername(String username);

    /**
     * 检测邮箱是否存在
     * @param email 邮箱
     * @return 返回是否合法
     */
    boolean isValidEmail(String email);

    /**
     * 检测验证码是否合法
     *
     * @param email    邮箱，用jwt解析出来的邮箱信息做校验
     * @param jwt      用jwt构成的验证码，包含邮箱信息
     * @param username 用户名
     * @return 返回是否合法
     */
    boolean isValidCode(String email, String jwt, String username);

    /**
     * 注册用户
     * @param user 用户信息
     */
    void register(User user);
}
