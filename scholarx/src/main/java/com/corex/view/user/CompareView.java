package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.CompareController;
import com.corex.model.Loan;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** The content of the Compare Loans page (sidebar and top bar come from MainLayoutView). */
public class CompareView extends VBox {

    private static final String BLUE = "#1A4FC0";
    private static final String INK  = "#111827";
    private static final String BODY = "#4B5563";
    private static final String LINE = "#E5E9F0";

    private final CompareController controller = new CompareController();
    private final List<Loan> allLoans = controller.getAllLoans();

    private final Map<String, CheckBox> checks = new LinkedHashMap<>();   // loan id -> checkbox
    private final Map<String, Button> chips = new LinkedHashMap<>();      // chip name -> button
    private final VBox tableHolder = new VBox();                          // the table is rebuilt inside this

    private String activeFilter = CompareController.ALL_BANKS;
    private boolean updatingChecks = false;   // true while a chip changes the checkboxes

    public CompareView() {
        setSpacing(24);

        Label disclaimer = UiFactory.text("ScholarX provides information and redirects you to official bank "
                + "websites. It does not approve loans or submit bank applications.", 13, false, BODY);
        disclaimer.setWrapText(true);

        getChildren().addAll(buildTitleArea(), buildBanksCard(), buildTableCard(),
                buildBottomCard(), disclaimer);

        applyFilter(CompareController.ALL_BANKS);   // start with every bank shown
    }

    // =====================================================
    //  Breadcrumb, title and the two buttons
    // =====================================================
    private VBox buildTitleArea() {
        Button dashboard = linkButton("Dashboard", AppConstants.PAGE_DASHBOARD);
        Button loans = linkButton("Education Loans", AppConstants.PAGE_LOANS);
        HBox breadcrumb = new HBox(8, dashboard, UiFactory.text("/", 13, false, BODY),
                loans, UiFactory.text("/", 13, false, BODY),
                UiFactory.text("Compare Loans", 13, false, BODY));
        breadcrumb.setAlignment(Pos.CENTER_LEFT);

        Label title = UiFactory.text("Compare Education Loans", 34, true, INK);
        Label sub = UiFactory.text("See rates, amounts and terms from every bank side by side.", 15, false, BODY);

        Button back = actionButton("Back to Education Loans", false, 48);
        back.setGraphic(icon("M19 12H5M11 6l-6 6 6 6", INK, 0.75, 18));
        back.setGraphicTextGap(8);
        back.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));   // the link

        Button emi = actionButton("EMI Calculator", true, 48);
        emi.setOnAction(e -> controller.openPage(AppConstants.PAGE_EMI));      // the link

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(12, new VBox(4, title, sub), spacer, back, emi);
        titleRow.setAlignment(Pos.BOTTOM_LEFT);

        return new VBox(10, breadcrumb, titleRow);
    }

    // =====================================================
    //  "Banks shown": chips + one checkbox per bank
    // =====================================================
    private VBox buildBanksCard() {
        HBox chipRow = new HBox(8);
        String[] chipNames = {CompareController.ALL_BANKS, CompareController.GOVERNMENT,
                CompareController.PRIVATE, CompareController.ABROAD};
        for (String name : chipNames) {
            Button chip = new Button(name);
            chip.setPrefHeight(40);
            chip.setOnAction(e -> applyFilter(name));
            chips.put(name, chip);
            chipRow.getChildren().add(chip);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(12, UiFactory.text("Banks shown", 18, true, INK), spacer, chipRow);
        top.setAlignment(Pos.CENTER_LEFT);

        FlowPane boxes = new FlowPane(10, 10);
        for (Loan loan : allLoans) {
            CheckBox box = new CheckBox(loan.getBankName());
            box.setSelected(true);
            box.setPrefHeight(44);
            box.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:" + INK + ";"
                    + "-fx-border-color:#D5DCE7; -fx-border-radius:12; -fx-padding:0 16; -fx-cursor:hand;");
            box.selectedProperty().addListener((obs, was, now) -> {
                if (updatingChecks) return;   // a chip is changing the boxes, it will rebuild once
                activeFilter = null;          // you picked banks by hand, so no chip is active
                restyleChips();
                rebuildTable();
            });
            checks.put(loan.getId(), box);
            boxes.getChildren().add(box);
        }

        VBox card = new VBox(16, top, boxes);
        card.setPadding(new Insets(24, 28, 24, 28));
        card.setStyle(cardStyle());
        return card;
    }

    /** A chip was clicked: tick the matching banks and rebuild the table. */
    private void applyFilter(String filter) {
        activeFilter = filter;

        updatingChecks = true;
        for (Loan loan : allLoans) {
            checks.get(loan.getId()).setSelected(controller.matchesFilter(loan, filter));
        }
        updatingChecks = false;

        restyleChips();
        rebuildTable();
    }

    private void restyleChips() {
        for (Map.Entry<String, Button> entry : chips.entrySet()) {
            boolean active = entry.getKey().equals(activeFilter);
            entry.getValue().setStyle(Styles.FONT + "-fx-font-size:14px; -fx-font-weight:"
                    + (active ? "bold" : "normal") + "; -fx-background-radius:20; -fx-border-radius:20;"
                    + "-fx-padding:0 16; -fx-cursor:hand;"
                    + (active
                        ? "-fx-background-color:" + BLUE + "; -fx-text-fill:white; -fx-border-color:" + BLUE + ";"
                        : "-fx-background-color:white; -fx-text-fill:" + INK + "; -fx-border-color:" + LINE + ";"));
        }
    }

    // =====================================================
    //  The comparison table
    // =====================================================
    private VBox buildTableCard() {
        Button download = actionButton("Download comparison", false, 48);
        // The download will be added in a later step.

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(16,
                new VBox(4, UiFactory.text("Side-by-side comparison", 22, true, INK),
                        UiFactory.text("Scroll sideways to see every bank.", 14, false, BODY)),
                spacer, download);
        top.setAlignment(Pos.BOTTOM_LEFT);

        Label note = UiFactory.text("Rates and terms are published by each bank and may change. "
                + "Confirm current details on the bank's official website.", 13, false, BODY);
        note.setWrapText(true);

        VBox card = new VBox(20, top, tableHolder, note);
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle());
        return card;
    }

    /** Draws the table again using only the ticked banks. */
    private void rebuildTable() {
        List<Loan> shown = new ArrayList<>();
        for (Loan loan : allLoans) {
            if (checks.get(loan.getId()).isSelected()) {
                shown.add(loan);
            }
        }

        if (shown.isEmpty()) {
            Label empty = UiFactory.text("No banks selected. Choose at least one bank above to compare.",
                    15, false, BODY);
            empty.setWrapText(true);
            empty.setPadding(new Insets(32, 0, 32, 0));
            tableHolder.getChildren().setAll(empty);
            return;
        }

        GridPane table = new GridPane();
        table.getColumnConstraints().add(new ColumnConstraints(170, 170, 170));   // feature names
        for (int i = 0; i < shown.size(); i++) {
            ColumnConstraints column = new ColumnConstraints(150, 170, Double.MAX_VALUE);
            column.setHgrow(Priority.ALWAYS);
            table.getColumnConstraints().add(column);
        }

        // Header row: bank badges and names
        table.add(cell(UiFactory.text("FEATURE", 12, true, BODY), 20, "#F8FAFD"), 0, 0);
        for (int i = 0; i < shown.size(); i++) {
            table.add(cell(bankHeader(shown.get(i)), 14, "#F8FAFD"), i + 1, 0);
        }

        // Data rows
        int row = 1;
        row = addRow(table, row, "Loan scheme", shown, Loan::getProductName, false);
        row = addRow(table, row, "Bank type", shown, loan -> loan.getBankType().replace(" bank", ""), false);
        row = addRow(table, row, "Study location", shown, Loan::getCoverage, false);
        row = addRow(table, row, "Interest rate", shown, loan -> loan.getRate() + " p.a.", true);
        row = addRow(table, row, "Maximum amount", shown, Loan::getMaxAmount, true);
        row = addRow(table, row, "Repayment tenure", shown, Loan::getTenure, false);
        row = addRow(table, row, "Collateral", shown, Loan::getCollateral, false);
        row = addRow(table, row, "Processing fee", shown, Loan::getProcessingFee, false);
        row = addRow(table, row, "Repayment holiday", shown, Loan::getRepaymentHoliday, false);

        // Last row: View details buttons
        boolean shaded = (row % 2 == 0);
        String background = shaded ? "#FBFCFE" : "white";
        table.add(cell(new Label(""), 20, background), 0, row);
        for (int i = 0; i < shown.size(); i++) {
            Loan loan = shown.get(i);
            Button details = new Button("View details");
            details.setPrefHeight(44);
            details.setStyle(Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                    + "-fx-font-size:13px; -fx-font-weight:bold; -fx-background-radius:10;"
                    + "-fx-padding:0 16; -fx-cursor:hand;");
            details.setOnAction(e -> controller.openDetails(loan));   // the link
            table.add(cell(details, 14, background), i + 1, row);
        }

        // Scrolls sideways when the window is too narrow for all the banks
        ScrollPane scroll = new ScrollPane(table);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:transparent; -fx-background:transparent; -fx-padding:0;");
        scroll.prefHeightProperty().bind(table.heightProperty().add(18));   // room for the scroll bar

        VBox frame = new VBox(scroll);
        frame.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:14;");
        tableHolder.getChildren().setAll(frame);
    }

    /** Adds one row: the feature name, then one value per bank. Returns the next row number. */
    private int addRow(GridPane table, int row, String title, List<Loan> shown,
                       Function<Loan, String> value, boolean strong) {
        boolean shaded = (row % 2 == 0);
        String background = shaded ? "#FBFCFE" : "white";

        table.add(cell(UiFactory.text(title, 14, true, INK), 20, background), 0, row);
        for (int i = 0; i < shown.size(); i++) {
            Label label = UiFactory.text(value.apply(shown.get(i)), 14, strong, strong ? INK : BODY);
            label.setWrapText(true);
            table.add(cell(label, 14, background), i + 1, row);
        }
        return row + 1;
    }

    /** Badge with the bank letters, and the bank name next to it. */
    private HBox bankHeader(Loan loan) {
        StackPane badge = new StackPane(UiFactory.text(loan.getShortCode(), 12, true, loan.getBadgeColor()));
        badge.setMinSize(36, 36);
        badge.setMaxSize(36, 36);
        badge.setStyle("-fx-background-color:" + loan.getBadgeTint() + "; -fx-background-radius:10;");

        Label name = UiFactory.text(loan.getBankName(), 14, true, INK);
        name.setWrapText(true);

        HBox box = new HBox(10, badge, name);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    /** One table cell: padding, background and a thin line below. */
    private VBox cell(Node content, double side, String background) {
        VBox box = new VBox(content);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(16, side, 16, side));
        box.setStyle("-fx-background-color:" + background + ";"
                + "-fx-border-color:transparent transparent #EEF1F6 transparent; -fx-border-width:0 0 1 0;");
        return box;
    }

    // =====================================================
    //  Bottom card
    // =====================================================
    private HBox buildBottomCard() {
        Label text = UiFactory.text("Use the EMI Calculator with the rate and amount you are considering.",
                14, false, BODY);
        text.setWrapText(true);
        VBox texts = new VBox(4, UiFactory.text("Ready to see your monthly repayment?", 17, true, INK), text);
        HBox.setHgrow(texts, Priority.ALWAYS);

        Button open = actionButton("Open EMI Calculator", true, 48);
        open.setOnAction(e -> controller.openPage(AppConstants.PAGE_EMI));   // the link

        HBox card = new HBox(16, texts, open);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(24, 28, 24, 28));
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
                  + "-fx-padding:0 22; -fx-cursor:hand;"
                : Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-border-color:#D5DCE7; -fx-border-radius:12; -fx-padding:0 22; -fx-cursor:hand;");
        return button;
    }

    private Button linkButton(String text, String page) {
        Button button = new Button(text);
        button.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0; -fx-cursor:hand;"
                + "-fx-text-fill:" + BLUE + "; -fx-font-size:13px;");
        button.setOnAction(e -> controller.openPage(page));
        return button;
    }

    private StackPane icon(String path, String color, double scale, int size) {
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