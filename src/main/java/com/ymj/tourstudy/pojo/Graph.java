package com.ymj.tourstudy.pojo;

import com.ymj.tourstudy.utils.MyPriorityQueueMinHeap;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
public class Graph {
    private Map<Point, List<Edge>> adjList;

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

    public static void main(String[] args) {
        Graph graph = new Graph();

        // 添加节点
        Point a = new Point(0, 0, 0, "A");
        Point b = new Point(1, 0, 0, "B");
        Point c = new Point(2, 0, 0, "C");
        Point d = new Point(3, 0, 0, "D");
        Point e = new Point(4, 0, 0, "E");
        Point f = new Point(5, 0, 0, "F");
        Point g = new Point(6, 0, 0, "G");
        Point h = new Point(7, 0, 0, "H");
        Point i = new Point(8, 0, 0, "I");
        Point j = new Point(9, 0, 0, "J");

        // 添加边，构造一个复杂的图
        graph.addEdge(a, b, 4);
        graph.addEdge(a, c, 2);
        graph.addEdge(b, c, 3);
        graph.addEdge(b, d, 2);
        graph.addEdge(b, e, 3);
        graph.addEdge(c, d, 4);
        graph.addEdge(c, e, 5);
        graph.addEdge(d, e, 1);
        graph.addEdge(d, f, 8);
        graph.addEdge(e, f, 6);
        graph.addEdge(g, h, 3);
        graph.addEdge(h, i, 2);
        graph.addEdge(i, j, 4);
        graph.addEdge(g, j, 7);
        graph.addEdge(f, h, 3);
        graph.addEdge(a, j, 10);

        // 测试从 A 到 J 的最短路径
        System.out.println("Shortest path from A to J: " + graph.dijkstra(a, j));

        // 测试从 G 到 E 的最短路径
        System.out.println("Shortest path from G to E: " + graph.dijkstra(g, e));

        // 测试从 D 到 J 的最短路径
        System.out.println("Shortest path from D to J: " + graph.dijkstra(d, j));
    }

}
