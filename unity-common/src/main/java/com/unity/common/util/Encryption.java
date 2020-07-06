package com.unity.common.util;

import java.security.MessageDigest;

/**
 * 密码加密类
 * <p>
 * create by zhangwei at 2018年05月22日16:11:53
 */
public class Encryption {

    /**
     * 密码加密
     *
     * @param passWrod 明文密码
     * @return passWrod 密文密码
     * @author zhangwei
     * @since 2018年5月23日 15:27:00
     */
    public static String getEncryption(String passWrod,String employeeNo){
        return MD5(employeeNo+MD5(passWrod));
    }

    /**
     * 登录接口服务
     *
     * @param pwd 需要加密的密码
     * @return userInfoToken 用户信息和token
     * @author zhangwei
     * @since 2018年6月1日 17:27:00
     */
    public final static String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }

            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }
}
