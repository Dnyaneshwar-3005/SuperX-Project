package com.corex.controller.user;

import com.corex.config.AppConstants;
import com.corex.util.SceneNavigator;

public class SignUpController {

    /**
     * Checks the form. Returns an error message if something is wrong,
     * or null if everything is fine (and then opens the Login page).
     */
    public String createAccount(String name, String email, String phone,
                                String password, String confirm, boolean agreed) {

        if (name == null || name.isBlank()) {
            return "Please enter your full name.";
        }
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
            return "Please enter a valid email address.";
        }

        String digits = (phone == null) ? "" : phone.replaceAll("[^0-9]", "");
        if (digits.length() < 10) {
            return "Please enter a valid phone number (at least 10 digits).";
        }

        if (password == null || password.length() < 8) {
            return "Your password must be at least 8 characters long.";
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            return "Your password must contain both letters and numbers.";
        }
        if (!password.equals(confirm)) {
            return "The two passwords do not match.";
        }
        if (!agreed) {
            return "Please accept the Terms of Use and Privacy Policy.";
        }

        // TODO: the account will be saved in Firebase in a later step.
        SceneNavigator.go(AppConstants.PAGE_LOGIN);
        return null;
    }

    public void backToLogin() {
        SceneNavigator.go(AppConstants.PAGE_LOGIN);
    }
}