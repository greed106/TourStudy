package com.ymj.tourstudy.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyPriorityQueueMaxHeap<T> {
    private ArrayList<T> heap;
    private Comparator<T> comparator;

    /**
     * 构造一个带有自定义比较器的优先队列。
     * @param comparator 自定义的比较器
     */
    public MyPriorityQueueMaxHeap(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    /**
     * 向优先队列中插入一个元素。
     * @param element 要插入的元素
     */
    public void insert(T element) {
        heap.add(element);
        int current = heap.size() - 1;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (comparator.compare(heap.get(current), heap.get(parent)) > 0) {
                swap(current, parent);
                current = parent;
            } else {
                break;
            }
        }
    }

    /**
     * 从优先队列中移除并返回最大元素。
     * @return 队列中的最大元素
     */
    public T remove() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        T removed = heap.get(0);
        T lastItem = heap.remove(heap.size() - 1);
        if (heap.size() > 0) {
            heap.set(0, lastItem);
            heapify(0);
        }
        return removed;
    }

    /**
     * 返回但不移除优先队列中的最大元素。
     * @return 队列中的最大元素
     */
    public T peek() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        return heap.get(0);
    }

    /**
     * 返回优先队列是否为空。
     * @return 如果优先队列为空，返回true；否则返回false
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private void heapify(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;
        if (left < heap.size() && comparator.compare(heap.get(left), heap.get(largest)) > 0) {
            largest = left;
        }
        if (right < heap.size() && comparator.compare(heap.get(right), heap.get(largest)) > 0) {
            largest = right;
        }
        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }

    public static void main(String[] args) {
        // 使用Lambda表达式定义比较器，例如，此处定义一个Integer的降序比较器
        MyPriorityQueueMaxHeap<Integer> priorityQueue = new MyPriorityQueueMaxHeap<>((a, b) -> b - a);
        priorityQueue.insert(10);
        priorityQueue.insert(5);
        priorityQueue.insert(20);
        priorityQueue.insert(1);

        System.out.println("最大元素: " + priorityQueue.remove());
        System.out.println("下一个最大元素: " + priorityQueue.peek());

        while (!priorityQueue.isEmpty()) {
            System.out.println("移除元素: " + priorityQueue.remove());
        }
    }
}
