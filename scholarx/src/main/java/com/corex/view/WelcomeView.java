package com.corex.view;

import com.corex.config.AppConstants;
import com.corex.util.SceneNavigator;
import com.corex.util.UiFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class WelcomeView extends VBox {

    public WelcomeView() {
        setStyle("-fx-background-color:" + Styles.BG + ";");

        VBox main = buildMain();
        VBox.setVgrow(main, Priority.ALWAYS);   // main area takes the free space

        getChildren().addAll(buildHeader(), main, buildHighlights(), buildFooter());
    }

    // ---------- Header: logo on the left, tagline on the right ----------
    private HBox buildHeader() {
        StackPane logo = new StackPane(icon(
                "M2 9l10-5 10 5-10 5z M6 11.5V16c0 1.4 2.7 3 6 3s6-1.6 6-3v-4.5",
                Color.WHITE, 0.9));
        logo.setMinSize(40, 40);
        logo.setMaxSize(40, 40);
        logo.setStyle("-fx-background-color:" + Styles.BLUE + "; -fx-background-radius:10;");

        HBox brand = new HBox(0, UiFactory.text("Scholar", 24, true, Styles.TEXT),
                                 UiFactory.text("X", 24, true, Styles.BLUE));
        brand.setAlignment(Pos.CENTER);

        HBox left = new HBox(12, logo, brand);
        left.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(left, spacer,
                UiFactory.text(AppConstants.TAGLINE, 14, false, Styles.MUTED));
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 64, 0, 64));
        header.setPrefHeight(84);
        header.setMinHeight(84);
        header.setStyle("-fx-background-color:white;"
                + "-fx-border-color: transparent transparent " + Styles.BORDER + " transparent;"
                + "-fx-border-width: 0 0 1 0;");
        return header;
    }

    // ---------- Main: title, intro text and the three role cards ----------
    private VBox buildMain() {
        HBox title = new HBox(0,
                UiFactory.text("Welcome to Scholar", 48, true, Styles.TEXT),
                UiFactory.text("X", 48, true, Styles.BLUE));
        title.setAlignment(Pos.CENTER);

        Label intro = UiFactory.paragraph(
                "Your complete platform for education loans, scholarships and student benefits. "
                + "Choose your role to get started.", 18, Styles.MUTED);
        intro.setMaxWidth(720);

        VBox heading = new VBox(16, title, intro);
        heading.setAlignment(Pos.CENTER);

        // roleCard(icon, tint, color, title, text, button text, page it opens)
        VBox userCard = roleCard(
                "M2 9l10-5 10 5-10 5z M6 11.5V16c0 1.4 2.7 3 6 3s6-1.6 6-3v-4.5 M22 9v6",
                "#E6EEFB", Styles.BLUE, "User",
                "Explore education loans, scholarships and student benefits in one place.",
                "Continue as User", AppConstants.PAGE_LOGIN);

        VBox adminCard = roleCard(
                "M12 3l7 3v5c0 4.5-3 8-7 10-4-2-7-5.5-7-10V6z M9 12l2 2 4-4",
                "#DFF3EF", Styles.GREEN, "Admin",
                "Manage users, verify information and keep the platform running smoothly.",
                "Continue as Admin", AppConstants.PAGE_ADMIN_LOGIN);

        VBox companyCard = roleCard(
                "M3 21h18 M5 21V5a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v16 M15 21h4v-9a1 1 0 0 0-1-1h-3 M9 8h2 M9 12h2 M9 16h2",
                "#ECE8FA", Styles.PURPLE, "Company",
                "Partner with us to offer loan programs, scholarships and exclusive benefits for students.",
                "Continue as Company", AppConstants.PAGE_COMPANY_LOGIN);

        HBox cards = new HBox(24, userCard, adminCard, companyCard);
        cards.setAlignment(Pos.CENTER);

        VBox main = new VBox(56, heading, cards);
        main.setAlignment(Pos.TOP_CENTER);
        main.setPadding(new Insets(72, 64, 64, 64));
        return main;
    }

    /** One role card. The button opens the page called targetPage. */
    private VBox roleCard(String iconPath, String tint, String color, String title,
                          String description, String buttonText, String targetPage) {

        StackPane iconBox = new StackPane(icon(iconPath, Color.web(color), 1.5));
        iconBox.setMinSize(76, 76);
        iconBox.setMaxSize(76, 76);
        iconBox.setStyle("-fx-background-color:" + tint + "; -fx-background-radius:20;");

        Label name = UiFactory.text(title, 24, true, Styles.TEXT);
        Label text = UiFactory.paragraph(description, 15, Styles.MUTED);

        Button button = UiFactory.filledButton(buttonText + "   \u2192", color);
        button.setOnAction(e -> SceneNavigator.go(targetPage));   // the link

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);   // pushes the button to the bottom

        VBox card = new VBox(16, iconBox, name, text, grow, button);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(36, 32, 32, 32));
        card.setMinWidth(350);
        card.setPrefWidth(350);
        card.setMaxWidth(350);
        card.setPrefHeight(400);
        card.setStyle("-fx-background-color:white; -fx-background-radius:16;"
                + "-fx-border-color:" + Styles.BORDER + "; -fx-border-radius:16;");
        card.setEffect(new DropShadow(24, 0, 8, Color.rgb(15, 27, 45, 0.08)));
        return card;
    }

    // ---------- Highlights strip ----------
    private HBox buildHighlights() {
        HBox box = new HBox(72,
                highlight("Trusted Platform", "Safe, secure and reliable"),
                highlight("Wide Opportunities", "Loans, scholarships and more"),
                highlight("Better Future", "For every student"));
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(36, 64, 36, 64));
        box.setStyle("-fx-background-color:white;"
                + "-fx-border-color:" + Styles.BORDER + " transparent transparent transparent;"
                + "-fx-border-width: 1 0 0 0;");
        return box;
    }

    private VBox highlight(String title, String subtitle) {
        VBox box = new VBox(4, UiFactory.text(title, 15, true, Styles.TEXT),
                               UiFactory.text(subtitle, 14, false, Styles.MUTED));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    // ---------- Footer ----------
    private HBox buildFooter() {
        HBox footer = new HBox(
                UiFactory.text("\u00A9 2026 ScholarX. All rights reserved.", 13, false, "#5B6676"));
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 64, 20, 64));
        footer.setStyle("-fx-border-color:" + Styles.BORDER + " transparent transparent transparent;"
                + "-fx-border-width: 1 0 0 0;");
        return footer;
    }

    // ---------- Draws a line icon from an SVG path ----------
    private SVGPath icon(String path, Color color, double scale) {
        SVGPath svg = new SVGPath();
        svg.setContent(path);
        svg.setFill(null);                  // outline only
        svg.setStroke(color);
        svg.setStrokeWidth(1.7);
        svg.setStrokeLineCap(StrokeLineCap.ROUND);
        svg.setStrokeLineJoin(StrokeLineJoin.ROUND);
        svg.setScaleX(scale);
        svg.setScaleY(scale);
        return svg;
    }
}