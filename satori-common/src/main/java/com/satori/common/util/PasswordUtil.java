package com.satori.common.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YanFuYou
 * @date 2024/02/22 下午 10:45
 */

@UtilityClass
public class PasswordUtil {


    public static Map<String, String> encrypt(String password) {
        HashMap<String, String> result = new HashMap<>();
        String salt = RandomUtil.randomString(16);
        result.put("salt", salt);
        String confusion = confusion(password, salt);
        result.put("pwd", confusion);
        return result;
    }

    public static String encrypt(String password, String salt) {
        return confusion(password, salt);
    }

    public boolean equalPassword(String source, String salt, String target) {
        String encrypt = encrypt(source, salt);
        return encrypt.equals(target);
    }

    private static String confusion(String pwd, String salt) {
        char[] charArray = salt.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (charArray[i] ^ i);
        }
        String tmp = new String(charArray);
        String source = tmp + tmp + pwd + tmp;
        return SecureUtil.md5(source);
    }
}
