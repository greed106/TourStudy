package com.ymj.tourstudy.mapper;

import com.ymj.tourstudy.pojo.CompressedDiary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiaryMapper {
    @Select("select * from tour_system.compressed_diary")
    List<CompressedDiary> getAllCompressedDiaries();
    @Insert("insert into tour_system.compressed_diary(username, title, compressed_content) values(#{username}, #{title}, #{compressedContent})")
    void setCompressedDiary(CompressedDiary compressedDiary);
}
