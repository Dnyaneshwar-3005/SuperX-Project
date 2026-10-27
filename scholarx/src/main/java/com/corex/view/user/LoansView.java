package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.LoansController;
import com.corex.model.Loan;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

/** The content of the Education Loans page (sidebar and top bar come from MainLayoutView). */
public class LoansView extends VBox {

    private static final String BLUE = "#1A4FC0";
    private static final String INK  = "#111827";
    private static final String BODY = "#4B5563";
    private static final String LINE = "#E5E9F0";

    private static final String ICON_BOOKMARK = "M6 3h12v18l-6-4-6 4z";
    private static final String ICON_SEARCH = "M18 11a7 7 0 1 0-14 0 7 7 0 1 0 14 0z M20 20l-3.5-3.5";
    private static final String ICON_TICK = "M5 12l5 5 9-10";
    private static final String ICON_CALC = "M5 3h14v18H5z M8 7h8 M8 12h2 M14 12h2 M8 16h2 M14 16h2";
    private static final String ICON_COMPARE = "M3 4h7v16H3z M14 4h7v16h-7z";

    private final LoansController controller = new LoansController();

    // Form fields
    private final ComboBox<String> levelBox = combo("Undergraduate", "Postgraduate", "Diploma", "Doctoral");
    private final ComboBox<String> locationBox = combo("India", "Abroad");
    private final ComboBox<String> streamBox = combo("Engineering", "Medicine", "Management", "Arts and Science", "Law");
    private final ComboBox<String> admissionBox = combo("Confirmed", "Applied, awaiting result", "Not yet applied");
    private final TextField amountField = input("Enter amount");
    private final TextField incomeField = input("Enter amount");
    private final ComboBox<String> coBox = combo("Yes", "No");
    private final ComboBox<String> collateralBox = combo("No", "Yes");
    private final CheckBox saveBox = new CheckBox("Save these details to my profile");

    // Parts that change when you click "Show matching loans"
    private final HBox matchBar = new HBox(12);
    private final Label resultsTitle = UiFactory.text("", 22, true, INK);
    private final GridPane cardsGrid = new GridPane();

    public LoansView() {
        setSpacing(24);

        // 3 equal columns for the loan cards
        cardsGrid.setHgap(24);
        cardsGrid.setVgap(24);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 3);
            cardsGrid.getColumnConstraints().add(column);
        }

        Label note = UiFactory.text("Rates, rules and charges are published by each bank and change often. "
                + "Results here are a preliminary guide, not an approval. ScholarX redirects you to the "
                + "bank's official website and does not accept loan applications.", 13, false, BODY);
        note.setWrapText(true);

        getChildren().addAll(buildTitleArea(), buildTopRow(), matchBar, buildResultsHeading(), cardsGrid, note);
        refreshResults();
    }

    // =====================================================
    //  Breadcrumb, title and "Saved loans" button
    // =====================================================
    private VBox buildTitleArea() {
        Button dashboard = linkButton("Dashboard", BLUE, 13, false);
        dashboard.setOnAction(e -> controller.openPage(AppConstants.PAGE_DASHBOARD));
        HBox breadcrumb = new HBox(8, dashboard,
                UiFactory.text("/", 13, false, BODY), UiFactory.text("Education Loans", 13, false, BODY));
        breadcrumb.setAlignment(Pos.CENTER_LEFT);

        Label title = UiFactory.text("Education Loans", 34, true, INK);
        Label sub = UiFactory.text("Enter your details once and see the loans you are most likely to qualify for.",
                15, false, BODY);

        Button saved = new Button("Saved loans");
        saved.setGraphic(iconBox(ICON_BOOKMARK, INK, 0.83, 20));
        saved.setGraphicTextGap(8);
        saved.setPrefHeight(48);
        saved.setStyle(Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:12; -fx-padding:0 22; -fx-cursor:hand;");
        saved.setOnAction(e -> controller.openPage(AppConstants.PAGE_SAVED_LOANS));   // the link

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(20, new VBox(4, title, sub), spacer, saved);
        titleRow.setAlignment(Pos.BOTTOM_LEFT);

        return new VBox(10, breadcrumb, titleRow);
    }

    // =====================================================
    //  Form on the left, Loan tools on the right
    // =====================================================
    private HBox buildTopRow() {
        VBox form = buildForm();
        HBox.setHgrow(form, Priority.ALWAYS);
        form.setPrefWidth(620);
        form.setMaxWidth(Double.MAX_VALUE);

        VBox tools = buildTools();
        tools.setPrefWidth(360);
        tools.setMinWidth(320);
        tools.setMaxWidth(380);

        HBox row = new HBox(24, form, tools);
        row.setFillHeight(true);
        return row;
    }

    private VBox buildForm() {
        VBox heading = new VBox(6,
                UiFactory.text("Find loans you qualify for", 22, true, INK),
                UiFactory.text("Fill in your details. We compare them with each bank's published rules.",
                        14, false, BODY));

        GridPane rowOne = grid(4, 18, 18,
                field("Course level", levelBox), field("Study location", locationBox),
                field("Course or stream", streamBox), field("Admission status", admissionBox));
        GridPane rowTwo = grid(4, 18, 18,
                field("Loan amount needed", amountField), field("Annual family income", incomeField),
                field("Co-applicant available", coBox), field("Collateral available", collateralBox));

        // Buttons
        Button show = new Button("Show matching loans");
        show.setGraphic(iconBox(ICON_SEARCH, "#FFFFFF", 0.83, 20));
        show.setGraphicTextGap(10);
        show.setPrefHeight(52);
        show.setStyle(Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                + "-fx-font-size:15px; -fx-font-weight:bold; -fx-background-radius:12;"
                + "-fx-padding:0 24; -fx-cursor:hand;");
        show.setOnAction(e -> refreshResults());

        Button reset = new Button("Reset");
        reset.setPrefHeight(52);
        reset.setStyle(Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                + "-fx-font-size:15px; -fx-font-weight:bold; -fx-background-radius:12;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:12; -fx-padding:0 24; -fx-cursor:hand;");
        reset.setOnAction(e -> resetForm());

        saveBox.setSelected(true);
        saveBox.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-text-fill:" + INK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttons = new HBox(14, show, reset, spacer, saveBox);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(22, 0, 0, 0));
        buttons.setStyle("-fx-border-color:" + LINE + " transparent transparent transparent; -fx-border-width:1 0 0 0;");

        VBox card = new VBox(22, heading, rowOne, rowTwo, buttons);
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle(18));
        return card;
    }

    private VBox buildTools() {
        VBox emi = toolCard(ICON_CALC, "#DCF3EE", "#0F766E", "EMI Calculator",
                "Estimate your monthly repayment before you borrow.",
                "Open EMI Calculator  \u2192", AppConstants.PAGE_EMI, true);
        VBox compare = toolCard(ICON_COMPARE, "#ECE8FA", "#5B3FD0", "Compare Education Loans",
                "See rates, amounts and terms from every bank side by side.",
                "Compare loans  \u2192", AppConstants.PAGE_COMPARE, false);
        VBox.setVgrow(emi, Priority.ALWAYS);
        VBox.setVgrow(compare, Priority.ALWAYS);

        VBox panel = new VBox(18, UiFactory.text("Loan tools", 20, true, INK), emi, compare);
        panel.setPadding(new Insets(28));
        panel.setStyle(cardStyle(18));
        return panel;
    }

    private VBox toolCard(String iconPath, String tint, String color, String title, String text,
                          String buttonText, String page, boolean filled) {
        StackPane tile = new StackPane(iconBox(iconPath, color, 0.92, 22));
        tile.setMinSize(44, 44);
        tile.setMaxSize(44, 44);
        tile.setStyle("-fx-background-color:" + tint + "; -fx-background-radius:12;");

        Label description = UiFactory.text(text, 14, false, BODY);
        description.setWrapText(true);

        Button button = new Button(buttonText);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(48);
        button.setStyle(filled
                ? Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12; -fx-cursor:hand;"
                : Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-border-color:#D5DCE7; -fx-border-radius:12; -fx-cursor:hand;");
        button.setOnAction(e -> controller.openPage(page));   // the link

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        VBox card = new VBox(12, tile, UiFactory.text(title, 17, true, INK), description, grow, button);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:14;");
        return card;
    }

    // =====================================================
    //  Results: match bar + loan cards
    // =====================================================
    private HBox buildResultsHeading() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label sort = UiFactory.text("Sort by: Lowest rate", 13, false, BODY);
        HBox row = new HBox(resultsTitle, spacer, sort);
        row.setAlignment(Pos.BASELINE_LEFT);
        row.setPadding(new Insets(4, 0, 0, 0));
        return row;
    }

    /** Rebuilds the match bar and the cards using the current form values. */
    private void refreshResults() {
        List<Loan> matches = controller.findMatches(locationBox.getValue());
        int count = matches.size();
        boolean found = count > 0;

        String summary = levelBox.getValue() + " \u00B7 " + locationBox.getValue() + " \u00B7 "
                + streamBox.getValue() + " \u00B7 Admission " + admissionBox.getValue().toLowerCase();

        Text countText = new Text(count + (count == 1 ? " loan" : " loans"));
        countText.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-font-weight:bold; -fx-fill:" + INK + ";");
        Text restText = new Text(found ? " match your details: " + summary
                                       : " match your details. Try changing the study location.");
        restText.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-fill:" + INK + ";");
        TextFlow message = new TextFlow(countText, restText);
        HBox.setHgrow(message, Priority.ALWAYS);

        Button edit = linkButton("Edit details", BLUE, 14, true);
        edit.setOnAction(e -> levelBox.requestFocus());

        matchBar.getChildren().setAll(iconBox(ICON_TICK, found ? BLUE : "#B45309", 0.83, 20), message, edit);
        matchBar.setAlignment(Pos.CENTER_LEFT);
        matchBar.setPadding(new Insets(16, 22, 16, 22));
        matchBar.setStyle(found
                ? "-fx-background-color:#E8EEFA; -fx-background-radius:14; -fx-border-color:#D5DFF3; -fx-border-radius:14;"
                : "-fx-background-color:#FFF4E5; -fx-background-radius:14; -fx-border-color:#F5D9A8; -fx-border-radius:14;");

        resultsTitle.setText("Loans you may qualify for (" + count + ")");

        cardsGrid.getChildren().clear();
        for (int i = 0; i < count; i++) {
            cardsGrid.add(loanCard(matches.get(i)), i % 3, i / 3);
        }
    }

    private void resetForm() {
        levelBox.getSelectionModel().selectFirst();
        locationBox.getSelectionModel().selectFirst();
        streamBox.getSelectionModel().selectFirst();
        admissionBox.getSelectionModel().selectFirst();
        coBox.getSelectionModel().selectFirst();
        collateralBox.getSelectionModel().selectFirst();
        amountField.clear();
        incomeField.clear();
        saveBox.setSelected(true);
        refreshResults();
    }

    // =====================================================
    //  One loan card
    // =====================================================
    private VBox loanCard(Loan loan) {
        // Badge + names + bookmark
        StackPane badge = new StackPane(UiFactory.text(loan.getShortCode(), 13, true, loan.getBadgeColor()));
        badge.setMinSize(46, 46);
        badge.setMaxSize(46, 46);
        badge.setStyle("-fx-background-color:" + loan.getBadgeTint() + "; -fx-background-radius:12;");

        Label product = UiFactory.text(loan.getProductName(), 17, true, INK);
        product.setWrapText(true);
        VBox names = new VBox(2, product, UiFactory.text(loan.getBankName(), 13, false, BODY));
        HBox.setHgrow(names, Priority.ALWAYS);

        HBox top = new HBox(14, badge, names, bookmarkButton(loan.getId()));
        top.setAlignment(Pos.TOP_LEFT);

        // Tags
        FlowPane tags = new FlowPane(6, 6,
                tag(loan.getBankType(), loan.getBankType().startsWith("Gov") ? "#E6EEFB" : "#ECE8FA",
                        loan.getBankType().startsWith("Gov") ? "#1A4FC0" : "#5B3FD0"),
                tag(loan.getCoverage(), "#DCF3EE", "#0F766E"),
                tag("Likely eligible", "#0F766E", "#FFFFFF"));

        // Rate
        Label rate = UiFactory.text(loan.getRate(), 26, true, INK);
        Label upTo = UiFactory.text("p.a., up to " + loan.getMaxAmount(), 14, false, BODY);
        HBox rateRow = new HBox(6, rate, upTo);
        rateRow.setAlignment(Pos.BASELINE_LEFT);

        VBox points = new VBox(8, point(loan.getPoint1()), point(loan.getPoint2()));

        // Footer: tenure + View details
        VBox tenure = new VBox(2, UiFactory.text("Repayment tenure", 12, false, BODY),
                UiFactory.text(loan.getTenure(), 14, true, INK));
        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Button details = new Button("View details");
        details.setPrefHeight(48);
        details.setStyle(Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                + "-fx-padding:0 24; -fx-cursor:hand;");
        details.setOnAction(e -> controller.openDetails(loan));   // the link

        HBox footer = new HBox(12, tenure, gap, details);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(16, 0, 0, 0));
        footer.setStyle("-fx-border-color:" + LINE + " transparent transparent transparent; -fx-border-width:1 0 0 0;");

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        VBox card = new VBox(14, top, tags, rateRow, points, grow, footer);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle(18));
        return card;
    }

    /** The round bookmark button. Click to save or un-save the loan. */
    private Button bookmarkButton(String loanId) {
        SVGPath shape = new SVGPath();
        shape.setContent(ICON_BOOKMARK);
        shape.setStrokeWidth(1.8);
        shape.setStrokeLineCap(StrokeLineCap.ROUND);
        shape.setStrokeLineJoin(StrokeLineJoin.ROUND);
        shape.setScaleX(0.75);
        shape.setScaleY(0.75);

        StackPane graphic = new StackPane(shape);
        graphic.setMinSize(18, 18);
        graphic.setMaxSize(18, 18);

        Button button = new Button();
        button.setGraphic(graphic);
        button.setMinSize(44, 44);
        button.setMaxSize(44, 44);

        Runnable paint = () -> {
            boolean on = controller.isSaved(loanId);
            shape.setFill(on ? Color.web(BLUE) : null);
            shape.setStroke(Color.web(on ? BLUE : INK));
            button.setStyle("-fx-background-radius:22; -fx-border-radius:22; -fx-cursor:hand;"
                    + "-fx-background-color:" + (on ? "#E6EEFB" : "white") + ";"
                    + "-fx-border-color:" + (on ? BLUE : LINE) + ";");
        };
        paint.run();

        button.setOnAction(e -> {
            controller.toggleSaved(loanId);
            paint.run();
        });
        return button;
    }

    private Label tag(String text, String bg, String color) {
        Label label = UiFactory.text(text, 12, true, color);
        label.setStyle(label.getStyle() + "-fx-background-color:" + bg + ";"
                + "-fx-background-radius:6; -fx-padding:4 10;");
        return label;
    }

    private HBox point(String text) {
        Label label = UiFactory.text(text, 14, false, BODY);
        label.setWrapText(true);
        HBox row = new HBox(10, iconBox(ICON_TICK, "#0F766E", 0.67, 16), label);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    // =====================================================
    //  Small helpers
    // =====================================================
    private String cardStyle(int radius) {
        return "-fx-background-color:white; -fx-background-radius:" + radius + ";"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:" + radius + ";";
    }

    /** A small label above an input. */
    private VBox field(String labelText, Node input) {
        return new VBox(8, UiFactory.text(labelText, 13, true, INK), input);
    }

    private static ComboBox<String> combo(String... items) {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll(items);
        box.getSelectionModel().selectFirst();
        box.setMaxWidth(Double.MAX_VALUE);
        box.setPrefHeight(48);
        box.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-background-color:white; -fx-background-radius:10;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:10;");
        return box;
    }

    private static TextField input(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(48);
        field.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-background-color:white; -fx-background-radius:10;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:10; -fx-padding:0 14;");
        return field;
    }

    private GridPane grid(int columns, double hgap, double vgap, Node... items) {
        GridPane grid = new GridPane();
        grid.setHgap(hgap);
        grid.setVgap(vgap);
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

    private Button linkButton(String text, String color, int size, boolean bold) {
        Button button = new Button(text);
        button.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0; -fx-cursor:hand;"
                + "-fx-text-fill:" + color + "; -fx-font-size:" + size + "px;"
                + "-fx-font-weight:" + (bold ? "bold" : "normal") + ";");
        return button;
    }

    /** A line icon inside a fixed-size box. */
    private StackPane iconBox(String path, String color, double scale, int size) {
        SVGPath svg = new SVGPath();
        svg.setContent(path);
        svg.setFill(null);
        svg.setStroke(Color.web(color));
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
}