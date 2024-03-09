package com.ymj.tourstudy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuffmanResult {
    private Node root;
    private String encodedData;

    public Node getRoot() {
        return root;
    }

    public String getEncodedData() {
        return encodedData;
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class Node {
    char character;
    int frequency;
    Node left, right;

}
