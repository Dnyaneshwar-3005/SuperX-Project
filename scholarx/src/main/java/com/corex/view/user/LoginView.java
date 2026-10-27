package com.corex.view.user;

import com.corex.controller.user.LoginController;
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

public class LoginView extends HBox {

    private final LoginController controller = new LoginController();

    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label errorLabel = UiFactory.text("", 14, false, "#B42318");

    public LoginView() {
        VBox left = buildBrandPanel();
        StackPane right = buildFormPanel();

        // both halves share the width equally
        left.setPrefWidth(520);
        right.setPrefWidth(520);
        left.setMaxWidth(Double.MAX_VALUE);
        right.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        getChildren().addAll(left, right);
        showError(null);
    }

    // =====================================================
    //  LEFT SIDE: navy brand panel
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

        Label headline = wrapped("Plan your education with confidence.", 42, true, "white");
        Label sub = wrapped("One place to compare education loans, find scholarships "
                + "and discover student benefits.", 17, false, "#C7D4E3");

        VBox points = new VBox(14,
                point("Compare education loans from leading banks", true),
                point("Discover scholarships that match your profile", true),
                point("Find offers and benefits made for students", false));

        VBox middle = new VBox(28, headline, sub, points);
        middle.setMaxWidth(520);

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

    /** One line of the benefits list, with a thin line below it. */
    private VBox point(String text, boolean withLine) {
        VBox box = new VBox(wrapped(text, 15, false, "#E3EAF2"));
        if (withLine) {
            box.setStyle("-fx-padding:0 0 14 0;"
                    + "-fx-border-color: transparent transparent rgba(255,255,255,0.14) transparent;"
                    + "-fx-border-width:0 0 1 0;");
        }
        return box;
    }

    // =====================================================
    //  RIGHT SIDE: the sign-in form
    // =====================================================
    private StackPane buildFormPanel() {

        // "Back to role selection" link
        Button back = linkButton("\u2190 Back to role selection", Styles.MUTED, 14, false);
        back.setOnAction(e -> controller.backToWelcome());

        // Badge + heading + text
        Label badge = UiFactory.text("USER LOGIN", 12, true, Styles.BLUE);
        badge.setStyle(badge.getStyle()
                + "-fx-background-color:#E6EEFB; -fx-background-radius:6; -fx-padding:6 12;");
        Label title = UiFactory.text("Sign in to your account", 30, true, Styles.TEXT);
        Label intro = wrapped("Enter your details to continue to your dashboard.", 15, false, Styles.MUTED);
        VBox heading = new VBox(10, badge, title, intro);

        // Email
        emailField.setPromptText("name@example.com");
        styleField(emailField);
        VBox emailBox = new VBox(8, UiFactory.text("Email address", 14, true, Styles.TEXT), emailField);

        // Password (with "Forgot password?" on the right)
        passwordField.setPromptText("Enter your password");
        styleField(passwordField);
        passwordField.setOnAction(e -> doSignIn());   // pressing Enter also signs in

        Button forgot = linkButton("Forgot password?", Styles.BLUE, 14, false);
        forgot.setOnAction(e -> { /* password reset will be added later */ });

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);
        HBox passwordHeader = new HBox(UiFactory.text("Password", 14, true, Styles.TEXT), gap, forgot);
        passwordHeader.setAlignment(Pos.CENTER_LEFT);
        VBox passwordBox = new VBox(8, passwordHeader, passwordField);

        // Keep me signed in
        CheckBox keep = new CheckBox("Keep me signed in on this device");
        keep.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-text-fill:" + Styles.MUTED + ";");

        // Error message (hidden until needed)
        errorLabel.setWrapText(true);

        // Sign in button
        Button signIn = UiFactory.filledButton("Sign in", Styles.BLUE);
        signIn.setPrefHeight(52);
        signIn.setOnAction(e -> doSignIn());

        VBox fields = new VBox(20, emailBox, passwordBox, keep, errorLabel, signIn);

        // Bottom: "New to ScholarX? Create an account"
        Button create = linkButton("Create an account", Styles.BLUE, 15, true);
        create.setOnAction(e -> controller.openSignUp());
        HBox bottomRow = new HBox(6, UiFactory.text("New to ScholarX?", 15, false, Styles.MUTED), create);
        bottomRow.setAlignment(Pos.CENTER);
        VBox bottom = new VBox(bottomRow);
        bottom.setStyle("-fx-padding:24 0 0 0;"
                + "-fx-border-color:" + Styles.BORDER + " transparent transparent transparent;"
                + "-fx-border-width:1 0 0 0;");

        VBox form = new VBox(28, back, heading, fields, bottom);
        form.setMaxWidth(440);

        StackPane panel = new StackPane(form);
        panel.setPadding(new Insets(56, 48, 56, 48));
        panel.setStyle("-fx-background-color:" + Styles.BG + ";");
        return panel;
    }

    // =====================================================
    //  Actions
    // =====================================================
    private void doSignIn() {
        String error = controller.signIn(emailField.getText().trim(), passwordField.getText());
        showError(error);
    }

    private void showError(String message) {
        boolean hasMessage = (message != null);
        errorLabel.setText(hasMessage ? message : "");
        errorLabel.setVisible(hasMessage);
        errorLabel.setManaged(hasMessage);   // hidden label takes no space
    }

    // =====================================================
    //  Small helpers
    // =====================================================
    private void styleField(TextField field) {
        field.setPrefHeight(50);
        field.setStyle(Styles.FONT + "-fx-font-size:15px; -fx-text-fill:" + Styles.TEXT + ";"
                + "-fx-background-color:white; -fx-background-radius:10;"
                + "-fx-border-color:#C9D2DE; -fx-border-radius:10; -fx-padding:0 16;");
    }

    /** A label that wraps onto several lines and stays left-aligned. */
    private Label wrapped(String text, int size, boolean bold, String color) {
        Label label = UiFactory.text(text, size, bold, color);
        label.setWrapText(true);
        return label;
    }

    /** A button that looks like a text link. */
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