package com.jswidler.hotpgen.util;

import com.google.common.base.Charsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:jswidler@gmail.com">Jesse Swidler</a>
 */
public class SimpleCrypt {

    private static byte[] IV = {66,43,41,102,54,-57,-10,115,-60,-36,51,-52,35,-50,-113,-64};

    public static String encrypt(byte[] key, String value) throws GeneralSecurityException {
        if (key.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV));
        byte[] bytes = cipher.doFinal(value.getBytes(Charsets.UTF_8));
        return Base32Codec.encode(bytes);
    }

    public static String decrypt(byte[] key, String encrypted) throws GeneralSecurityException {
        if (key.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(IV));
        byte[] original = cipher.doFinal(Base32Codec.decode(encrypted));

        return new String(original, Charsets.UTF_8);
    }

    public static byte[] sha1Hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(s.getBytes(Charsets.UTF_8));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("Unable to make hash", e);
        }
    }

    /**
     * Returns a 16 byte array from one or more Strings which can be used as a key for encryption.
     *
     * At least one of the Strings must not be null or empty.  The Strings will be hashed with SHA-1 and the first 16
     * bytes will be XOR'd together for the result.
     *
     * @param keys a list of Strings to create a key from
     * @return 16 byte array from the sha1 hash of both strings.
     */
    public static byte[] makeKeyBytes(String... keys) {
        byte[] keyBytes = new byte[16];
        boolean hasKey = false;

        for (String key : keys) {
            if (key == null || key.isEmpty()) {
                continue;
            }
            hasKey = true;
            byte[] hash = sha1Hash(key);
            for (int i = 0; i < 16; i++) {
                keyBytes[i] = (byte) (0xff & (keyBytes[i] ^ hash[i]));
            }
        }

        if (!hasKey) {
            throw new SecurityException("an attempt to make an encryption key without data was made");
        }

        return keyBytes;
    }
}