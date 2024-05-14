package com.ymj.tourstudy.utils;

public class AVLTree<T> {
    private class Node {
        String key;
        T value;
        int height;
        Node left, right;

        Node(String key, T value) {
            this.key = key;
            this.value = value;
            this.height = 1;
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

        node.height = 1 + Math.max(height(node.left), height(node.right));

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

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
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
            if ((root.left == null) || (root.right == null)) {
                Node temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                Node temp = minValueNode(root.right);

                root.key = temp.key;
                root.value = temp.value;

                root.right = delete(root.right, temp.key);
            }
        }

        if (root == null)
            return root;

        root.height = Math.max(height(root.left), height(root.right)) + 1;

        return balance(root);
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public static void main(String[] args) {
        AVLTree<Integer> avlTree = new AVLTree<>();
        avlTree.insert("a", 1);
        avlTree.insert("b", 2);
        avlTree.insert("c", 3);
        avlTree.insert("d", 4);
        avlTree.insert("e", 5);

        System.out.println("Search for key 'c': " + avlTree.search("c")); // Output: 3
        avlTree.delete("c");
        System.out.println("Search for key 'c' after deletion: " + avlTree.search("c")); // Output: null
    }
}
