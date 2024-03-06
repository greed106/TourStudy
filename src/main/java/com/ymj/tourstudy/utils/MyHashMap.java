package com.ymj.tourstudy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class MyHashMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final List<List<Entry<K, V>>> buckets;

    public MyHashMap(int capacity) {
        this.capacity = capacity;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        List<Entry<K, V>> bucket = buckets.get(bucketIndex);
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        List<Entry<K, V>> bucket = buckets.get(bucketIndex);
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        int bucketIndex = getBucketIndex(key);
        List<Entry<K, V>> bucket = buckets.get(bucketIndex);
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                return true;
            }
        }
        return false;
    }

    public void remove(K key) {
        int bucketIndex = getBucketIndex(key);
        List<Entry<K, V>> bucket = buckets.get(bucketIndex);
        Entry<K, V> toRemove = null;
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                toRemove = entry;
                break;
            }
        }
        if (toRemove != null) {
            bucket.remove(toRemove);
        }
    }

    /**
     * 对每个存储在HashMap中的键值对执行给定的操作。
     * @param action 应用于每个键值对的操作
     */
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                action.accept(entry.key, entry.value);
            }
        }
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    public static void main(String[] args) {
        // 创建一个容量为 10 的哈希表
        MyHashMap<String, Integer> hashMap = new MyHashMap<>(10);

        // 添加元素
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        hashMap.put("C", 3);
        hashMap.put("D", 4);

        // 打印所有元素
        System.out.println("HashMap contains:");
        for (int i = 0; i < 10; i++) {
            List<Entry<String, Integer>> bucket = hashMap.buckets.get(i);
            for (Entry<String, Integer> entry : bucket) {
                System.out.println(entry.key + ": " + entry.value);
            }
        }

        // 测试获取元素
        System.out.println("Get value for key 'B': " + hashMap.get("B"));
        System.out.println("Get value for key 'E': " + hashMap.get("E")); // 不存在的键

        // 测试包含键
        System.out.println("Contains key 'C': " + hashMap.containsKey("C"));
        System.out.println("Contains key 'F': " + hashMap.containsKey("F")); // 不存在的键

        // 测试移除元素
        hashMap.remove("B");
        hashMap.remove("E"); // 不存在的键
        System.out.println("After removing 'B':");
        for (int i = 0; i < 10; i++) {
            List<Entry<String, Integer>> bucket = hashMap.buckets.get(i);
            for (Entry<String, Integer> entry : bucket) {
                System.out.println(entry.key + ": " + entry.value);
            }
        }
    }

}
