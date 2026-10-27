package com.corex.view.user;

import com.corex.controller.user.SignUpController;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class SignUpView extends HBox {

    private final SignUpController controller = new SignUpController();

    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmField = new PasswordField();
    private final CheckBox termsBox = new CheckBox("I agree to the Terms of Use and Privacy Policy.");
    private final Label errorLabel = UiFactory.text("", 14, false, "#B42318");

    public SignUpView() {
        VBox left = buildBrandPanel();
        StackPane right = buildFormPanel();

        left.setPrefWidth(480);
        right.setPrefWidth(560);
        left.setMaxWidth(Double.MAX_VALUE);
        right.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        getChildren().addAll(left, right);
        showError(null);
    }

    // =====================================================
    //  LEFT SIDE: navy panel with the 3 steps
    // =====================================================
    private VBox buildBrandPanel() {
        StackPane logo = new StackPane(icon(
                "M2 9l10-5 10 5-10 5z M6 11.5V16c0 1.4 2.7 3 6 3s6-1.6 6-3v-4.5",
                Color.web(Styles.NAVY), 0.9));
        logo.setMinSize(40, 40);
        logo.setMaxSize(40, 40);
        logo.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        HBox brand = new HBox(12, logo, UiFactory.text("ScholarX", 24, true, "white"));
        brand.setAlignment(Pos.CENTER_LEFT);

        Label headline = wrapped("Create your student account.", 40, true, "white");
        Label sub = wrapped("It takes a minute. Once you are in, you can explore loans, "
                + "scholarships and student benefits.", 17, false, "#C7D4E3");

        VBox steps = new VBox(18,
                step("1", "Enter your basic details", true),
                step("2", "Sign in to your account", false),
                step("3", "Start exploring your options", false));

        VBox middle = new VBox(28, headline, sub, steps);
        middle.setMaxWidth(480);

        Label footer = UiFactory.text("\u00A9 2026 ScholarX. All rights reserved.", 13, false, "#9FB2C8");

        Region spaceTop = new Region();
        Region spaceBottom = new Region();
        VBox.setVgrow(spaceTop, Priority.ALWAYS);
        VBox.setVgrow(spaceBottom, Priority.ALWAYS);

        VBox panel = new VBox(brand, spaceTop, middle, spaceBottom, footer);
        panel.setPadding(new Insets(56, 72, 56, 72));
        panel.setStyle("-fx-background-color:" + Styles.NAVY + ";");
        return panel;
    }

    /** One numbered step. The first one is filled white, the others are outlined. */
    private HBox step(String number, String text, boolean active) {
        Label num = UiFactory.text(number, 14, true, active ? Styles.NAVY : "#E3EAF2");
        StackPane circle = new StackPane(num);
        circle.setMinSize(30, 30);
        circle.setMaxSize(30, 30);
        circle.setStyle(active
                ? "-fx-background-color:white; -fx-background-radius:15;"
                : "-fx-border-color:#6F89A6; -fx-border-radius:15; -fx-background-radius:15;");

        Label label = wrapped(text, 15, false, "#E3EAF2");
        HBox row = new HBox(16, circle, label);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // =====================================================
    //  RIGHT SIDE: the sign-up form
    // =====================================================
    private StackPane buildFormPanel() {

        Button back = linkButton("\u2190 Back to login", Styles.MUTED, 14, false);
        back.setOnAction(e -> controller.backToLogin());

        Label badge = UiFactory.text("USER SIGN UP", 12, true, Styles.BLUE);
        badge.setStyle(badge.getStyle()
                + "-fx-background-color:#E6EEFB; -fx-background-radius:6; -fx-padding:6 12;");
        Label title = UiFactory.text("Create an account", 30, true, Styles.TEXT);
        Label intro = wrapped("Fill in the details below to get started.", 15, false, Styles.MUTED);
        VBox heading = new VBox(10, badge, title, intro);

        // Full name
        nameField.setPromptText("Enter your full name");
        styleField(nameField);
        VBox nameBox = field("Full name", nameField);

        // Email and phone, side by side
        emailField.setPromptText("name@example.com");
        styleField(emailField);
        phoneField.setPromptText("Enter your phone number");
        styleField(phoneField);
        VBox emailBox = field("Email address", emailField);
        VBox phoneBox = field("Phone number", phoneField);
        HBox.setHgrow(emailBox, Priority.ALWAYS);
        HBox.setHgrow(phoneBox, Priority.ALWAYS);
        emailBox.setPrefWidth(200);
        phoneBox.setPrefWidth(200);
        emailBox.setMaxWidth(Double.MAX_VALUE);
        phoneBox.setMaxWidth(Double.MAX_VALUE);
        HBox contactRow = new HBox(18, emailBox, phoneBox);

        // Password with hint
        passwordField.setPromptText("Create a password");
        styleField(passwordField);
        Label hint = wrapped("Use at least 8 characters, with letters and numbers.", 13, false, "#5B6676");
        VBox passwordBox = field("Password", passwordField);
        passwordBox.getChildren().add(hint);

        // Confirm password
        confirmField.setPromptText("Re-enter your password");
        styleField(confirmField);
        confirmField.setOnAction(e -> doCreateAccount());   // Enter key also submits
        VBox confirmBox = field("Confirm password", confirmField);

        // Terms checkbox
        termsBox.setWrapText(true);
        termsBox.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-text-fill:" + Styles.MUTED + ";");

        errorLabel.setWrapText(true);

        Button createButton = UiFactory.filledButton("Create account", Styles.BLUE);
        createButton.setPrefHeight(52);
        createButton.setOnAction(e -> doCreateAccount());

        VBox fields = new VBox(18, nameBox, contactRow, passwordBox, confirmBox,
                termsBox, errorLabel, createButton);

        // Bottom: "Already have an account? Sign in"
        Button signIn = linkButton("Sign in", Styles.BLUE, 15, true);
        signIn.setOnAction(e -> controller.backToLogin());
        HBox bottomRow = new HBox(6,
                UiFactory.text("Already have an account?", 15, false, Styles.MUTED), signIn);
        bottomRow.setAlignment(Pos.CENTER);
        VBox bottom = new VBox(bottomRow);
        bottom.setStyle("-fx-padding:22 0 0 0;"
                + "-fx-border-color:" + Styles.BORDER + " transparent transparent transparent;"
                + "-fx-border-width:1 0 0 0;");

        VBox form = new VBox(24, back, heading, fields, bottom);
        form.setMaxWidth(480);

        StackPane panel = new StackPane(form);
        panel.setPadding(new Insets(48));
        panel.setStyle("-fx-background-color:" + Styles.BG + ";");
        return panel;
    }

    // =====================================================
    //  Actions
    // =====================================================
    private void doCreateAccount() {
        String error = controller.createAccount(
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                passwordField.getText(),
                confirmField.getText(),
                termsBox.isSelected());
        showError(error);
    }

    private void showError(String message) {
        boolean hasMessage = (message != null);
        errorLabel.setText(hasMessage ? message : "");
        errorLabel.setVisible(hasMessage);
        errorLabel.setManaged(hasMessage);
    }

    // =====================================================
    //  Small helpers
    // =====================================================
    /** A label above an input box. */
    private VBox field(String labelText, TextField input) {
        return new VBox(8, UiFactory.text(labelText, 14, true, Styles.TEXT), input);
    }

    private void styleField(TextField field) {
        field.setPrefHeight(50);
        field.setStyle(Styles.FONT + "-fx-font-size:15px; -fx-text-fill:" + Styles.TEXT + ";"
                + "-fx-background-color:white; -fx-background-radius:10;"
                + "-fx-border-color:#C9D2DE; -fx-border-radius:10; -fx-padding:0 16;");
    }

    private Label wrapped(String text, int size, boolean bold, String color) {
        Label label = UiFactory.text(text, size, bold, color);
        label.setWrapText(true);
        return label;
    }

    private Button linkButton(String text, String color, int size, boolean bold) {
        Button button = new Button(text);
        button.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0;"
                + "-fx-cursor:hand; -fx-text-fill:" + color + ";"
                + "-fx-font-size:" + size + "px; -fx-font-weight:" + (bold ? "bold" : "normal") + ";");
        return button;
    }

    private SVGPath icon(String path, Color color, double scale) {
        SVGPath svg = new SVGPath();
        svg.setContent(path);
        svg.setFill(null);
        svg.setStroke(color);
        svg.setStrokeWidth(1.7);
        svg.setStrokeLineCap(StrokeLineCap.ROUND);
        svg.setStrokeLineJoin(StrokeLineJoin.ROUND);
        svg.setScaleX(scale);
        svg.setScaleY(scale);
        return svg;
    }
}