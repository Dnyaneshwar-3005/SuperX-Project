package com.corex.view;

import java.util.function.BiConsumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/** Java-only ScholarX user login page. */
public class LoginPage {

    private final BiConsumer<String, String> loginAction;

    /** Creates the page without connecting it to Firebase yet. */
    public LoginPage() {
        this((email, password) -> { });
    }

    /**
     * @param loginAction receives the entered email and password when Sign in is pressed
     */
    public LoginPage(BiConsumer<String, String> loginAction) {
        this.loginAction = loginAction;
    }

    public Scene getLoginScene() {
        HBox page = new HBox(createInformationPanel(), createFormPanel());
        HBox.setHgrow(page.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(page.getChildren().get(1), Priority.ALWAYS);
        return new Scene(page, 1350, 800);
    }

    private BorderPane createInformationPanel() {
        BorderPane left = new BorderPane();
        left.setPrefWidth(675);
        left.setStyle("-fx-background-color: #112F52;");
        left.setPadding(new Insets(56, 72, 52, 72));

        left.setTop(createBrand());

        Label heading = new Label("Plan your education with\nconfidence.");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold; -fx-line-spacing: 2px;");

        Label description = new Label("One place to compare education loans, find scholarships and\n"
                + "discover student benefits.");
        description.setStyle("-fx-text-fill: #C9D8EB; -fx-font-size: 17px; -fx-line-spacing: 5px;");

        VBox benefits = new VBox(0,
                benefit("Compare education loans from leading banks"),
                benefit("Discover scholarships that match your profile"),
                benefit("Find offers and benefits made for students"));

        VBox center = new VBox(32, heading, description, benefits);
        center.setAlignment(Pos.CENTER_LEFT);
        left.setCenter(center);

        Label copyright = new Label("© 2026 ScholarX. All rights reserved.");
        copyright.setStyle("-fx-text-fill: #B5C8DD; -fx-font-size: 13px;");
        left.setBottom(copyright);
        return left;
    }

    private HBox createBrand() {
        Label cap = new Label("⌂");
        cap.setStyle("-fx-text-fill: #102F52; -fx-font-size: 25px; -fx-font-weight: bold;");
        StackPane mark = new StackPane(cap);
        mark.setPrefSize(40, 40);
        mark.setMaxSize(40, 40);
        mark.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Text scholar = brandText("Scholar", Color.WHITE);
        Text x = brandText("X", Color.web("#5B8CFF"));
        HBox wordmark = new HBox(scholar, x);
        wordmark.setAlignment(Pos.CENTER_LEFT);

        HBox brand = new HBox(11, mark, wordmark);
        brand.setAlignment(Pos.CENTER_LEFT);
        return brand;
    }
    
    private Label benefit(String text) {
        Label benefit = new Label(text);
        benefit.setMaxWidth(Double.MAX_VALUE);
        benefit.setPadding(new Insets(15, 0, 15, 0));
        benefit.setStyle("-fx-text-fill: #F0F5FC; -fx-font-size: 15px;"
                + "-fx-border-color: transparent transparent #294867 transparent;");
        return benefit;
    }

    private BorderPane createFormPanel() {
        BorderPane right = new BorderPane();
        right.setPrefWidth(675);
        right.setStyle("-fx-background-color: #F5F8FC;");
        right.setPadding(new Insets(65, 110, 58, 140));

        Hyperlink back = new Hyperlink("← Back to role selection");
        back.setStyle("-fx-text-fill: #46556E; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: transparent;");
        back.setOnAction(event -> openRoleSelection());
        right.setTop(back);

        Label badge = new Label("USER LOGIN");
        badge.setPadding(new Insets(6, 12, 6, 12));
        badge.setStyle("-fx-text-fill: #2457CC; -fx-font-size: 12px; -fx-font-weight: bold;"
                + "-fx-background-color: #E6EEFF; -fx-background-radius: 6;");

        Label heading = new Label("Sign in to your account");
        heading.setStyle("-fx-text-fill: #10203A; -fx-font-size: 30px; -fx-font-weight: bold;");

        Label subtitle = new Label("Enter your details to continue to your dashboard.");
        subtitle.setStyle("-fx-text-fill: #4C5B73; -fx-font-size: 15px;");

        Label emailLabel = formLabel("Email address");
        TextField emailField = new TextField();
        emailField.setPromptText("name@example.com");
        styleField(emailField);

        HBox passwordRow = new HBox(formLabel("Password"));
        Region passwordSpacer = new Region();
        HBox.setHgrow(passwordSpacer, Priority.ALWAYS);
        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setStyle("-fx-text-fill: #2457CC; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: transparent;");
        passwordRow.getChildren().addAll(passwordSpacer, forgotPassword);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        styleField(passwordField);

        CheckBox keepSignedIn = new CheckBox("Keep me signed in on this device");
        keepSignedIn.setStyle("-fx-text-fill: #4C5B73; -fx-font-size: 14px;");

        Button signIn = new Button("Sign in");
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setPrefHeight(52);
        signIn.setStyle("-fx-background-color: #2457CC; -fx-text-fill: white; -fx-font-size: 15px;"
                + "-fx-font-weight: bold; -fx-background-radius: 9; -fx-cursor: hand;");
        signIn.setOnAction(event -> loginAction.accept(emailField.getText().trim(), passwordField.getText()));

        Label divider = new Label();
        divider.setMaxWidth(Double.MAX_VALUE);
        divider.setStyle("-fx-border-color: #DCE4EF transparent transparent transparent;");

        Label accountText = new Label("New to ScholarX?");
        accountText.setStyle("-fx-text-fill: #4C5B73; -fx-font-size: 14px;");
        Hyperlink createAccount = new Hyperlink("Create an account");
        createAccount.setStyle("-fx-text-fill: #2457CC; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: transparent;");
        createAccount.setOnAction(event -> { /* Connect the User registration page here. */ });
        HBox accountLine = new HBox(4, accountText, createAccount);
        accountLine.setAlignment(Pos.CENTER);

        VBox form = new VBox(17, badge, heading, subtitle, emailLabel, emailField,
                passwordRow, passwordField, keepSignedIn, signIn, divider, accountLine);
        form.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(badge, new Insets(5, 0, 0, 0));
        VBox.setMargin(emailLabel, new Insets(12, 0, -9, 0));
        VBox.setMargin(passwordRow, new Insets(2, 0, -9, 0));
        VBox.setMargin(signIn, new Insets(13, 0, 10, 0));
        VBox.setMargin(divider, new Insets(0, 0, 6, 0));
        right.setCenter(form);

        return right;
    }

    private void openRoleSelection() {
        if (SplashScreen.primaryStage != null) {
            SplashScreen.primaryStage.setScene(new SignupPage().getSignupScene());
            SplashScreen.primaryStage.setTitle("ScholarX - Choose Your Role");
        }
    }

    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #16253B; -fx-font-size: 13px; -fx-font-weight: bold;");
        return label;
    }

    private void styleField(TextField field) {
        field.setPrefHeight(50);
        field.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-color: #C8D4E3;"
                + "-fx-border-radius: 9; -fx-font-size: 14px; -fx-padding: 0 15;");
    }

    private Text brandText(String value, Color color) {
        Text text = new Text(value);
        text.setFill(color);
        text.setFont(Font.font("System", FontWeight.BOLD, 24));
        return text;
    }
}