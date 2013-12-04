package com.jswidler.hotpgen;

import com.jswidler.hotpgen.app.HotpGenMain;

import java.security.GeneralSecurityException;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException {
        HotpGenMain app = new HotpGenMain(args);
        app.run();
    }
}
