package com.unity.common.util;

/**
 * <p>
 * create by qinhuan at 2018/9/25 上午11:32
 */
public class StringUtil {

    /**
     * 生成指定长度字符串，不足位右补空格
     * @param str
     * @param length
     * @return
     */
    public static String formatStr(String str, int length) {
        int strLen;
        if (str == null) {
            strLen = 0;
        }else{
            strLen= str.length();
        }

        if (strLen == length) {
            return str;
        } else if (strLen < length) {
            int temp = (length - strLen)*4;
            String tem = "";
            for (int i = 0; i < temp; i++) {
                tem = tem + "&#160;";
            }
            return str + tem;
        }else{
            return str.substring(0,length);
        }
    }
}
