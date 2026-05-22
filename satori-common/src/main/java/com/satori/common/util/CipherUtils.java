package com.satori.common.util;

import lombok.experimental.UtilityClass;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

@UtilityClass
public class CipherUtils {

    public static KeyPair genKeyPair() throws GeneralSecurityException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        return keyPairGen.generateKeyPair();
    }

    public static void main(String[] args) throws GeneralSecurityException {
        KeyPair keyPair = CipherUtils.genKeyPair();
        String reqPub = java.util.Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String reqPri = java.util.Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        System.out.println(reqPub);
        System.out.println(reqPri);
    }
}
