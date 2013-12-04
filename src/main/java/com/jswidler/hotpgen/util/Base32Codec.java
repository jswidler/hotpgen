package com.jswidler.hotpgen.util;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

/**
 * @author <a href="mailto:jswidler@gmail.com">Jesse Swidler</a>
 */
public class Base32Codec {

    public static String encode(String str) {
        return BaseEncoding.base32().encode(str.getBytes(Charsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        return BaseEncoding.base32().encode(bytes);
    }

    public static byte[] decode(String s) {
        return BaseEncoding.base32().decode(s);
    }
}
