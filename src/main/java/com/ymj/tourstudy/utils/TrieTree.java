package com.ymj.tourstudy.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// 定义泛型TrieNode类
class TrieNode<T> {
    public char c;
    public MyHashMap<Character, TrieNode<T>> children = new MyHashMap<>(128);
    public boolean isEndOfWord;
    public T data; // 存储与键关联的数据

    // 构造函数
    public TrieNode() {}

    public TrieNode(char c) {
        this.c = c;
    }
}

// 定义TrieTree类，使用泛型以存储不同类型的数据
public class TrieTree<T> {
    private TrieNode<T> root;

    // 构造函数初始化根节点
    public TrieTree() {
        root = new TrieNode<>();
    }

    // 插入方法
    public void insert(String key, T data) {
        TrieNode<T> current = root;
        for (char c : key.toCharArray()) {
            // 如果当前字符不存在于children中，则创建一个新节点
            current = current.children.computeIfAbsent(c, k -> new TrieNode<>(c));
        }
        current.isEndOfWord = true;
        current.data = data; // 在键的末尾节点存储数据
    }

    // 查找方法
    public T search(String key) {
        TrieNode<T> current = root;
        for (char c : key.toCharArray()) {
            TrieNode<T> node = current.children.get(c);
            if (node == null) {
                return null; // 未找到key
            }
            current = node;
        }
        return current.isEndOfWord ? current.data : null; // 返回找到的数据
    }

    private boolean delete(TrieNode<T> current, String key, int index){
        // 递归出口
        if(index == key.length()){
            if(!current.isEndOfWord){
                return false;
            }
            current.isEndOfWord = false;
            return current.children.isEmpty();
        }
        char ch = key.charAt(index);
        TrieNode<T> node = current.children.get(ch);
        if(node == null){
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, key, index + 1);

        if(shouldDeleteCurrentNode){
            current.children.remove(ch);
            return current.children.isEmpty();
        }

        return false;
    }

    // 删除方法
    public void delete(String key){
        delete(root, key, 0);
    }
    // 查找前缀匹配的所有键
    public List<String> keysWithPrefix(String prefix) {
        List<String> keys = new LinkedList<>();
        TrieNode<T> node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return keys; // 如果前缀不存在，返回空列表
            }
        }
        collect(node, new StringBuilder(prefix), keys);
        return keys;
    }

    private void collect(TrieNode<T> node, StringBuilder prefix, List<String> keys) {
        if (node == null) return;
        if (node.isEndOfWord) keys.add(prefix.toString());
        for (char c : node.children.keySet()) {
            prefix.append(c);
            collect(node.children.get(c), prefix, keys);
            prefix.deleteCharAt(prefix.length() - 1); // 删除添加的字符，回溯
        }
    }

    // 检查是否存在以给定前缀开始的键
    public boolean startsWith(String prefix) {
        TrieNode<T> current = root;
        for (char c : prefix.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        collectWords(root, new StringBuilder(), sb);
        return sb.toString();
    }

    private void collectWords(TrieNode<T> node, StringBuilder path, StringBuilder result) {
        if (node.isEndOfWord) {
            result.append(path.toString()).append(" -> ").append(node.data).append("\n");
        }
        node.children.forEach((character, trieNode) -> {
            path.append(character);
            collectWords(trieNode, path, result);
            path.deleteCharAt(path.length() - 1);
        });
    }

    public static void main(String[] args) {
        // 测试插入和搜索
        TrieTree<Integer> trieInt = new TrieTree<>();
        trieInt.insert("apple", 5);
        System.out.println("Search 'apple': " + trieInt.search("apple")); // 应输出 5
        System.out.println("Search 'app': " + trieInt.search("app")); // 应输出 null，因为'app'不是一个完整的键

        // 测试字符串类型数据
        TrieTree<String> trieString = new TrieTree<>();
        trieString.insert("hello", "world");
        System.out.println("Search 'hello': " + trieString.search("hello")); // 应输出 "world"

        // 测试删除功能
        trieInt.delete("apple");
        System.out.println("Search 'apple' after delete: " + trieInt.search("apple")); // 应输出 null，因为'apple'已被删除

        // 测试查找前缀匹配的所有键
        trieString.insert("helicopter", "fly");
        trieString.insert("hell", "inferno");
        List<String> keysWithPrefix = trieString.keysWithPrefix("he");
        System.out.println("Keys with prefix 'he': " + keysWithPrefix); // 应输出 [hello, helicopter, hell]

        // 测试是否存在以给定前缀开始的键
        System.out.println("Starts with 'hel': " + trieString.startsWith("hel")); // 应输出 true
        System.out.println("Starts with 'world': " + trieString.startsWith("world")); // 应输出 false
    }
}


