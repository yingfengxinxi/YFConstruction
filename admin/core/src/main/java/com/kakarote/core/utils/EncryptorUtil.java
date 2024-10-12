package com.kakarote.core.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置文件加密辅助工具类
 */
public class EncryptorUtil {
    /**
     * 解密
     *
     * @param encryptor
     * @param ciphertext
     * @return
     */
    public static String decrypt(StandardPBEStringEncryptor encryptor, String ciphertext) {
        List<String> list = getValues(ciphertext);
        for (String value : list) {
            String text = value.replace("ENC(", "").replace(")", "");
            //解密
            String plaintext = encryptor.decrypt(text);
            ciphertext = ciphertext.replace(value, plaintext);
        }
        return ciphertext;
    }

    /**
     * 获取要解密的串
     *
     * @return
     */
    private static List<String> getValues(String value) {
        List<String> list = new ArrayList<>();
        Pattern datePattern = Pattern.compile("ENC\\(\\S+\\)");
        Matcher dateMatcher = datePattern.matcher(value);
        while (dateMatcher.find()) {
            list.add(dateMatcher.group());
        }
        return list;
    }

    /**
     * Jasypt加密 结果
     *
     * @param plaintext 需要加密字符串
     * @param salt      密码盐
     */
    public static void encryption(String plaintext, String salt) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //加密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setPassword(salt);//加密所需的salt(盐)
        config.setPoolSize(8);
        //应用配置
        encryptor.setConfig(config);
        //加密
        String ciphertext = encryptor.encrypt(plaintext);
        System.out.println(plaintext + " :" + ciphertext);
    }
}
