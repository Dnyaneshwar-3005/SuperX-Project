package com.corex.view;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

/** Java-only role selection page. No FXML required. */
public class SignupPage {

    public enum Role {
        USER, ADMIN, COMPANY
    }

    private final Consumer<Role> roleSelected;

    /** Creates the page with no navigation action. */
    public SignupPage() {
        this(SignupPage::openDefaultRolePage);
    }

    /**
     * @param roleSelected invoked when a student, admin, or company card is chosen
     */
    public SignupPage(Consumer<Role> roleSelected) {
        this.roleSelected = roleSelected;
    }

    private static void openDefaultRolePage(Role role) {
        if (role == Role.USER && SplashScreen.primaryStage != null) {
            SplashScreen.primaryStage.setScene(new LoginPage().getLoginScene());
            SplashScreen.primaryStage.setTitle("ScholarX - User Login");
        }
    }

    public Scene getSignupScene() {
        BorderPane page = new BorderPane();
        page.setStyle("-fx-background-color: #F5F8FC;");
        page.setTop(createHeader());
        page.setCenter(createMainContent());
        page.setBottom(createFooter());

        return new Scene(page, 1350, 800);
    }

    private HBox createHeader() {
        StackPane logoMark = new StackPane(new Label("⌂"));
        logoMark.setPrefSize(40, 40);
        logoMark.setMaxSize(40, 40);
        logoMark.setStyle("-fx-background-color: #2457CC; -fx-background-radius: 10;");
        ((Label) logoMark.getChildren().get(0)).setStyle(
                "-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Text scholar = text("Scholar", 22, FontWeight.BOLD, Color.web("#12233D"));
        Text x = text("X", 22, FontWeight.BOLD, Color.web("#2860D7"));
        HBox brand = new HBox(logoMark, new HBox(scholar, x));
        brand.setSpacing(10);
        brand.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label motto = new Label("Your Education. Our Support.");
        motto.setStyle("-fx-text-fill: #3D4D66; -fx-font-size: 13px;");

        HBox header = new HBox(brand, spacer, motto);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 64, 18, 64));
        header.setStyle("-fx-background-color: white; -fx-border-color: transparent transparent #DDE5F0 transparent;");
        return header;
    }

    private VBox createMainContent() {
        Text welcome = text("Welcome to Scholar", 45, FontWeight.EXTRA_BOLD, Color.web("#10203A"));
        Text x = text("X", 45, FontWeight.EXTRA_BOLD, Color.web("#2860D7"));
        HBox heading = new HBox(welcome, x);
        heading.setAlignment(Pos.CENTER);

        Label description = new Label("Your complete platform for education loans, scholarships and student benefits.\n"
                + "Choose your role to get started.");
        description.setAlignment(Pos.CENTER);
        description.setStyle("-fx-text-alignment: center; -fx-text-fill: #4C5B73; -fx-font-size: 17px; -fx-line-spacing: 5px;");

        HBox cards = new HBox(24,
                createRoleCard("⌂", "#E6EEFF", "#2457CC", "User",
                        "Explore education loans, scholarships\nand student benefits in one place.",
                        "Continue as User  →", "#2457CC", Role.USER),
                createRoleCard("♢", "#DDF5F1", "#11847B", "Admin",
                        "Manage users, verify information and\nkeep the platform running smoothly.",
                        "Continue as Admin  →", "#11847B", Role.ADMIN),
                createRoleCard("▥", "#EEE8FF", "#6542D8", "Company",
                        "Partner with us to offer loan programs,\nscholarships and exclusive benefits for\nstudents.",
                        "Continue as Company  →", "#6542D8", Role.COMPANY));
        cards.setAlignment(Pos.CENTER);

        VBox main = new VBox(22, heading, description, cards);
        main.setAlignment(Pos.CENTER);
        main.setPadding(new Insets(42, 40, 44, 40));
        VBox.setMargin(cards, new Insets(32, 0, 0, 0));
        return main;
    }

    private VBox createRoleCard(String icon, String iconBackground, String iconColor, String title,
            String description, String buttonText, String buttonColor, Role role) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-text-fill: " + iconColor + "; -fx-font-size: 31px; -fx-font-weight: bold;");
        StackPane iconBox = new StackPane(iconLabel);
        iconBox.setPrefSize(76, 76);
        iconBox.setMaxSize(76, 76);
        iconBox.setStyle("-fx-background-color: " + iconBackground + "; -fx-background-radius: 20;");

        Label roleTitle = new Label(title);
        roleTitle.setStyle("-fx-text-fill: #13233D; -fx-font-size: 23px; -fx-font-weight: bold;");

        Label roleDescription = new Label(description);
        roleDescription.setAlignment(Pos.CENTER);
        roleDescription.setStyle("-fx-text-alignment: center; -fx-text-fill: #46556E; -fx-font-size: 14px; -fx-line-spacing: 4px;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button continueButton = new Button(buttonText);
        continueButton.setMaxWidth(Double.MAX_VALUE);
        continueButton.setPrefHeight(49);
        continueButton.setStyle("-fx-background-color: " + buttonColor + "; -fx-text-fill: white;"
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 9; -fx-cursor: hand;");
        continueButton.setOnAction(event -> roleSelected.accept(role));

        VBox card = new VBox(18, iconBox, roleTitle, roleDescription, spacer, continueButton);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35, 32, 31, 32));
        card.setPrefSize(330, 360);
        card.setMinSize(280, 340);
        card.setMaxSize(360, 380);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #DEE6F0;"
                + "-fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(28, 52, 85, 0.10), 18, 0, 0, 5);");
        return card;
    }

    private VBox createFooter() {
        HBox values = new HBox(150,
                footerValue("Trusted Platform", "Safe, secure and reliable"),
                footerValue("Wide Opportunities", "Loans, scholarships and more"),
                footerValue("Better Future", "For every student"));
        values.setAlignment(Pos.CENTER);
        values.setPadding(new Insets(22, 20, 22, 20));
        values.setStyle("-fx-background-color: white; -fx-border-color: #E1E8F2 transparent transparent transparent;");

        Label copyright = new Label("© 2026 ScholarX. All rights reserved.");
        copyright.setStyle("-fx-text-fill: #52617A; -fx-font-size: 13px;");
        StackPane copyrightBar = new StackPane(copyright);
        copyrightBar.setPadding(new Insets(17, 0, 17, 0));
        copyrightBar.setStyle("-fx-background-color: #F1F5FA; -fx-border-color: #E1E8F2 transparent transparent transparent;");

        return new VBox(values, copyrightBar);
    }

    private VBox footerValue(String title, String subtitle) {
        Label heading = new Label(title);
        heading.setStyle("-fx-text-fill: #19283E; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label detail = new Label(subtitle);
        detail.setStyle("-fx-text-fill: #4C5B73; -fx-font-size: 13px;");
        VBox value = new VBox(4, heading, detail);
        value.setAlignment(Pos.CENTER);
        return value;
    }

    private Text text(String value, double size, FontWeight weight, Color color) {
        Text text = new Text(value);
        text.setFont(Font.font("System", weight, size));
        text.setFill(color);
        return text;
    }
}
