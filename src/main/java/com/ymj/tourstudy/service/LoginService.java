package com.ymj.tourstudy.service;

import com.ymj.tourstudy.pojo.User;

public interface LoginService {
    /**
     * 验证登录
     * @param username 用户名
     * @param password 密码
     * @return 返回得到的实体user
     */
     User validLogin(String username, String password);

    /**
     * 下放jwt
     * @param username 用户名会含在jwt中，待验证时进行解析
     * @return 返回jwt
     */
    String getJwt(String username);
}
