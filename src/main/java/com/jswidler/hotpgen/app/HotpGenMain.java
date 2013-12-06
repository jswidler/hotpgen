package com.jswidler.hotpgen.app;

import com.jswidler.hotpgen.util.ClipboardTool;
import com.jswidler.hotpgen.util.HmacOneTimePassword;

import java.security.GeneralSecurityException;

/**
 * @author <a href="mailto:jswidler@gmail.com">Jesse Swidler</a>
 */
public class HotpGenMain {

    private final AppProperties properties;

    public HotpGenMain(String key, String password) throws GeneralSecurityException {
        properties = new AppProperties(password);

        if (key != null) {
            properties.setKey(key);
            properties.savePropertyFile();
        }
    }

    public void run() throws GeneralSecurityException {
        String token = HmacOneTimePassword.generateCode(
                properties.getKey(),
                properties.getAlgorithm(),
                properties.getPeriod(),
                properties.getDigits()
        );

        ClipboardTool.copyTo(token);
        System.out.println(token + " - (copied to clipboard)");
    }
}
