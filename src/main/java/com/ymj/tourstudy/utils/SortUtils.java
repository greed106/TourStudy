package com.ymj.tourstudy.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SortUtils {

    /**
     * 对泛型数组使用快速排序算法进行排序。
     * @param <T> 数组的数据类型。
     * @param array 要排序的数组。
     * @param comparator 定义排序规则的比较器。
     */
    public static <T> void quickSort(T[] array, Comparator<T> comparator) {
        quickSort(array, 0, array.length - 1, comparator);
    }

    /**
     * 快速排序的具体实现，包括递归调用和分区。
     * @param <T> 数组的数据类型。
     * @param array 要排序的数组。
     * @param low 分区的起始索引。
     * @param high 分区的结束索引。
     * @param comparator 定义排序规则的比较器。
     */
    private static <T> void quickSort(T[] array, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    /**
     * 对数组进行分区，并返回基准元素的索引。
     * @param <T> 数组的数据类型。
     * @param array 要分区的数组。
     * @param low 分区的起始索引。
     * @param high 分区的结束索引。
     * @param comparator 定义排序规则的比较器。
     * @return 基准元素的索引。
     */
    private static <T> int partition(T[] array, int low, int high, Comparator<T> comparator) {
        T pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }
    /**
     * 使用堆排序获取排序后的前n个元素。
     *
     * @param <T>        数组的数据类型。
     * @param array      要排序的数组。
     * @param n          要获取的前n个元素的数量。
     * @param comparator 定义排序规则的比较器。
     * @return 排序后的前n个元素。
     */
    public static <T> T[] getLastN(T[] array, int n, Comparator<T> comparator) {
        PriorityQueue<T> maxHeap = new PriorityQueue<>(n, comparator);
        for (T element : array) {
            if (maxHeap.size() < n) {
                maxHeap.offer(element);
            } else {
                // 如果新元素比堆顶元素小或者等于，那么将新元素加入堆中
                if (comparator.compare(element, maxHeap.peek()) <= 0) {
                    maxHeap.poll();
                    maxHeap.offer(element);
                }
            }
        }
        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), n);
        for (int i = 0; i < n; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    public static void main(String[] args) {
        Integer[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};

        Integer[] top5 = getLastN(array, array.length, Comparator.reverseOrder());

        quickSort(array, Comparator.naturalOrder());
        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.toString(top5));
    }

}
