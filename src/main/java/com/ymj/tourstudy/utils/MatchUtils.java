package com.ymj.tourstudy.utils;

/**
 * 字符串匹配工具类,实现 Boyer-Moore 算法
 */
public class MatchUtils {
    /**
     * 使用 Boyer-Moore 算法在目标字符串中查找模式串
     *
     * @param pattern 模式串
     * @param target  目标字符串
     * @return 模式串在目标字符串中的起始位置索引,如果没有匹配则返回 -1
     */
    public static int bmMatch(String pattern, String target) {
        int targetLen = target.length(); // 目标串长度
        int patternLen = pattern.length(); // 模式串长度

        // 如果模式串比目标串长,没有可比性,直接返回 -1
        if (patternLen > targetLen) {
            return -1;
        }

        // 如果模式串为空,直接返回 0
        if (patternLen == 0) {
            return 0;
        }

        int[] badTable = buildBadTable(pattern); // 获得坏字符数值的数组
        int[] goodTable = buildGoodTable(pattern); // 获得好后缀数值的数组

        for (int i = patternLen - 1, j; i < targetLen; ) {
            for (j = patternLen - 1; target.charAt(i) == pattern.charAt(j); i--, j--) {
                if (j == 0) { // 指向模式串的首字符,说明匹配成功,直接返回就可以了
                    return i;
                }
            }
            // 如果出现坏字符,那么这个时候比较坏字符以及好后缀的数组,哪个大用哪个
            i += Math.max(goodTable[patternLen - j - 1], badTable[target.charAt(i)]);
        }
        return -1;
    }

    /**
     * 构建坏字符表
     *
     * @param pattern 模式串
     * @return 坏字符表数组
     */
    public static int[] buildBadTable(String pattern) {
        final int tableSize = 256;
        int[] badTable = new int[tableSize];
        int patternLen = pattern.length();

        for (int i = 0; i < badTable.length; i++) {
            badTable[i] = patternLen;
        }
        for (int i = 0; i < patternLen - 1; i++) {
            int k = pattern.charAt(i);
            badTable[k] = patternLen - 1 - i;
        }
        return badTable;
    }

    /**
     * 构建好后缀表
     *
     * @param pattern 模式串
     * @return 好后缀表数组
     */
    public static int[] buildGoodTable(String pattern) {
        int patternLen = pattern.length();
        int[] goodTable = new int[patternLen];
        int lastPrefixPosition = patternLen;

        for (int i = patternLen - 1; i >= 0; --i) {
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPosition = i + 1;
            }
            goodTable[patternLen - 1 - i] = lastPrefixPosition - i + patternLen - 1;
        }

        for (int i = 0; i < patternLen - 1; ++i) {
            int suffixLen = suffixLength(pattern, i);
            goodTable[suffixLen] = patternLen - 1 - i + suffixLen;
        }
        return goodTable;
    }

    /**
     * 判断字符串的子串是否为前缀子串
     *
     * @param pattern 模式串
     * @param p       起始位置
     * @return 如果是前缀子串则返回 true,否则返回 false
     */
    private static boolean isPrefix(String pattern, int p) {
        int patternLength = pattern.length();
        for (int i = p, j = 0; i < patternLength; ++i, ++j) {
            if (pattern.charAt(i) != pattern.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算后缀长度
     *
     * @param pattern 模式串
     * @param p       起始位置
     * @return 后缀长度
     */
    private static int suffixLength(String pattern, int p) {
        int patternLen = pattern.length();
        int len = 0;
        for (int i = p, j = patternLen - 1; i >= 0 && pattern.charAt(i) == pattern.charAt(j); i--, j--) {
            len += 1;
        }
        return len;
    }
    public static void main(String[] args){
        // 测试用例1: 模式串存在于目标字符串中
        String pattern1 = "cde";
        String target1 = "abcdefgabcijk";
        int result1 = MatchUtils.bmMatch(pattern1, target1);
        System.out.println("测试用例1: " + result1); // 预期输出: 2

        // 测试用例2: 模式串不存在于目标字符串中
        String pattern2 = "xyz";
        String target2 = "abcdefgh";
        int result2 = MatchUtils.bmMatch(pattern2, target2);
        System.out.println("测试用例2: " + result2); // 预期输出: -1

        // 测试用例3: 模式串为空字符串
        String pattern3 = "";
        String target3 = "abcdefgh";
        int result3 = MatchUtils.bmMatch(pattern3, target3);
        System.out.println("测试用例3: " + result3); // 预期输出: 0

        // 测试用例4: 目标字符串为空字符串
        String pattern4 = "abc";
        String target4 = "";
        int result4 = MatchUtils.bmMatch(pattern4, target4);
        System.out.println("测试用例4: " + result4); // 预期输出: -1

        // 测试用例5: 模式串和目标字符串相同
        String pattern5 = "abcdefgh";
        String target5 = "abcdefgh";
        int result5 = MatchUtils.bmMatch(pattern5, target5);
        System.out.println("测试用例5: " + result5); // 预期输出: 0

        // 测试用例6: 模式串长度大于目标字符串长度
        String pattern6 = "abcdefghijklmn";
        String target6 = "abcdefgh";
        int result6 = MatchUtils.bmMatch(pattern6, target6);
        System.out.println("测试用例6: " + result6); // 预期输出: -1
    }
}