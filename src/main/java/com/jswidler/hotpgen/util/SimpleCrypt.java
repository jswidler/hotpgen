package com.jswidler.hotpgen.util;

import com.google.common.base.Charsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * @author <a href="mailto:jswidler@gmail.com">Jesse Swidler</a>
 */
public class SimpleCrypt {

    private static byte[] IV = {66,43,41,102,54,-57,-10,115,-60,-36,51,-52,35,-50,-113,-64};

    public static String encrypt(String key, String value) throws GeneralSecurityException {
        byte[] raw = key.getBytes(Charsets.US_ASCII);
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV));
        byte[] bytes = cipher.doFinal(value.getBytes(Charsets.UTF_8));
        return Base32Codec.encode(bytes);
    }

    public static String decrypt(String key, String encrypted) throws GeneralSecurityException {
        byte[] raw = key.getBytes(Charsets.US_ASCII);
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(IV));
        byte[] original = cipher.doFinal(Base32Codec.decode(encrypted));

        return new String(original, Charsets.UTF_8);
    }
}