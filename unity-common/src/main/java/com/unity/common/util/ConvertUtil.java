package com.unity.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertUtil {
    public static List<Long> arrString2Long(String[] arrs){
        /*1.字符串数组转换为集合*/
        List<Long> list = Arrays.asList(arrs)
                //调用stream获得操作流
                .stream()
                //调用map将集合中的每个字符串装换为Long类型
                .map(Long::parseLong)
                //收约成一个Long类型集合
                .collect(Collectors.toList());
        return list;
    }
}
