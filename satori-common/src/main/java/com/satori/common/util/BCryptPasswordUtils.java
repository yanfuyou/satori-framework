package com.satori.common.util;

import cn.hutool.crypto.digest.BCrypt;

import java.security.SecureRandom;

/**
 * @author yfy
 */
public class BCryptPasswordUtils {

    /**
     * 随机密钥长度
     */
    private static final int SEED_LENGTH = 32;

    public static String getSalt() {
        SecureRandom secureRandom = new SecureRandom(SecureRandom.getSeed(SEED_LENGTH));
        return BCrypt.gensalt(12, secureRandom);
    }

    /**
     * 编码
     *
     * @param rawPassword 未经加工的密码
     * @return 完整编码的密码
     */
    public static String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), getSalt());
    }

    /**
     * 编码
     *
     * @param rawPassword 未经加工的密码
     * @param salt        盐
     * @return 完整编码的密码去盐
     */
    public static String encode(CharSequence rawPassword, String salt) {
        String password = BCrypt.hashpw(rawPassword.toString(), salt);
        return password.substring(salt.length());
    }

    /**
     * 匹配
     *
     * @param rawPassword     未经加工的密码
     * @param encodedPassword 完整编码的密码
     * @return 匹配结果
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }

    /**
     * 匹配
     *
     * @param rawPassword 未经加工的密码
     * @param slat        盐
     * @param subPassword 完整编码的密码去盐
     * @return 匹配结果
     */
    public static boolean matches(CharSequence rawPassword, String slat, String subPassword) {
        String encodedPassword = slat.concat(subPassword);
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
