package com.ymj.tourstudy.utils;

import java.util.ArrayList;
import java.util.List;

public class MySet<T> {
    private RedBlackTree<T> tree;

    public MySet() {
        tree = new RedBlackTree<>();
    }

    public void add(String key, T value) {
        tree.insert(key, value);
    }

    public void remove(String key) {
        tree.delete(key);
    }

    public T get(String key) {
        return tree.search(key);
    }

    public boolean contains(String key) {
        return tree.search(key) != null;
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public void clear() {
        tree.clear();
    }


    public List<String> keys() {
        List<String> keysList = new ArrayList<>();
        tree.collectKeys(tree.getRoot(), keysList);
        return keysList;
    }

    public List<T> values() {
        List<T> valuesList = new ArrayList<>();
        tree.collectValues(tree.getRoot(), valuesList);
        return valuesList;
    }

    public static void main(String[] args) {
        MySet<Integer> mySet = new MySet<>();
        mySet.add("a", 1);
        mySet.add("b", 2);
        mySet.add("c", 3);
        mySet.add("d", 4);
        mySet.add("e", 5);

        System.out.println("Contains 'c': " + mySet.contains("c")); // Output: true
        mySet.remove("c");
        System.out.println("Contains 'c' after removal: " + mySet.contains("c")); // Output: false

        System.out.println("Keys: " + mySet.keys());
        System.out.println("Values: " + mySet.values());
    }
}
