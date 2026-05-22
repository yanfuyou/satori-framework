package com.satori.common.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author YanFuyou
 * @date 2024-10-26 23:38:53
 * @descraption 签名工具类
 */

@UtilityClass
public class SignUtils {

    /**
     * sha256-rsa 加签
     */
    public static String signSha256WithRsa(String privateKey, String stringData) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        privateKey = privateKey.replaceAll("\\s", "");
        Signature signature = Signature.getInstance("SHA256withRSA");
        byte[] decode = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode);
        KeyFactory rsa = KeyFactory.getInstance("RSA");
        PrivateKey privateKey1 = rsa.generatePrivate(pkcs8EncodedKeySpec);
        signature.initSign(privateKey1);
        signature.update(stringData.getBytes(StandardCharsets.UTF_8));
        byte[] signedBytes = signature.sign();
        return new String(Base64.getEncoder().encode(signedBytes), StandardCharsets.UTF_8);

    }
}
