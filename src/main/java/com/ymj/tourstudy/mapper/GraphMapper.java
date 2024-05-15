package com.ymj.tourstudy.mapper;

import com.ymj.tourstudy.pojo.JsonGraph;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GraphMapper {
    @Select("select * from tour_system.graph where name=#{name}")
    JsonGraph getGraphByName(String name);
    @Insert("insert into tour_system.graph(name, content, picture) values(#{name}, #{content}, #{picture})")
    void insertGraph(JsonGraph jsonGraph);
    @Update("update tour_system.graph set content=#{content}, picture=#{picture} where name=#{name}")
    void updateGraph(JsonGraph jsonGraph);
    @Select("select * from tour_system.graph")
    List<JsonGraph> getAllGraphs();
}
