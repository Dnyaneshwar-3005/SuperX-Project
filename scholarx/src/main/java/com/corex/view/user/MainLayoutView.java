package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.MainLayoutController;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/** The frame used by every student page: sidebar on the left, top bar, and your content. */
public class MainLayoutView extends HBox {

    // Icon shapes (drawn as lines)
    private static final String ICON_LOGO = "M2 9l10-5 10 5-10 5z M6 11.5V16c0 1.4 2.7 3 6 3s6-1.6 6-3v-4.5";
    private static final String ICON_DASHBOARD = "M3 3h7v7H3z M14 3h7v7h-7z M3 14h7v7H3z M14 14h7v7h-7z";
    private static final String ICON_LOANS = "M3 21h18 M5 21V10 M9 21V10 M15 21V10 M19 21V10 M2 10l10-6 10 6z";
    private static final String ICON_BENEFITS = "M3 12V4h8l10 10-8 8z M7.5 8.5h0.01";
    private static final String ICON_PROFILE = "M8 8a4 4 0 1 0 8 0 4 4 0 1 0-8 0z M4 21c0-4 4-6 8-6s8 2 8 6";
    private static final String ICON_BELL = "M6 16v-5a6 6 0 0 1 12 0v5l2 2H4z M10 21h4";
    private static final String ICON_SETTINGS = "M15 12a3 3 0 1 0-6 0 3 3 0 1 0 6 0z M12 2v3 M12 19v3 M2 12h3 M19 12h3 "
            + "M4.9 4.9l2.1 2.1 M17 17l2.1 2.1 M4.9 19.1L7 17 M17 7l2.1-2.1";

    private final MainLayoutController controller = new MainLayoutController();
    private final String activePage;

    /**
     * @param activePage which sidebar item to highlight (use an AppConstants.PAGE_ name)
     * @param content    the page content shown next to the sidebar
     */
    public MainLayoutView(String activePage, Node content) {
        this.activePage = activePage;
        setStyle("-fx-background-color:#F4F7FB;");

        VBox sidebar = buildSidebar();

        VBox right = new VBox(24, buildHeader(), content);
        right.setPadding(new Insets(24, 32, 40, 32));
        HBox.setHgrow(right, Priority.ALWAYS);

        getChildren().addAll(sidebar, right);
    }

    // =====================================================
    //  SIDEBAR
    // =====================================================
    private VBox buildSidebar() {
        StackPane logo = new StackPane(icon(ICON_LOGO, Color.web(Styles.NAVY), 0.9));
        logo.setMinSize(40, 40);
        logo.setMaxSize(40, 40);
        logo.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        VBox names = new VBox(2,
                UiFactory.text("ScholarX", 21, true, "white"),
                UiFactory.text("Student Finance Companion", 11, false, "#B7C5D6"));
        HBox brand = new HBox(12, logo, names);
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setPadding(new Insets(0, 12, 0, 12));

        VBox nav = new VBox(4,
                navItem("Dashboard", ICON_DASHBOARD, AppConstants.PAGE_DASHBOARD, 0),
                navItem("Education Loans", ICON_LOANS, AppConstants.PAGE_LOANS, 0),
                navItem("Scholarships", ICON_LOGO, AppConstants.PAGE_SCHOLARSHIPS, 0),
                navItem("Student Benefits", ICON_BENEFITS, AppConstants.PAGE_BENEFITS, 0),
                navItem("My Profile", ICON_PROFILE, AppConstants.PAGE_PROFILE, 0),
                navItem("Notifications", ICON_BELL, AppConstants.PAGE_NOTIFICATIONS, 3),
                navItem("Settings", ICON_SETTINGS, AppConstants.PAGE_SETTINGS, 0));

        // Card at the bottom of the sidebar
        Label promoTitle = UiFactory.text("Your goals.\nOur support.", 18, true, "white");
        Label promoText = UiFactory.text("Loans, scholarships and benefits to help you study with confidence.",
                13, false, "#C7D4E3");
        promoText.setWrapText(true);
        VBox promo = new VBox(10, promoTitle, promoText);
        promo.setPadding(new Insets(24, 22, 24, 22));
        promo.setStyle("-fx-background-color:#163A60; -fx-background-radius:14;");

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        VBox sidebar = new VBox(32, brand, nav, grow, promo);
        sidebar.setPadding(new Insets(28, 16, 24, 16));
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);
        sidebar.setStyle("-fx-background-color:" + Styles.NAVY + ";");
        return sidebar;
    }

    /** One sidebar row. The active page is white, the others are see-through. */
    private HBox navItem(String label, String iconPath, String page, int badge) {
        boolean active = page.equals(activePage);
        String textColor = active ? Styles.NAVY : "#E3EAF2";

        StackPane iconBox = new StackPane(icon(iconPath, Color.web(textColor), 0.75));
        iconBox.setMinSize(18, 18);
        iconBox.setMaxSize(18, 18);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(14, iconBox, UiFactory.text(label, 14, active, textColor), spacer);

        if (badge > 0) {
            Label count = UiFactory.text(String.valueOf(badge), 12, true, "white");
            count.setStyle(count.getStyle() + "-fx-background-color:#E5484D; -fx-background-radius:11;"
                    + "-fx-min-width:22; -fx-min-height:22; -fx-alignment:center;");
            row.getChildren().add(count);
        }

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 14, 0, 14));
        row.setPrefHeight(46);
        row.setMinHeight(46);
        row.setCursor(Cursor.HAND);
        row.setStyle(active ? "-fx-background-color:white; -fx-background-radius:10;"
                            : "-fx-background-radius:10;");

        if (!active) {
            row.setOnMouseEntered(e -> row.setStyle(
                    "-fx-background-color:rgba(255,255,255,0.08); -fx-background-radius:10;"));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-radius:10;"));
        }
        row.setOnMouseClicked(e -> controller.openPage(page));   // the link
        return row;
    }

    // =====================================================
    //  TOP BAR: search, bell, student name
    // =====================================================
    private HBox buildHeader() {
        TextField search = new TextField();
        search.setPromptText("Search loans, scholarships, benefits");
        search.setPrefWidth(520);
        search.setPrefHeight(46);
        search.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-background-color:white;"
                + "-fx-background-radius:10; -fx-border-color:#DCE3EC; -fx-border-radius:10;"
                + "-fx-padding:0 18;");

        // Bell with red dot
        StackPane bell = new StackPane(icon(ICON_BELL, Color.web(Styles.NAVY), 0.83));
        bell.setMinSize(44, 44);
        bell.setMaxSize(44, 44);
        bell.setStyle("-fx-background-color:white; -fx-background-radius:22;"
                + "-fx-border-color:#DCE3EC; -fx-border-radius:22;");
        Circle dot = new Circle(5, Color.web("#E5484D"));
        dot.setStroke(Color.WHITE);
        dot.setStrokeWidth(2);
        StackPane.setAlignment(dot, Pos.TOP_RIGHT);
        StackPane.setMargin(dot, new Insets(9, 10, 0, 0));
        bell.getChildren().add(dot);
        bell.setCursor(Cursor.HAND);
        bell.setOnMouseClicked(e -> controller.openPage(AppConstants.PAGE_NOTIFICATIONS));

        // Avatar with initials
        StackPane avatar = new StackPane(UiFactory.text(controller.getInitials(), 15, true, Styles.NAVY));
        avatar.setMinSize(44, 44);
        avatar.setMaxSize(44, 44);
        avatar.setStyle("-fx-background-color:#DCE6F5; -fx-background-radius:22;");

        VBox who = new VBox(2,
                UiFactory.text(controller.getStudentName(), 14, true, Styles.TEXT),
                UiFactory.text("Student", 12, false, "#5B6676"));
        HBox user = new HBox(12, avatar, who);
        user.setAlignment(Pos.CENTER_LEFT);

        HBox rightSide = new HBox(20, bell, user);
        rightSide.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(16, search, spacer, rightSide);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    // =====================================================
    //  Helper: draws a line icon
    // =====================================================
    private SVGPath icon(String path, Color color, double scale) {
        SVGPath svg = new SVGPath();
        svg.setContent(path);
        svg.setFill(null);
        svg.setStroke(color);
        svg.setStrokeWidth(1.8);
        svg.setStrokeLineCap(StrokeLineCap.ROUND);
        svg.setStrokeLineJoin(StrokeLineJoin.ROUND);
        svg.setScaleX(scale);
        svg.setScaleY(scale);
        return svg;
    }
}