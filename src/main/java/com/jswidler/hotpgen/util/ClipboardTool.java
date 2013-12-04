package com.jswidler.hotpgen.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author <a href="mailto:jswidler@gmail.com">Jesse Swidler</a>
 */
public class ClipboardTool {
    public static void copyTo(String s) {
        StringSelection stringSelection = new StringSelection(s);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }
}
