package com.corex.controller.user;

import com.corex.util.SceneNavigator;
import com.corex.util.SessionManager;

public class DashboardController {

    public void openPage(String page) {
        SceneNavigator.go(page);
    }

    public String getStudentName() {
        return SessionManager.getStudentName();
    }
}