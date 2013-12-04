package com.jswidler.hotpgen.app;

import com.jswidler.hotpgen.util.ClipboardTool;
import com.jswidler.hotpgen.util.HmacOneTimePassword;

import java.security.GeneralSecurityException;

/**
 * @author <a href="mailto:jswidler@gmail.com">Jesse Swidler</a>
 */
public class HotpGenMain {

    private final AppProperties properties;

    public HotpGenMain(String[] args) throws GeneralSecurityException {
        properties = new AppProperties();

        if (args.length == 1) {
            properties.setKey(args[0]);
            properties.save();
        }
    }

    public void run() throws GeneralSecurityException {
        if (!properties.hasKey()) {
            throw new IllegalStateException("A key must be provided");
        }

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
