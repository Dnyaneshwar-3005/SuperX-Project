package com.corex.controller.user;

import com.corex.config.AppConstants;
import com.corex.util.SceneNavigator;

public class LoginController {

    /**
     * Checks the details. Returns an error message if something is wrong,
     * or null if everything is fine (and then opens the Dashboard).
     */
    public String signIn(String email, String password) {
        if (email == null || email.isBlank()) {
            return "Please enter your email address.";
        }
        if (!email.contains("@")) {
            return "Please enter a valid email address.";
        }
        if (password == null || password.isEmpty()) {
            return "Please enter your password.";
        }

        // TODO: real Firebase sign-in will be added in a later step.
        SceneNavigator.go(AppConstants.PAGE_DASHBOARD);
        return null;
    }

    public void openSignUp() {
        SceneNavigator.go(AppConstants.PAGE_SIGNUP);
    }

    public void backToWelcome() {
        SceneNavigator.go(AppConstants.PAGE_WELCOME);
    }
}
