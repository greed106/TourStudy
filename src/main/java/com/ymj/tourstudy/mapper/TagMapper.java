package com.ymj.tourstudy.mapper;

import com.ymj.tourstudy.pojo.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagMapper {
    @Insert("insert into tour_system.tag_diary(tag_name, diary_name) values(#{tagName}, #{diaryName})")
    void insertDiaryTag(@Param("tagName")String tagName, @Param("diaryName")String diaryName);
    @Insert("insert into tour_system.tag_tourism(tag_name, tourism_name) values(#{tagName}, #{tourismName})")
    void insertTourismTag(@Param("tagName")String tagName, @Param("tourismName")String tourismName);
    @Select("select tag_name from tour_system.tag")
    List<String> getAllTags();
    @Select("select diary_name from tour_system.tag_diary where tag_name=#{tagName}")
    List<String> getDiaryNameByTag(String tagName);
    @Select("select tourism_name from tour_system.tag_tourism where tag_name=#{tagName}")
    List<String> getTourismNameByTag(String tagName);
    @Select("select tag_name from tour_system.tag_diary where diary_name=#{diaryName}")
    List<String> getTagsByDiaryName(String diaryName);
    @Select("select tag_name from tour_system.tag_tourism where tourism_name=#{tourismName}")
    List<String> getTagsByTourismName(String tourismName);
    @Delete("delete from tour_system.tag_diary where tag_name=#{tagName} and diary_name=#{diaryName}")
    void deleteDiaryTag(@Param("tagName")String tagName, @Param("diaryName")String diaryName);
    @Delete("delete from tour_system.tag_tourism where tag_name=#{tagName} and tourism_name=#{tourismName}")
    void deleteTourismTag(@Param("tagName")String tagName, @Param("tourismName")String tourismName);
    @Select("select * from tour_system.tag where tag_name=#{name}")
    Tag getTagByName(String name);
    @Insert("insert into tour_system.tag(tag_name) values(#{name})")
    void insertTag(String name);

}
