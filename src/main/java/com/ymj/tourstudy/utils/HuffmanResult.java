package com.ymj.tourstudy.utils;

public class HuffmanResult {
    private final Node root;
    private final String encodedData;

    public HuffmanResult(Node root, String encodedData) {
        this.root = root;
        this.encodedData = encodedData;
    }

    public Node getRoot() {
        return root;
    }

    public String getEncodedData() {
        return encodedData;
    }
}

class Node {
    char character;
    int frequency;
    Node left, right;

    Node(char character, int frequency, Node left, Node right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}
