package com.ymj.tourstudy.utils;

import lombok.Data;

import java.util.List;

@Data
public class RedBlackTree<T> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        String key;
        T value;
        Node left, right, parent;
        boolean color;

        Node(String key, T value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;

    public void insert(String key, T value) {
        root = insert(root, key, value, null);
        root.color = BLACK;
    }

    private Node insert(Node node, String key, T value, Node parent) {
        if (node == null) {
            return new Node(key, value, RED, parent);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value, node);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value, node);
        } else {
            node.value = value; // update value if key already exists
        }

        return balance(node);
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
        if (root != null) {
            root = delete(root, key);
            if (root != null) root.color = BLACK;
        }
    }

    private Node delete(Node node, String key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left != null && !isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (cmp == 0 && (node.right == null)) {
                return null;
            }
            if (node.right != null && !isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (cmp == 0) {
                Node min = min(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = delete(node.right, key);
            }
        }

        return balance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        x.parent = node.parent;
        node.parent = x;
        if (node.left != null) node.left.parent = node;
        return x;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        x.parent = node.parent;
        node.parent = x;
        if (node.right != null) node.right.parent = node;
        return x;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        if (node.left != null) node.left.color = !node.left.color;
        if (node.right != null) node.right.color = !node.right.color;
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node balance(Node node) {
        if (isRed(node.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);
        return node;
    }

    protected void collectKeys(Node node, List<String> keysList) {
        if (node != null) {
            collectKeys(node.left, keysList);
            keysList.add(node.key);
            collectKeys(node.right, keysList);
        }
    }

    protected void collectValues(Node node, List<T> valuesList) {
        if (node != null) {
            collectValues(node.left, valuesList);
            valuesList.add(node.value);
            collectValues(node.right, valuesList);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }


    public static void main(String[] args) {
        RedBlackTree<Integer> rbTree = new RedBlackTree<>();
        rbTree.insert("a", 1);
        rbTree.insert("b", 2);
        rbTree.insert("c", 3);
        rbTree.insert("d", 4);
        rbTree.insert("e", 5);

        System.out.println("Search for key 'c': " + rbTree.search("c")); // Output: 3
        rbTree.delete("c");
        System.out.println("Search for key 'c' after deletion: " + rbTree.search("c")); // Output: null
    }
}
