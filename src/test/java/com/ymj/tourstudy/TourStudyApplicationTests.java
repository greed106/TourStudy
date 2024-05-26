package com.ymj.tourstudy;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymj.tourstudy.mapper.GraphMapper;
import com.ymj.tourstudy.pojo.Graph;
import com.ymj.tourstudy.pojo.JsonGraph;
import com.ymj.tourstudy.pojo.Point;
import com.ymj.tourstudy.pojo.TourMap;
import com.ymj.tourstudy.utils.AVLTree;
import com.ymj.tourstudy.utils.HuffmanResult;
import com.ymj.tourstudy.utils.HuffmanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TourStudyApplicationTests {
    @Autowired
    GraphMapper graphMapper;
    @Autowired
    AVLTree<JsonGraph> avlTree;

    @Test
    void contextLoads() {
        String str = "i like like like java do you like a java";
        HuffmanResult result = HuffmanUtils.encode(str);
        String s = JSON.toJSONString(result);
        HuffmanResult huffmanResult = JSON.parseObject(s, HuffmanResult.class);
        String decode = HuffmanUtils.decode(huffmanResult);
        System.out.println("解码后的字符串：" + decode);
    }

    @Test
    void testGraphMapper() {
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
        graph.setName("graph");

        List<Point> points = Arrays.asList(a, b, c, d, e, f);
        graph.computeAllPairsShortestPaths(points);
        List<Point> p = Arrays.asList(e, d, f);
        // 测试TSP问题求解
        System.out.println("Shortest path through all points: " + graph.getShortestPathThroughPoints(p));

        ObjectMapper mapper = new ObjectMapper();
        try {
            String g = mapper.writeValueAsString(graph);
            String picture = "data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAEoAAAAyCAYAAAD7oU3dAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAFiUAABYlAUlSJPAAAARxSURBVGhD7ZjZK25fGIDPv2ceL5ALU4YIkQsiMiVlKKS4QCRSRKQQpUTkiszzLPN0/Z6eVfu0N5+sc37729/Xr3XxtNtrr71rP633Xe+7fn18fIjhZ4woTYwoTYwoTYwoTYwoTYwoTYwoTYwoTYwoTYwoTYwoTYJG1MvLixweHsrc3Jw0NjZKdna2xMbGSnh4uMTFxan7hoYGmZmZkYODAzXf13f8RVCIurq6ks7OTsnMzFRi8vPzpa2tTYaGhmR0dFSGh4elvb1dCgoKJCIiQjIyMqSjo0POzs58fs8fBFzU+vq6JCUlKQFlZWWyt7cnj4+P8vr6Ku/v72oOV+4Z53lFRYWan5iYKKurq1++6Q8CKmphYUGSk5MlNTVVZmdn5fn52ee8zxB28/PzKhyRRTj6mucmARPFSkJSeXm5bG9v+5zzE+SqqqoqtSLX1tZ8znGLgIgiJ/Fz6enpsru763OOLkdHR2plxcTEyOXlpc85buC5KMKGxE2OIdyscXLQ/v6+bGxsyNPTk+MdC0Jzc3NT5SnmW+OEcFhYmLS2tvptN/RcFCUAuxuJ256TFhcXJS8vT60ydrvPP4yYsbExtePl5uaqMsJ6xncIYXLdzs6O4z238FwUP8hq+hxy1EghISEKygNCyv784uJCiouL/8ypqalxyDw9PVWlxeTkpOM9t/BcFMUkItjq7eN9fX0SGRkpoaGhUltbKzc3N47nd3d36l1CDNHd3d3y9vbmmFNYWCj19fWOMbfwXBSJl2LSnmOARExx2dPTo3KV/ZkFYYvQkZEROT8///K8q6tLsrKyvoy7geeiaEvIQVYx6SYTExOq3fH17L/iuSjyCCvHukfY7e3tP2MXPj09rcLSuneTgKwoejfrB6+vr9Uu+K9Qk1nf/l+tKHIUDa6Vo7hGR0erVqS0tFT1cT/BPApW3rPnOhI88qx7N/FcFGUApwD2Xa+urk5ycnJUIWmf+x2UDuyclAj28aKiIvUt+5hbeC6KBpYywC6FH6cF6e3tdcz9jsHBQRXCW1tbf8Y4cuG7hJ99rlt4LopGluqaELIXjJwvkV+mpqbk/v7e8Y7Fw8ODSthIol2xwo7vVFZWSkpKikOem3guip9CCrsTRyXW+MnJiQob8k5TU5MsLS2pohMZ7G7Ly8vS0tKiZFZXVzsqd9ofCtHm5mbto5q/xXNRQJiQvCkO7cUlPzkwMKBkREVFqWt8fLzjnvC0yzg+Pla9H6uMNsYad5uAiAJOJpHFedLnvo7wRBjPSkpKVFj19/d/SfZIot1JSEiQlZUVxzO3CZgoILGzzVMycFSiGzaEL+HGSkISec3XPDcJqCjgZJKwIcdwVPJT+BC2rDDm856/V5JFwEUBDTG7WFpammpxOAWgwR0fH1e7HFeKSeokSgB2NxK3P3PSZ4JCFBBOHLpxnsRRCYmeFYM4rlTc7IrUSZQA/trdviNoRAU7RpQmRpQmRpQmRpQmRpQmRpQmRpQmRpQmRpQmRpQmRpQmRpQWH/IbI1R4beB9o5gAAAAASUVORK5CYII=";
            JsonGraph jsonGraph = new JsonGraph("graph", g, picture);
//            graphMapper.insertGraph(jsonGraph);
            graphMapper.updateGraph(jsonGraph);
            JsonGraph j = graphMapper.getGraphByName("graph");
            Graph gr = j.getGraph();
            System.out.println("Shortest path through all points: " + gr.getShortestPathThroughPoints(p));
            System.out.println(g);
//            System.out.println(g);
//            Graph graph1 = mapper.readValue(g, Graph.class);
//            System.out.println("Shortest path through all points: " + graph1.shortestPathThroughAllPoints(p));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Test
    void testJsonGraph(){
        String path = "C:/Users/ymj/Desktop/graph(4).json";
        String picturePath = "C:/Users/ymj/Desktop/picture.txt";
        try{
            File file = new File(path);
            File pic = new File(picturePath);
            String json = new String(Files.readAllBytes(file.toPath()));
            String picture = new String(Files.readAllBytes(pic.toPath()));
            ObjectMapper mapper = new ObjectMapper();
            Graph graph = mapper.readValue(json, Graph.class);
            graph.setName("scene");
            JsonGraph jsonGraph = new JsonGraph("scene", json, picture);
            graphMapper.insertGraph(jsonGraph);
//            System.out.println(graph);
            JsonGraph j = graphMapper.getGraphByName("scene");
            List<Point> q = j.getGraph().getAllPoints();
            // 输出点的数量
            System.out.println("q.size() " + q.size());
//            q.remove(0);
//            q.remove(1);
//            System.out.println(q);
//            List<Point> p = graph.shortestPathThroughAllPoints(q);
//            System.out.println(p);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void testAVLTree(){
        JsonGraph jsonGraph = avlTree.search("graph");
        try {
            TourMap map = jsonGraph.getMap();
            System.out.println(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
