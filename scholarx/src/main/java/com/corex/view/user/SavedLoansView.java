package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.SavedLoansController;
import com.corex.model.Loan;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.List;

/** The content of the Saved Loans page (sidebar and top bar come from MainLayoutView). */
public class SavedLoansView extends VBox {

    private static final String BLUE = "#1A4FC0";
    private static final String INK  = "#111827";
    private static final String BODY = "#4B5563";
    private static final String LINE = "#E5E9F0";

    private static final String ICON_BOOKMARK = "M6 3h12v18l-6-4-6 4z";

    private final SavedLoansController controller = new SavedLoansController();
    private final VBox content = new VBox(24);   // filled again by refresh()

    public SavedLoansView() {
        setSpacing(24);

        Label disclaimer = UiFactory.text("ScholarX provides information and redirects you to official bank "
                + "websites. It does not approve loans or submit bank applications.", 13, false, BODY);
        disclaimer.setWrapText(true);

        getChildren().addAll(buildTitleArea(), content, disclaimer);
        refresh();
    }

    /** Shows the saved loans, or the empty message when nothing is saved. */
    private void refresh() {
        List<Loan> saved = controller.getSavedLoans();
        if (saved.isEmpty()) {
            content.getChildren().setAll(buildEmptyState());
        } else {
            content.getChildren().setAll(buildInfoBar(saved.size()), buildCards(saved));
        }
    }

    // =====================================================
    //  Breadcrumb, title and the two buttons
    // =====================================================
    private VBox buildTitleArea() {
        Button dashboard = linkButton("Dashboard", AppConstants.PAGE_DASHBOARD);
        Button loans = linkButton("Education Loans", AppConstants.PAGE_LOANS);
        HBox breadcrumb = new HBox(8, dashboard, UiFactory.text("/", 13, false, BODY),
                loans, UiFactory.text("/", 13, false, BODY),
                UiFactory.text("Saved loans", 13, false, BODY));
        breadcrumb.setAlignment(Pos.CENTER_LEFT);

        Label title = UiFactory.text("Saved loans", 34, true, INK);
        Label sub = UiFactory.text("Loans you have bookmarked. Open the details or compare them side by side.",
                15, false, BODY);

        Button back = actionButton("Back to Education Loans", false, 48);
        back.setGraphic(arrowIcon());
        back.setGraphicTextGap(8);
        back.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));   // the link

        Button compare = actionButton("Compare loans", true, 48);
        compare.setOnAction(e -> controller.openPage(AppConstants.PAGE_COMPARE));   // the link

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(12, new VBox(4, title, sub), spacer, back, compare);
        titleRow.setAlignment(Pos.BOTTOM_LEFT);

        return new VBox(10, breadcrumb, titleRow);
    }

    // =====================================================
    //  When there are saved loans
    // =====================================================
    private HBox buildInfoBar(int count) {
        Label countLabel = UiFactory.text(count + (count == 1 ? " saved loan" : " saved loans"),
                14, true, INK);

        Label note = UiFactory.text("Rates and terms may change. Check the bank's official website "
                + "before you decide.", 13, false, BODY);
        note.setWrapText(true);
        note.setMaxWidth(520);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(12, bookmarkIcon(true, 0.83, 20), countLabel, spacer, note);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(16, 22, 16, 22));
        bar.setStyle("-fx-background-color:#E8EEFA; -fx-background-radius:14;"
                + "-fx-border-color:#D5DFF3; -fx-border-radius:14;");
        return bar;
    }

    private GridPane buildCards(List<Loan> saved) {
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 3);
            grid.getColumnConstraints().add(column);
        }
        for (int i = 0; i < saved.size(); i++) {
            grid.add(loanCard(saved.get(i)), i % 3, i / 3);
        }
        return grid;
    }

    private VBox loanCard(Loan loan) {
        // Badge + names
        StackPane badge = new StackPane(UiFactory.text(loan.getShortCode(), 13, true, loan.getBadgeColor()));
        badge.setMinSize(46, 46);
        badge.setMaxSize(46, 46);
        badge.setStyle("-fx-background-color:" + loan.getBadgeTint() + "; -fx-background-radius:12;");

        Label product = UiFactory.text(loan.getProductName(), 17, true, INK);
        product.setWrapText(true);
        VBox names = new VBox(2, product, UiFactory.text(loan.getBankName(), 13, false, BODY));
        HBox.setHgrow(names, Priority.ALWAYS);

        HBox top = new HBox(14, badge, names);
        top.setAlignment(Pos.TOP_LEFT);

        // Tags
        boolean government = loan.getBankType().startsWith("Government");
        FlowPane tags = new FlowPane(6, 6,
                tag(loan.getBankType(), government ? "#E6EEFB" : "#ECE8FA", government ? "#1A4FC0" : "#5B3FD0"),
                tag(loan.getCoverage(), "#DCF3EE", "#0F766E"));

        // Rate
        HBox rateRow = new HBox(6,
                UiFactory.text(loan.getRate(), 26, true, INK),
                UiFactory.text("p.a., up to " + loan.getMaxAmount(), 14, false, BODY));
        rateRow.setAlignment(Pos.BASELINE_LEFT);

        // Two small fact boxes
        HBox facts = new HBox(12,
                fact("Tenure", loan.getTenure()),
                fact("Collateral", loan.getCollateral()));

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        // Footer: Remove + View details
        Button remove = actionButton("Remove", false, 48);
        remove.setOnAction(e -> {
            controller.remove(loan);
            refresh();   // draw the page again without this loan
        });

        Button details = actionButton("View details", true, 48);
        details.setOnAction(e -> controller.openDetails(loan));   // the link

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);
        HBox footer = new HBox(12, remove, gap, details);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(16, 0, 0, 0));
        footer.setStyle("-fx-border-color:" + LINE + " transparent transparent transparent;"
                + "-fx-border-width:1 0 0 0;");

        VBox card = new VBox(16, top, tags, rateRow, facts, grow, footer);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle());
        return card;
    }

    private VBox fact(String label, String value) {
        VBox box = new VBox(2, UiFactory.text(label, 12, false, BODY), UiFactory.text(value, 14, true, INK));
        box.setPadding(new Insets(10, 12, 10, 12));
        box.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:10;");
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefWidth(100);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private Label tag(String text, String background, String color) {
        Label label = UiFactory.text(text, 12, true, color);
        label.setStyle(label.getStyle() + "-fx-background-color:" + background + ";"
                + "-fx-background-radius:6; -fx-padding:4 10;");
        return label;
    }

    // =====================================================
    //  When nothing is saved
    // =====================================================
    private VBox buildEmptyState() {
        StackPane tile = new StackPane(bookmarkIcon(false, 1.33, 32));
        tile.setMinSize(72, 72);
        tile.setMaxSize(72, 72);
        tile.setStyle("-fx-background-color:#E6EEFB; -fx-background-radius:20;");

        Label title = UiFactory.text("No saved loans yet", 22, true, INK);
        Label text = UiFactory.paragraph("Select the bookmark icon on any loan to keep it here, "
                + "so you can compare it later.", 15, BODY);
        text.setMaxWidth(420);

        Button browse = actionButton("Browse education loans", true, 50);
        browse.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));   // the link
        VBox.setMargin(browse, new Insets(10, 0, 0, 0));

        VBox card = new VBox(14, tile, title, text, browse);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(64, 28, 64, 28));
        card.setStyle(cardStyle());
        return card;
    }

    // =====================================================
    //  Small helpers
    // =====================================================
    private String cardStyle() {
        return "-fx-background-color:white; -fx-background-radius:18;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:18;";
    }

    private Button actionButton(String text, boolean filled, int height) {
        Button button = new Button(text);
        button.setPrefHeight(height);
        button.setStyle(filled
                ? Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-padding:0 24; -fx-cursor:hand;"
                : Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-border-color:" + LINE + "; -fx-border-radius:12; -fx-padding:0 20; -fx-cursor:hand;");
        return button;
    }

    private Button linkButton(String text, String page) {
        Button button = new Button(text);
        button.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0; -fx-cursor:hand;"
                + "-fx-text-fill:" + BLUE + "; -fx-font-size:13px;");
        button.setOnAction(e -> controller.openPage(page));
        return button;
    }

    /** The bookmark shape. Filled blue when "filled" is true, outline otherwise. */
    private StackPane bookmarkIcon(boolean filled, double scale, int size) {
        SVGPath svg = new SVGPath();
        svg.setContent(ICON_BOOKMARK);
        svg.setFill(filled ? Color.web(BLUE) : null);
        svg.setStroke(Color.web(BLUE));
        svg.setStrokeWidth(1.8);
        svg.setStrokeLineCap(StrokeLineCap.ROUND);
        svg.setStrokeLineJoin(StrokeLineJoin.ROUND);
        svg.setScaleX(scale);
        svg.setScaleY(scale);
        StackPane box = new StackPane(svg);
        box.setMinSize(size, size);
        box.setMaxSize(size, size);
        return box;
    }

    private StackPane arrowIcon() {
        SVGPath svg = new SVGPath();
        svg.setContent("M19 12H5M11 6l-6 6 6 6");
        svg.setFill(null);
        svg.setStroke(Color.web(INK));
        svg.setStrokeWidth(1.8);
        svg.setStrokeLineCap(StrokeLineCap.ROUND);
        svg.setStrokeLineJoin(StrokeLineJoin.ROUND);
        svg.setScaleX(0.75);
        svg.setScaleY(0.75);
        StackPane box = new StackPane(svg);
        box.setMinSize(18, 18);
        box.setMaxSize(18, 18);
        return box;
    }
}