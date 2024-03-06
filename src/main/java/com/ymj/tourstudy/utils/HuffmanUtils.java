package com.ymj.tourstudy.utils;

import java.util.Comparator;

public class HuffmanUtils {


    /**
     * 根据输入字符串构建哈夫曼树。
     * @param text 输入的字符串
     * @return 构建好的哈夫曼树的根节点
     */
    private static Node buildHuffmanTree(String text) {
        // 统计每个字符出现的频率
        MyHashMap<Character, Integer> freqMap = new MyHashMap<>(256);
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.get(c) == null ? 1 : freqMap.get(c) + 1);
        }

        // 使用优先队列构建哈夫曼树
        MyPriorityQueueMinHeap<Node> priorityQueue = new MyPriorityQueueMinHeap<>(Comparator.comparingInt(a -> a.frequency));
        // 将每个字符及其频率作为一个节点插入优先队列
        freqMap.forEach((character, frequency) -> {
            priorityQueue.insert(new Node(character, frequency, null, null));
        });
        // 从优先队列中取出两个频率最小的节点，合并成一个新节点，然后再插入优先队列
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.remove();
            Node right = priorityQueue.remove();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            priorityQueue.insert(parent);
        }

        return priorityQueue.remove();
    }

    /**
     * 根据哈夫曼树为每个字符生成编码。
     * @param root 哈夫曼树的根节点
     * @param code 当前编码
     * @param codes 存储字符及其编码的映射
     */
    private static void generateCodes(Node root, String code, MyHashMap<Character, String> codes) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            codes.put(root.character, code);
        }

        generateCodes(root.left, code + "0", codes);
        generateCodes(root.right, code + "1", codes);
    }

    /**
     * 对输入的字符串进行哈夫曼编码。
     * @param text 输入的字符串
     * @return 哈夫曼编码结果，包括编码后的字符串和哈夫曼树根节点
     */
    public static HuffmanResult encode(String text) {
        Node root = buildHuffmanTree(text);
        MyHashMap<Character, String> codes = new MyHashMap<>(256);
        generateCodes(root, "", codes);

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(codes.get(c));
        }

        return new HuffmanResult(root, encoded.toString());
    }

    /**
     * 根据哈夫曼树和编码后的字符串进行解码。
     * @param huffmanResult 包含哈夫曼树根节点和编码后字符串的结果对象
     * @return 解码后的原始字符串
     */
    public static String decode(HuffmanResult huffmanResult) {
        StringBuilder decoded = new StringBuilder();
        Node current = huffmanResult.getRoot();
        for (int i = 0; i < huffmanResult.getEncodedData().length(); i++) {
            if (huffmanResult.getEncodedData().charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                decoded.append(current.character);
                current = huffmanResult.getRoot(); // 重置为根节点开始下一个字符的解码
            }
        }
        return decoded.toString();
    }

    public static void main(String[] args) {
        String text = "example of huffman encoding";
        HuffmanResult result = HuffmanUtils.encode(text);
        System.out.println("Encoded Huffman Data: " + result.getEncodedData());

        String decodedText = HuffmanUtils.decode(result);
        System.out.println("Decoded Text: " + decodedText);
    }
}



