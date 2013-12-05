package com.jswidler.hotpgen.util;

import com.google.common.base.Strings;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class HmacOneTimePassword {

    public static final String DEFAULT_ALGORITHM = "HMACSHA1";
    public static final int DEFAULT_PERIOD = 30;
    public static final int DEFAULT_DIGITS = 6;

    public static String generateCode(String secretKey) {
        return generateCode(secretKey, DEFAULT_ALGORITHM, DEFAULT_PERIOD, DEFAULT_DIGITS);
    }

    public static String generateCode(String secretKey, String algorithm, int period, int digits) {
        final byte[] keyBytes = Base32Codec.decode(secretKey);
        int modulo = (int) Math.pow(10, digits);
        long interval = new Date().getTime() / (period * 1000);

        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(keyBytes, ""));
            byte[] intervalBytes = ByteBuffer.allocate(8).putLong(interval).array();

            // Create the hash of the secret key + interval
            byte[] hash = mac.doFinal(intervalBytes); // 20 byte hash

            // Compute the offset by getting the 4 lowest order bits from the last byte of the hash
            int offset = hash[hash.length - 1] & 0xF; // Value between 0 & 15

            // Get a positive integer value from the hash comprised of the 4 bytes starting at offset. Since
            // 0 <= offset <= 15 & hash is 20 bytes we are guaranteed not to buffer underflow. We mask to guarantee
            // a positive value
            int truncatedHash = ByteBuffer.wrap(hash, offset, 4).getInt() & 0x7FFFFFFF;

            // Extract the correct number of digits
            int rawPasscode = truncatedHash % modulo;

            // Convert result to a string left padded with 0's
            return Strings.padStart(Integer.toString(rawPasscode), digits, '0');
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Couldn't find the " + algorithm + " algorithm", e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }
}
