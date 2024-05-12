package com.ymj.tourstudy.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TagMapper {
    @Insert("insert into tour_system.tag_diary(tag_name, diary_name) values(#{tagName}, #{diaryName})")
    void setDiaryTag(@Param("tagName")String tagName, @Param("diaryName")String diaryName);
    @Select("select tag_name from tour_system.tag_diary where diary_name=#{diaryName}")
    void getDiaryTag(String diaryName);
    @Insert("insert into tour_system.tag_tourism(tag_name, tourism_name) values(#{tagName}, #{tourismName})")
    void setTourismTag(@Param("tagName")String tagName, @Param("tourismName")String tourismName);
    @Select("select tag_name from tour_system.tag_tourism where tourism_name=#{tourismName}")
    void getTourismTag(String tourismName);
}
