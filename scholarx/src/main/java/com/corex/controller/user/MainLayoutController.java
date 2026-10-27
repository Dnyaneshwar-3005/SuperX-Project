package com.corex.controller.user;

import com.corex.util.SceneNavigator;
import com.corex.util.SessionManager;

public class MainLayoutController {

    /** Opens any page by name (used by the sidebar and the top bar). */
    public void openPage(String page) {
        SceneNavigator.go(page);
    }

    public String getStudentName() {
        return SessionManager.getStudentName();
    }

    public String getInitials() {
        return SessionManager.getInitials();
    }
}