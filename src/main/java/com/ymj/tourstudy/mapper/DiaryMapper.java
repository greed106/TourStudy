package com.ymj.tourstudy.mapper;

import com.ymj.tourstudy.pojo.CompressedDiary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiaryMapper {
    @Select("select * from tour_system.compressed_diary")
    List<CompressedDiary> getAllCompressedDiaries();
    @Insert("insert into tour_system.compressed_diary(username, title, compressed_content) values(#{username}, #{title}, #{compressedContent})")
    void setCompressedDiary(CompressedDiary compressedDiary);
    @Select("select * from tour_system.compressed_diary where username=#{username} and title=#{title}")
    CompressedDiary getCompressedDiary(@Param("username") String username, @Param("title")String title);
    @Update("update tour_system.compressed_diary set compressed_content=#{compressedContent} where username=#{username} and title=#{title}")
    void updateCompressedDiary(@Param("username")String username, @Param("title")String title, @Param("compressedContent")String compressedContent);
}
