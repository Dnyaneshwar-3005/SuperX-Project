package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.DashboardController;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** The content of the Dashboard (the sidebar and top bar come from MainLayoutView). */
public class DashboardView extends VBox {

    private final DashboardController controller = new DashboardController();

    public DashboardView() {
        setSpacing(24);

        VBox left = buildLeftColumn();
        HBox.setHgrow(left, Priority.ALWAYS);

        VBox right = buildRightColumn();
        right.setPrefWidth(320);
        right.setMinWidth(300);
        right.setMaxWidth(340);

        HBox columns = new HBox(24, left, right);

        Label note = UiFactory.text("ScholarX provides information and redirects you to official bank "
                + "websites. It does not approve loans or submit bank applications.", 12, false, "#5B6676");
        note.setWrapText(true);

        getChildren().addAll(columns, note);
    }

    // =====================================================
    //  LEFT COLUMN
    // =====================================================
    private VBox buildLeftColumn() {
        HBox stats = new HBox(20,
                statCard("M3 21h18 M5 21V10 M9 21V10 M15 21V10 M19 21V10 M2 10l10-6 10 6z",
                        "#E6EEFB", Styles.BLUE, "Available Loans", "[00]", "Banks and financial institutions"),
                statCard("M2 9l10-5 10 5-10 5z M6 11.5V16c0 1.4 2.7 3 6 3s6-1.6 6-3v-4.5",
                        "#ECE8FA", Styles.PURPLE, "Total Scholarships", "[0,000]", "For eligible students"),
                statCard("M3 12V4h8l10 10-8 8z M7.5 8.5h0.01",
                        "#DFF3EF", Styles.GREEN, "Student Benefits", "[00]", "Offers and discounts"));

        HBox middleRow = new HBox(24, buildScholarshipCard(), buildBenefitsCard());
        HBox bottomRow = new HBox(24, buildLoanCard(), buildJourneyCard());

        return new VBox(24, buildHero(), stats, middleRow, bottomRow);
    }

    /** The big navy welcome banner. */
    private StackPane buildHero() {
        Label title = UiFactory.text("Hello, " + controller.getStudentName(), 32, true, "white");
        Label tagline = UiFactory.text("Your future is not just a dream, it is a plan.", 18, false, "#E3EAF2");
        Label para = UiFactory.text("Explore education loans, discover scholarships and access student "
                + "benefits, all in one place.", 15, false, "#C7D4E3");
        para.setWrapText(true);
        para.setMaxWidth(480);

        Button explore = new Button("Explore Now");
        explore.setPrefHeight(46);
        explore.setStyle(Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + Styles.NAVY + ";"
                + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:10;"
                + "-fx-padding:0 24; -fx-cursor:hand;");
        explore.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));
        VBox.setMargin(explore, new Insets(8, 0, 0, 0));

        VBox text = new VBox(14, title, tagline, para, explore);
        text.setAlignment(Pos.CENTER_LEFT);
        text.setPadding(new Insets(40, 44, 40, 44));

        StackPane hero = new StackPane();
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setMinHeight(200);
        hero.setStyle("-fx-background-color:" + Styles.NAVY + "; -fx-background-radius:16;");

        // Soft decorative circles (unmanaged = they do not change the size of the banner)
        Circle c1 = new Circle(160, Color.rgb(255, 255, 255, 0.06));
        c1.setManaged(false);
        c1.centerXProperty().bind(hero.widthProperty().subtract(100));
        c1.setCenterY(80);
        Circle c2 = new Circle(110, Color.rgb(255, 255, 255, 0.05));
        c2.setManaged(false);
        c2.centerXProperty().bind(hero.widthProperty().subtract(230));
        c2.centerYProperty().bind(hero.heightProperty());

        // Cut off anything that sticks out of the rounded corners
        Rectangle clip = new Rectangle();
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        clip.widthProperty().bind(hero.widthProperty());
        clip.heightProperty().bind(hero.heightProperty());
        hero.setClip(clip);

        hero.getChildren().addAll(c1, c2, text);
        return hero;
    }

    /** One of the three number cards. */
    private VBox statCard(String iconPath, String tint, String color,
                          String label, String value, String caption) {
        StackPane iconBox = new StackPane(icon(iconPath, Color.web(color), 0.83));
        iconBox.setMinSize(40, 40);
        iconBox.setMaxSize(40, 40);
        iconBox.setStyle("-fx-background-color:" + tint + "; -fx-background-radius:10;");

        VBox box = new VBox(6, iconBox,
                UiFactory.text(label, 14, false, Styles.MUTED),
                UiFactory.text(value, 28, true, Styles.TEXT),
                UiFactory.text(caption, 12, false, "#5B6676"));
        box.setPadding(new Insets(22));
        box.setStyle(cardStyle());
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefWidth(200);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private VBox buildScholarshipCard() {
        HBox numbers = new HBox(12,
                miniStat("[00]", "Matching scholarships"),
                miniStat("[Amount]", "Average benefit"),
                miniStat("[Date]", "Next deadline"));

        Button merit = categoryButton("Merit Based");
        Button need = categoryButton("Need Based");
        Button category = categoryButton("Category Based");
        Button course = categoryButton("Course Specific");

        VBox categories = new VBox(12,
                UiFactory.text("Top scholarship categories", 13, true, Styles.MUTED),
                grid(2, merit, need, category, course));
        categories.setPadding(new Insets(18, 0, 0, 0));
        categories.setStyle("-fx-border-color:#EDF1F6 transparent transparent transparent; -fx-border-width:1 0 0 0;");

        return card(
                cardHeader("Scholarship Overview", "Scholarships that match your profile.", AppConstants.PAGE_SCHOLARSHIPS),
                numbers, categories);
    }

    private VBox buildBenefitsCard() {
        return card(
                cardHeader("Student Benefits", "Offers and discounts for students.", AppConstants.PAGE_BENEFITS),
                grid(3,
                        benefitTile("Technology", "#1B4FBF"),
                        benefitTile("Education", "#5B3FD0"),
                        benefitTile("Travel", "#0F766E"),
                        benefitTile("Shopping", "#B45309"),
                        benefitTile("Food & Beverages", "#BE185D"),
                        benefitTile("Entertainment", "#0E7490")));
    }

    private VBox buildLoanCard() {
        return card(
                cardHeader("Education Loan Assistance", "Compare loans, check eligibility and calculate EMI.",
                        AppConstants.PAGE_LOANS),
                grid(2, bankTile("SBI"), bankTile("HDFC Bank"), bankTile("ICICI Bank"), bankTile("Axis Bank")));
    }

    private VBox buildJourneyCard() {
        HBox steps = new HBox(16,
                journeyStep("1", "Profile", "Completed", 0),
                journeyStep("2", "Explore Loans", "In progress", 1),
                journeyStep("3", "Check Benefits", "Pending", 2),
                journeyStep("4", "Apply with the Bank", "Pending", 2));

        VBox heading = new VBox(4,
                UiFactory.text("Your Financial Journey", 17, true, Styles.TEXT),
                wrapped("Track your progress and explore more opportunities.", 13, Styles.MUTED));
        VBox box = card(heading, steps);
        box.setSpacing(22);
        return box;
    }

    // =====================================================
    //  RIGHT COLUMN
    // =====================================================
    private VBox buildRightColumn() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.ENGLISH));

        VBox deadlines = new VBox(16,
                deadlineItem("#5B3FD0", "[Scholarship name]", "Deadline: [Date]"),
                deadlineItem("#1B4FBF", "[Education loan scheme]", "Last date: [Date]"),
                deadlineItem("#0F766E", "[Scholarship name]", "Deadline: [Date]"));

        VBox deadlineBlock = new VBox(16,
                simpleHeader("Upcoming Deadlines", AppConstants.PAGE_SCHOLARSHIPS), deadlines);
        deadlineBlock.setPadding(new Insets(18, 0, 0, 0));
        deadlineBlock.setStyle("-fx-border-color:#EDF1F6 transparent transparent transparent; -fx-border-width:1 0 0 0;");

        VBox dateCard = card(UiFactory.text(today, 15, true, Styles.TEXT), deadlineBlock);
        dateCard.setSpacing(18);

        VBox quick = card(
                UiFactory.text("Quick Actions", 17, true, Styles.TEXT),
                new VBox(10,
                        actionRow("Check Loan Eligibility", AppConstants.PAGE_LOANS),
                        actionRow("Calculate EMI", AppConstants.PAGE_EMI),
                        actionRow("Find Student Benefits", AppConstants.PAGE_BENEFITS),
                        actionRow("Update Profile", AppConstants.PAGE_PROFILE)));
        quick.setSpacing(16);

        VBox recentRows = new VBox(
                recentRow("B1", "#E6EEFB", Styles.BLUE),
                recentRow("B2", "#ECE8FA", Styles.PURPLE),
                recentRow("B3", "#DFF3EF", Styles.GREEN));
        VBox recent = card(simpleHeader("Recent Benefits", AppConstants.PAGE_BENEFITS), recentRows);
        recent.setSpacing(16);

        return new VBox(24, dateCard, quick, recent);
    }

    // =====================================================
    //  Small building blocks
    // =====================================================
    private String cardStyle() {
        return "-fx-background-color:white; -fx-background-radius:14;"
                + "-fx-border-color:#E1E7EF; -fx-border-radius:14;";
    }

    /** A white rounded box with the given content inside. Two cards in a row share the width. */
    private VBox card(Node... children) {
        VBox box = new VBox(20, children);
        box.setPadding(new Insets(24));
        box.setStyle(cardStyle());
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefWidth(340);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    /** Card title + small text on the left, "View all" link on the right. */
    private HBox cardHeader(String title, String subtitle, String page) {
        VBox texts = new VBox(4,
                UiFactory.text(title, 17, true, Styles.TEXT),
                wrapped(subtitle, 13, Styles.MUTED));
        HBox.setHgrow(texts, Priority.ALWAYS);
        HBox row = new HBox(12, texts, linkButton("View all", page));
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    /** Title on the left, "View all" on the right (no small text). */
    private HBox simpleHeader(String title, String page) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(12, UiFactory.text(title, 17, true, Styles.TEXT), spacer, linkButton("View all", page));
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Button linkButton(String text, String page) {
        Button button = new Button(text);
        button.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0; -fx-cursor:hand;"
                + "-fx-text-fill:" + Styles.BLUE + "; -fx-font-size:13px; -fx-font-weight:bold;");
        button.setOnAction(e -> controller.openPage(page));
        return button;
    }

    private VBox miniStat(String value, String label) {
        VBox box = new VBox(4, UiFactory.text(value, 18, true, Styles.TEXT), wrapped(label, 12, Styles.MUTED));
        box.setPadding(new Insets(14));
        box.setStyle("-fx-border-color:#E8EDF3; -fx-border-radius:10;");
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefWidth(100);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private Button categoryButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(44);
        button.setStyle(Styles.FONT + "-fx-background-color:#F8FAFD; -fx-text-fill:" + Styles.TEXT + ";"
                + "-fx-font-size:13px; -fx-font-weight:bold; -fx-background-radius:10;"
                + "-fx-border-color:#DCE3EC; -fx-border-radius:10; -fx-cursor:hand;");
        button.setOnAction(e -> controller.openPage(AppConstants.PAGE_SCHOLARSHIPS));
        return button;
    }

    private VBox benefitTile(String name, String color) {
        Region square = new Region();
        square.setMinSize(10, 10);
        square.setMaxSize(10, 10);
        square.setStyle("-fx-background-color:" + color + "; -fx-background-radius:3;");

        VBox tile = new VBox(4, square,
                UiFactory.text(name, 14, true, Styles.TEXT),
                UiFactory.text("Up to [00]% off", 12, false, Styles.MUTED));
        ((Label) tile.getChildren().get(1)).setWrapText(true);
        VBox.setMargin(tile.getChildren().get(1), new Insets(6, 0, 0, 0));
        tile.setPadding(new Insets(14));
        tile.setStyle("-fx-border-color:#E8EDF3; -fx-border-radius:10; -fx-cursor:hand;");
        tile.setOnMouseClicked(e -> controller.openPage(AppConstants.PAGE_BENEFITS));
        return tile;
    }

    private VBox bankTile(String name) {
        VBox tile = new VBox(6,
                UiFactory.text(name, 16, true, Styles.TEXT),
                UiFactory.text("[Rate] p.a.", 12, false, Styles.MUTED),
                UiFactory.text("Up to [Amount]", 12, false, Styles.MUTED));
        tile.setPadding(new Insets(16));
        tile.setStyle("-fx-border-color:#E8EDF3; -fx-border-radius:10; -fx-cursor:hand;");
        tile.setOnMouseClicked(e -> controller.openPage(AppConstants.PAGE_LOANS));
        return tile;
    }

    /** A step in "Your Financial Journey". status: 0 = done, 1 = in progress, 2 = pending. */
    private VBox journeyStep(String number, String name, String statusText, int status) {
        String color = (status == 0) ? Styles.GREEN : (status == 1) ? Styles.BLUE : "#5B6676";

        Label num = UiFactory.text(number, 13, true, status == 2 ? "#5B6676" : "white");
        StackPane circle = new StackPane(num);
        circle.setMinSize(32, 32);
        circle.setMaxSize(32, 32);
        circle.setStyle(status == 2
                ? "-fx-border-color:#B9C4D3; -fx-border-width:1.5; -fx-border-radius:16; -fx-background-radius:16;"
                : "-fx-background-color:" + color + "; -fx-background-radius:16;");

        Label title = UiFactory.text(name, 14, true, Styles.TEXT);
        title.setWrapText(true);
        Label statusLabel = UiFactory.text(statusText, 12, status != 2, color);

        VBox step = new VBox(8, circle, title, statusLabel);
        HBox.setHgrow(step, Priority.ALWAYS);
        step.setPrefWidth(100);
        return step;
    }

    private HBox deadlineItem(String color, String title, String sub) {
        Region bar = new Region();
        bar.setMinWidth(4);
        bar.setPrefWidth(4);
        bar.setMaxWidth(4);
        bar.setStyle("-fx-background-color:" + color + "; -fx-background-radius:2;");

        VBox texts = new VBox(3, UiFactory.text(title, 14, true, Styles.TEXT),
                UiFactory.text(sub, 13, false, Styles.MUTED));
        return new HBox(14, bar, texts);
    }

    /** A clickable row in Quick Actions. */
    private HBox actionRow(String text, String page) {
        StackPane arrow = new StackPane(icon("M9 6l6 6-6 6", Color.web("#5B6676"), 0.67));
        arrow.setMinSize(16, 16);
        arrow.setMaxSize(16, 16);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(UiFactory.text(text, 14, true, Styles.TEXT), spacer, arrow);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 16, 0, 16));
        row.setPrefHeight(52);
        row.setMinHeight(52);
        row.setCursor(Cursor.HAND);
        row.setStyle("-fx-background-color:white; -fx-background-radius:10;"
                + "-fx-border-color:#E1E7EF; -fx-border-radius:10;");
        row.setOnMouseClicked(e -> controller.openPage(page));
        return row;
    }

    private HBox recentRow(String badge, String tint, String color) {
        Label code = UiFactory.text(badge, 14, true, color);
        StackPane box = new StackPane(code);
        box.setMinSize(40, 40);
        box.setMaxSize(40, 40);
        box.setStyle("-fx-background-color:" + tint + "; -fx-background-radius:10;");

        VBox texts = new VBox(2, UiFactory.text("[Brand name]", 14, true, Styles.TEXT),
                UiFactory.text("[Offer details]", 12, false, Styles.MUTED));

        HBox row = new HBox(14, box, texts);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 0, 14, 0));
        row.setStyle("-fx-border-color:#EDF1F6 transparent transparent transparent; -fx-border-width:1 0 0 0;");
        return row;
    }

    /** Puts items into a grid with equal columns. */
    private GridPane grid(int columns, Node... items) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            grid.getColumnConstraints().add(column);
        }
        for (int i = 0; i < items.length; i++) {
            grid.add(items[i], i % columns, i / columns);
        }
        return grid;
    }

    private Label wrapped(String text, int size, String color) {
        Label label = UiFactory.text(text, size, false, color);
        label.setWrapText(true);
        return label;
    }

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
