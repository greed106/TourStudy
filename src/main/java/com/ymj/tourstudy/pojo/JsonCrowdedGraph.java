package com.ymj.tourstudy.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonCrowdedGraph {
    private String name;
    private String content;
    private String crowdedness;
    public JsonCrowdedGraph(CrowdedGraph graph) throws JsonProcessingException {
        this.name = graph.getName();
        ObjectMapper mapper = new ObjectMapper();
        this.content = mapper.writeValueAsString(graph.getGraph());
        this.crowdedness = mapper.writeValueAsString(graph.getCrowdedness());
    }
    public CrowdedGraph getCrowdedGraph() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Graph graph = mapper.readValue(content, Graph.class);
        double[][] crowdedness = mapper.readValue(this.crowdedness, double[][].class);
        return new CrowdedGraph(graph, crowdedness);
    }
}
