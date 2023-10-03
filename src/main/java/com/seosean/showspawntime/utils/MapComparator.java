package com.seosean.showspawntime.utils;

import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator<Map.Entry<String, Integer>> {
//    @Override
//    public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
//        return entry2.getValue().compareTo(entry1.getValue());
//    }
    @Override
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        if (o1.getValue() == 0 && o2.getValue() != 0) {
            return -1; // 如果 o1 的值为0，o2 的值不为0，则 o1 排在最前面
        } else if (o1.getValue() != 0 && o2.getValue() == 0) {
            return 1; // 如果 o1 的值不为0，o2 的值为0，则 o2 排在最前面
        } else {
            return Integer.compare(o2.getValue(), o1.getValue()); // 其他情况按值的大小从大到小排序
        }
    }
}