package com.ymj.tourstudy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class MultiMap<K, V> {
    private MyHashMap<K, List<V>> map;

    public MultiMap(int capacity) {
        map = new MyHashMap<>(capacity);
    }

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public List<V> get(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void remove(K key, V value) {
        List<V> values = map.get(key);
        if (values != null) {
            values.remove(value);
            if (values.isEmpty()) {
                map.remove(key);
            }
        }
    }

    public void removeAll(K key) {
        map.remove(key);
    }

    public void clear() {
        map = new MyHashMap<>(map.getCapacity());
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public void forEach(BiConsumer<? super K, ? super List<V>> action) {
        map.forEach(action);
    }

    public static void main(String[] args) {
        MultiMap<String, Integer> multiMap = new MultiMap<>(10);

        // 添加元素
        multiMap.put("A", 1);
        multiMap.put("A", 2);
        multiMap.put("B", 3);
        multiMap.put("B", 4);
        multiMap.put("C", 5);

        // 打印所有元素
        System.out.println("MultiMap contains:");
        multiMap.forEach((key, values) -> {
            System.out.println(key + ": " + values);
        });

        // 测试获取元素
        System.out.println("Get values for key 'A': " + multiMap.get("A"));
        System.out.println("Get values for key 'D': " + multiMap.get("D")); // 不存在的键

        // 测试包含键
        System.out.println("Contains key 'B': " + multiMap.containsKey("B"));
        System.out.println("Contains key 'E': " + multiMap.containsKey("E")); // 不存在的键

        // 测试移除元素
        multiMap.remove("A", 1);
        System.out.println("After removing value '1' from key 'A': " + multiMap.get("A"));
        multiMap.removeAll("B");
        System.out.println("After removing all values for key 'B': " + multiMap.get("B"));
    }
}