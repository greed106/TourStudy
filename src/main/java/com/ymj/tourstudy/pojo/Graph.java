package com.ymj.tourstudy.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ymj.tourstudy.serializer.PointKeyDeserializer;
import com.ymj.tourstudy.serializer.PointKeySerializer;
import com.ymj.tourstudy.utils.MyPriorityQueueMinHeap;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
public class Graph {
    private String name;
    @JsonSerialize(keyUsing = PointKeySerializer.class)
    @JsonDeserialize(keyUsing = PointKeyDeserializer.class)
    private Map<Point, List<Edge>> adjList;
    private int[][] shortestDistances; // 存储所有点对之间的最短距离

    public Graph() {
        adjList = new HashMap<>();
    }

    public void addVertex(Point point) {
        adjList.putIfAbsent(point, new ArrayList<>());
    }

    public void addEdge(Point source, Point destination, int weight) {
        addVertex(source);
        addVertex(destination);
        adjList.get(source).add(new Edge(destination, weight));
        // 对于无向图，还需要添加反向边
        adjList.get(destination).add(new Edge(source, weight));
    }

    public List<Point> dijkstra(Point source, Point target) {
        Map<Point, Integer> distances = new HashMap<>();
        Map<Point, Point> predecessors = new HashMap<>();
        MyPriorityQueueMinHeap<PointDistancePair> queue = new MyPriorityQueueMinHeap<>(Comparator.comparingInt(p -> p.distance));

        // 初始化所有点的距离为无穷大，除了起点距离为0
        for (Point p : adjList.keySet()) {
            distances.put(p, p.equals(source) ? 0 : Integer.MAX_VALUE);
            queue.insert(new PointDistancePair(p, distances.get(p)));
        }

        while (!queue.isEmpty()) {
            PointDistancePair pair = queue.remove();
            Point u = pair.point;

            // 如果找到目标点，停止算法
            if (u.equals(target)) {
                break;
            }

            // 遍历当前点u的所有邻接点
            if (adjList.get(u) != null) {
                for (Edge e : adjList.get(u)) {
                    Point v = e.getDestination();
                    int weight = e.getWeight();
                    int distanceThroughU = distances.get(u) + weight;
                    if (distanceThroughU < distances.get(v)) {
                        distances.put(v, distanceThroughU);
                        predecessors.put(v, u);
                        queue.insert(new PointDistancePair(v, distanceThroughU));  // 插入更新后的距离
                    }
                }
            }
        }

        // 重构路径
        return reconstructPath(predecessors, source, target);
    }

    private List<Point> reconstructPath(Map<Point, Point> predecessors, Point source, Point target) {
        LinkedList<Point> path = new LinkedList<>();
        if (!predecessors.containsKey(target)) {
            return path; // 如果没有路径则返回空列表
        }
        Point step = target;
        while (step != null && !step.equals(source)) {
            path.addFirst(step);
            step = predecessors.get(step);
        }
        if (step != null) {
            path.addFirst(source);
        }
        return path;
    }

    // 辅助类，用于在优先队列中管理带距离的点
    private static class PointDistancePair {
        Point point;
        int distance;

        PointDistancePair(Point point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    // 使用Floyd-Warshall算法计算所有点对的最短路径
    public void computeAllPairsShortestPaths(List<Point> points) {
        int numVertices = points.size();
        shortestDistances = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(shortestDistances[i], Integer.MAX_VALUE / 2);
            shortestDistances[i][i] = 0;
        }

        for (Point u : adjList.keySet()) {
            int uIndex = points.indexOf(u);
            for (Edge e : adjList.get(u)) {
                int vIndex = points.indexOf(e.getDestination());
                shortestDistances[uIndex][vIndex] = Math.min(shortestDistances[uIndex][vIndex], e.getWeight());
            }
        }

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (shortestDistances[i][j] > shortestDistances[i][k] + shortestDistances[k][j]) {
                        shortestDistances[i][j] = shortestDistances[i][k] + shortestDistances[k][j];
                    }
                }
            }
        }
    }

    // 使用动态规划求解TSP问题
    public List<Point> shortestPathThroughAllPoints(List<Point> points) {
        int n = points.size();
        int[][] dp = new int[1 << n][n];
        int[][] path = new int[1 << n][n];
        int INF = Integer.MAX_VALUE / 2;

        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
        }

        for (int mask = 0; mask < (1 << n); mask++) {
            for (int end = 0; end < n; end++) {
                if ((mask & (1 << end)) == 0) continue;
                int prevMask = mask ^ (1 << end);
                if (prevMask == 0) continue;
                for (int next = 0; next < n; next++) {
                    if ((mask & (1 << next)) == 0 || end == next) continue;
                    if (dp[mask][end] > dp[prevMask][next] + shortestDistances[next][end]) {
                        dp[mask][end] = dp[prevMask][next] + shortestDistances[next][end];
                        path[mask][end] = next;
                    }
                }
            }
        }

        // 重建路径
        int last = (1 << n) - 1;
        int minCost = INF;
        int lastNode = -1;
        for (int i = 0; i < n; i++) {
            if (minCost > dp[last][i]) {
                minCost = dp[last][i];
                lastNode = i;
            }
        }

        LinkedList<Point> shortestPath = new LinkedList<>();
        int state = last;
        while (state > 0) {
            shortestPath.addFirst(points.get(lastNode));
            int temp = lastNode;
            lastNode = path[state][lastNode];
            state = state ^ (1 << temp);
        }

        // 将起点再次加入路径中形成环
        shortestPath.addLast(shortestPath.getFirst());

        // 确保每一步都使用实际存在的边，修正最终路径
        LinkedList<Point> finalPath = new LinkedList<>();
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            List<Point> segment = dijkstra(shortestPath.get(i), shortestPath.get(i + 1));
            // 去掉每段路径的重复起点
            if (i > 0) segment.remove(0);
            finalPath.addAll(segment);
        }

        return finalPath;
    }

    @JSONField(serialize = false)
    public Map<Point, List<Edge>> getAdjList() {
        return adjList;
    }

    @JSONField(deserialize = false)
    public void setAdjList(Map<Point, List<Edge>> adjList) {
        this.adjList = adjList;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        // 添加节点
        Point a = new Point(0, 0, 0, "A");
        Point b = new Point(1, 0, 0, "B");
        Point c = new Point(2, 0, 0, "C");
        Point d = new Point(3, 0, 0, "D");
        Point e = new Point(4, 0, 0, "E");
        Point f = new Point(5, 0, 0, "F");

        // 添加边
        graph.addEdge(a, b, 1);
        graph.addEdge(a, c, 2);
        graph.addEdge(a, d, 1);
        graph.addEdge(b, d, 3);
        graph.addEdge(b, f, 5);
        graph.addEdge(c, d, 2);
        graph.addEdge(d, e, 2);
        graph.addEdge(e, f, 2);

        List<Point> points = Arrays.asList(a, b, c, d, e, f);
        graph.computeAllPairsShortestPaths(points);
        List<Point> p = Arrays.asList(e, d, f);
        // 测试TSP问题求解
        System.out.println("Shortest path through all points: " + graph.shortestPathThroughAllPoints(p));

        ObjectMapper mapper = new ObjectMapper();
        try {
            String g = mapper.writeValueAsString(graph);
            System.out.println(g);
            Graph graph1 = mapper.readValue(g, Graph.class);
            System.out.println("Shortest path through all points: " + graph1.shortestPathThroughAllPoints(p));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

//        System.out.println("Shortest path through all points: " + graph1.shortestPathThroughAllPoints(p));
    }
}
