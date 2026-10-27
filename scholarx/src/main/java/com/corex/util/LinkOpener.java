package com.corex.util;

import java.awt.Desktop;
import java.net.URI;

public class LinkOpener {

    /** Opens the link in the default browser. Returns false if it could not be opened. */
    public static boolean open(String url) {
        if (url == null || !url.startsWith("https://")) {
            return false;
        }
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}