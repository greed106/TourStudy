package com.ymj.tourstudy.utils;

import java.util.List;

public class BinarySearchTree<T> {
    private class Node {
        String key;
        T value;
        Node left, right;

        Node(String key, T value) {
            this.key = key;
            this.value = value;
            this.left = this.right = null;
        }
    }

    private Node root;

    public void insert(String key, T value) {
        root = insert(root, key, value);
    }

    private Node insert(Node node, String key, T value) {
        if (node == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value; // update value if key already exists
        }

        return node;
    }

    public T search(String key) {
        Node node = search(root, key);
        return node == null ? null : node.value;
    }

    private Node search(Node node, String key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return search(node.left, key);
        } else if (cmp > 0) {
            return search(node.right, key);
        } else {
            return node;
        }
    }

    public void delete(String key) {
        root = delete(root, key);
    }

    private Node delete(Node root, String key) {
        if (root == null) return root;

        int cmp = key.compareTo(root.key);
        if (cmp < 0)
            root.left = delete(root.left, key);
        else if (cmp > 0)
            root.right = delete(root.right, key);
        else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            Node temp = minValueNode(root.right);
            root.key = temp.key;
            root.value = temp.value;
            root.right = delete(root.right, temp.key);
        }

        return root;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert("a", 1);
        bst.insert("b", 2);
        bst.insert("c", 3);
        bst.insert("d", 4);
        bst.insert("e", 5);

        System.out.println("Search for key 'c': " + bst.search("c")); // Output: 3
        bst.delete("c");
        System.out.println("Search for key 'c' after deletion: " + bst.search("c")); // Output: null
    }
}

