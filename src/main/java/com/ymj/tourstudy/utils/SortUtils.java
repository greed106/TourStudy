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
     * 使用优先队列获取排序后的前n个元素。
     *
     * @param <T>        数组的数据类型。
     * @param array      要排序的数组。
     * @param n          要获取的后n个元素的数量。
     * @param comparator 定义排序规则的比较器。
     * @return 排序后的后n个元素。
     */
    public static <T> T[] getLastN(T[] array, int n, Comparator<T> comparator) {
        if(n > array.length){
            n = array.length;
        }
        // 使用优先队列来维护前 n 个最大元素
        MyPriorityQueueMinHeap<T> queue = new MyPriorityQueueMinHeap<>(comparator);

        // 遍历数组中的每一个元素
        for (T element : array) {
            if (queue.size() < n) {
                // 如果优先队列中元素数量少于 n，直接添加
                queue.insert(element);
            } else {
                // 否则，比较当前元素和优先队列的队头元素
                if (comparator.compare(element, queue.peek()) > 0) {
                    // 如果当前元素比队头元素更大（根据 comparator），则替换队头元素
                    queue.remove();
                    queue.insert(element);
                }
            }
        }

        // 将优先队列中的元素转化为数组返回
        @SuppressWarnings("unchecked")
        T[] result = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), n);
        for (int i = 0; i < n; i++) {
            result[i] = queue.remove();
        }

        // 返回前，对结果数组进行排序
        Arrays.sort(result, comparator);
        return result;
    }
    public static <T> void reverse(T[] array) {
        int n = array.length;
        for (int i = 0; i < n / 2; i++) {
            T temp = array[i];
            array[i] = array[n - i - 1];
            array[n - i - 1] = temp;
        }
    }

    public static void main(String[] args) {
        Integer[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};

        Integer[] top5 = getLastN(array, 5, Comparator.reverseOrder());

        quickSort(array, Comparator.reverseOrder());
        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.toString(top5));
    }

}
