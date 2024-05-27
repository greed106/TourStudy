package com.ymj.tourstudy.pojo;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CrowdedGraph extends Graph{
    private double[][] crowdedness; // 存储所有点对之间的拥挤度
    private static final double[] CROWDED_VALUES = {0, 0.33, 0.66, 1.0};
    // 创建随机的拥挤度（双向边的拥挤度一致，拥挤度取值为CROWDED_VALUES）
    private void createRandomCrowdedness() {
        crowdedness = new double[adjList.size()][adjList.size()];
        for (int i = 0; i < adjList.size(); i++) {
            for (int j = i + 1; j < adjList.size(); j++) {
                int randomIndex = (int) (Math.random() * CROWDED_VALUES.length);
                crowdedness[i][j] = crowdedness[j][i] = CROWDED_VALUES[randomIndex];
            }
        }
    }

    private void changeWeightByCrowdedness() {
        // 遍历adjList
        for (Point u : adjList.keySet()) {
            for (Edge e : adjList.get(u)) {
                Point v = e.getDestination();
                int uIndex = u.getIndex();
                int vIndex = v.getIndex();
                int newWeight = (int) (Math.round(e.getWeight() * (1 + crowdedness[uIndex][vIndex])));
                e.setWeight(newWeight);
            }
        }
        List<Point> points = new ArrayList<>(adjList.keySet());
        // 按照index从小到大排序
        points.sort((p1, p2) -> p1.getIndex() - p2.getIndex());
        computeAllPairsShortestPaths(points);
    }

    public CrowdedGraph(Graph graph) {
        super(graph);
        createRandomCrowdedness();
        changeWeightByCrowdedness();
    }

    public CrowdedGraph(Graph graph, double[][] crowdedness) {
        super(graph);
        this.crowdedness = crowdedness;
    }

    public Graph getGraph() {
        return new Graph(this);
    }

    public double[][] getCrowdedness() {
        return crowdedness;
    }

    public List<List<CrowdedEdge>> getCrowdedEdges(){
        List<List<CrowdedEdge>> crowdedEdges = new ArrayList<>();
        List<Point> points = new ArrayList<>(adjList.keySet());
        // 按照index从小到大排序
        points.sort((p1, p2) -> p1.getIndex() - p2.getIndex());
        for(Point u : points){
            List<CrowdedEdge> edges = new ArrayList<>();
            for (Edge e : adjList.get(u)) {
                Point v = e.getDestination();
                int uIndex = u.getIndex();
                int vIndex = v.getIndex();
                double crowdedness = this.crowdedness[uIndex][vIndex];
                edges.add(new CrowdedEdge(v.getIndex(), crowdedness));
            }
            crowdedEdges.add(edges);
        }
        return crowdedEdges;
    }

    public void setCrowdedness(double[][] crowdedness) {
        this.crowdedness = crowdedness;
    }
}
