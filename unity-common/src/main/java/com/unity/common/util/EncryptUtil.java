package com.unity.common.util;

import com.unity.common.constants.Constants;
import com.unity.common.constants.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 密码加密类
 * <p>
 * create by Jung at 2018年06月14日16:30:00
 */
@Slf4j
public class EncryptUtil {

    /**
     * 加密用户密码的工具类
     *
     * @param username 用户名
     * @param password 密码
     * @return 加密后的字符串
     * @author Jung
     * @since 2018年06月14日16:37:21
     */
    public static String encryptPassword(String username, String password) {
        return DigestUtils.md5DigestAsHex((username + DigestUtils.md5DigestAsHex(password.getBytes())).getBytes());
    }

    private static final String aesKey = "9wUqEecQfBCn8fC9";

    /**
     * 生成签名
     *
     * @param data 需要生成的数据
     * @param key  需要生成的key
     * @return 生成好的数据
     * @author Jung
     * @since 2017年9月11日11:32:33
     */
    public static String getSingByMD5(String data, String key) {
        return Base64Utils.encodeToString(DigestUtils.md5Digest((data + "|" + key).getBytes()));
    }

    /**
     * Y5报文AES加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptAES(String data, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        log.debug("加密前数据: {}", data);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decryptAES(String data) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        return decryptAES(data, aesKey);
    }

    /**
     * Y5报文AES解密
     */
    public static String decryptAES(String data, String key) throws NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);

        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));
        String respStr = new String(decryptedData, "UTF-8");
        log.debug("解密后数据: {}", respStr);
        return respStr;
    }

    /**
     * AES/ECB/PKCS7Padding加密
     *
     * @param data 需要加密的数据
     * @param key  加密的key
     * @return 加密后的base64
     * @author Jung
     * @since 2017年7月27日15:11:17
     */
    public static String encryptAESECBPKCS7(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        Key key1 = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding"); // 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key1);// 初始化
        return new String(Base64.getEncoder().encode(cipher.doFinal(data.getBytes())));
    }

    public static String decryptAESECBPKCS7(String data) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return decryptAESECBPKCS7(data, aesKey);
    }

    /**
     * AES/ECB/PKCS7Padding解密
     *
     * @param data 需要解密的数据
     * @param key  加密的key
     * @return 解密后的base64
     * @author Jung
     * @since 2017年7月27日15:11:17
     */
    public static String decryptAESECBPKCS7(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] encryptedText;
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        Key key1 = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding"); // 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key1);
        encryptedText = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(encryptedText, "UTF-8");
    }

    /**
     * 生成用户Token的方法
     *
     * @return 生成好的Token
     * @author Jung
     * @since 2017年7月28日00:13:22
     */
    private static String generateToken() {
        return (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "");
    }

    /**
     * 生成系统Token的方法
     *
     * @return 生成好的Token
     * @author Jung
     * @since 2017年7月28日00:13:22
     */
    private static String generateSystemToken() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(new Date()) + (UUID.randomUUID().toString() + UUID.randomUUID().toString() +
                UUID.randomUUID().toString() + UUID.randomUUID().toString() +
                UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "");
    }

    /**
     * 加密用户密码
     *
     * @param phone            用户手机号
     * @param originalPassword 用户明文密码
     * @return 加密后的密码
     * @author Jung
     * @since 2017年12月13日13:57:48
     */
    public static String encryptUserPassword(String phone, String originalPassword) {
        return DigestUtils.md5DigestAsHex((phone + DigestUtils.md5DigestAsHex(originalPassword.getBytes())).getBytes());
    }

    /**
     * 获取有效期之后的时间
     *
     * @return 有效期之后的时间
     * @author Jung
     * @since 2017年7月28日00:13:33
     */
    private static Date getDayAfterExpireDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Constants.APP_TOKEN_EXPIRE_DAY);
        return calendar.getTime();
    }

    /**
     * 生成token的方法
     *
     * @param module 需要生成token的模块
     * @return 生成好的token，模块名+随机串
     */
    public static String generateToken(RedisKeys module) {
        return module.name().toLowerCase() + UUID.randomUUID().toString().replace("-", "");
    }

}
