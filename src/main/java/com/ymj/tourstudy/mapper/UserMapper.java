package com.ymj.tourstudy.mapper;

import com.ymj.tourstudy.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface UserMapper {

    @Select("select * from tour_system.user_info where username=#{username}")
    User getUserByUsername(String username);

    @Select("select * from tour_system.user_info where email=#{email}")
    User getUserByEmail(String email);

    @Insert("insert into tour_system.user_info(username,password,email,created_time,last_login_time) values(#{username},#{password},#{email},#{createdTime},#{lastLoginTime})")
    void insertUser(User user);

    @Update("update tour_system.user_info set last_login_time=#{lastLoginTime} where username=#{username}")
    void updateLastLoginTime(@Param("username")String username, @Param("lastLoginTime")LocalDate lastLoginTime);

    @Update("update tour_system.user_info set password=#{password} where username=#{username}")
    void updatePassword(String username, String password);

    @Delete("delete from tour_system.user_info where username=#{username}")
    void deleteUserByUsername(String username);

    @Select("select * from tour_system.user_info where username=#{username} and password=#{password}")
    User getUserByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

}
