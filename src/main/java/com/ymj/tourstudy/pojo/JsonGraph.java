package com.ymj.tourstudy.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonGraph {
    private String name;
    private String content;
    private String picture;
    public Graph getGraph() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, Graph.class);
    }
    public TourMap getMap() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Graph graph = mapper.readValue(content, Graph.class);
        String name = graph.getName();
        List<Point> points = graph.getAllPoints();
        return new TourMap(name, points, picture);
    }
}