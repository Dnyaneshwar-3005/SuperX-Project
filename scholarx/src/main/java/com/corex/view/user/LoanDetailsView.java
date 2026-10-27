package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.LoanDetailsController;
import com.corex.model.Loan;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/** The content of the Loan Details page (sidebar and top bar come from MainLayoutView). */
public class LoanDetailsView extends VBox {

    private static final String BLUE = "#1A4FC0";
    private static final String INK  = "#111827";
    private static final String BODY = "#4B5563";
    private static final String LINE = "#E5E9F0";

    private static final String ICON_BOOKMARK = "M6 3h12v18l-6-4-6 4z";

    private static final String[] TAB_NAMES = {"Overview", "Eligibility Criteria", "Required Documents",
            "Interest Rate & Charges", "Key Features", "FAQs"};

    private final LoanDetailsController controller = new LoanDetailsController();
    private final Loan loan = controller.getSelectedLoan();

    private final FlowPane tabBar = new FlowPane(4, 0);
    private final VBox panel = new VBox(22);        // the content of the chosen tab
    private final VBox notice = new VBox(10);       // brochure / website message
    private final Button saveButton = new Button();

    public LoanDetailsView() {
        setSpacing(24);

        if (loan == null) {   // nobody chose a loan
            getChildren().addAll(buildBreadcrumb(), buildNotFound());
            return;
        }

        getChildren().addAll(buildBreadcrumb(), buildSummaryCard(), buildBody());
        showTab(0);
    }

    // =====================================================
    //  Breadcrumb
    // =====================================================
    private HBox buildBreadcrumb() {
        Button dashboard = linkButton("Dashboard", AppConstants.PAGE_DASHBOARD);
        Button loans = linkButton("Education Loans", AppConstants.PAGE_LOANS);
        HBox breadcrumb = new HBox(8, dashboard, UiFactory.text("/", 13, false, BODY),
                loans, UiFactory.text("/", 13, false, BODY),
                UiFactory.text("Loan details", 13, false, BODY));
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        return breadcrumb;
    }

    private VBox buildNotFound() {
        Button back = actionButton("Back to Education Loans", true, 50);
        back.setMaxWidth(260);
        back.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));

        Label text = UiFactory.paragraph("Open a loan from the Education Loans page to see its details.",
                15, BODY);
        VBox card = new VBox(14, UiFactory.text("No loan selected", 22, true, INK), text, back);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(64, 28, 64, 28));
        card.setStyle(cardStyle(18));
        return card;
    }

    // =====================================================
    //  Summary card: name, buttons and four key numbers
    // =====================================================
    private VBox buildSummaryCard() {
        StackPane badge = new StackPane(UiFactory.text(loan.getShortCode(), 18, true, loan.getBadgeColor()));
        badge.setMinSize(64, 64);
        badge.setMaxSize(64, 64);
        badge.setStyle("-fx-background-color:" + loan.getBadgeTint() + "; -fx-background-radius:16;");

        Label name = UiFactory.text(loan.getProductName(), 30, true, INK);
        name.setWrapText(true);
        boolean government = loan.getBankType().startsWith("Government");
        FlowPane tags = new FlowPane(6, 6,
                tag(loan.getBankType(), government ? "#E6EEFB" : "#ECE8FA", government ? "#1A4FC0" : "#5B3FD0"),
                tag(loan.getCoverage(), "#DCF3EE", "#0F766E"));
        VBox names = new VBox(4, name, UiFactory.text(loan.getBankName(), 15, false, BODY), tags);
        VBox.setMargin(tags, new Insets(6, 0, 0, 0));
        HBox.setHgrow(names, Priority.ALWAYS);

        HBox left = new HBox(18, badge, names);
        left.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(left, Priority.ALWAYS);

        // Buttons on the right
        Button back = actionButton("Back to Loans", false, 48);
        back.setGraphic(arrowIcon());
        back.setGraphicTextGap(8);
        back.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));   // the link

        saveButton.setPrefHeight(48);
        saveButton.setOnAction(e -> {
            controller.toggleSaved(loan);
            paintSaveButton();
        });
        paintSaveButton();

        HBox top = new HBox(20, left, new HBox(12, back, saveButton));
        top.setAlignment(Pos.TOP_LEFT);

        // Four key numbers
        HBox stats = new HBox(14,
                summaryStat("Interest rate", loan.getRate() + " p.a."),
                summaryStat("Loan amount", "[Min to max]"),
                summaryStat("Repayment period", loan.getTenure()),
                summaryStat("Collateral", loan.getCollateral()));
        stats.setPadding(new Insets(22, 0, 0, 0));
        stats.setStyle("-fx-border-color:" + LINE + " transparent transparent transparent; -fx-border-width:1 0 0 0;");

        VBox card = new VBox(24, top, stats);
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle(18));
        return card;
    }

    private VBox summaryStat(String label, String value) {
        Label number = UiFactory.text(value, 20, true, INK);
        number.setWrapText(true);
        VBox box = new VBox(2, UiFactory.text(label, 12, false, BODY), number);
        box.setPadding(new Insets(14, 16, 14, 16));
        box.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:12;");
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setPrefWidth(160);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    /** "Save loan" turns into a blue "Saved" when the loan is bookmarked. */
    private void paintSaveButton() {
        boolean saved = controller.isSaved(loan);
        saveButton.setText(saved ? "Saved" : "Save loan");
        saveButton.setGraphic(bookmarkIcon(saved));
        saveButton.setGraphicTextGap(8);
        saveButton.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-font-weight:bold;"
                + "-fx-background-radius:12; -fx-border-radius:12; -fx-padding:0 20; -fx-cursor:hand;"
                + (saved
                    ? "-fx-background-color:#E6EEFB; -fx-text-fill:" + BLUE + "; -fx-border-color:" + BLUE + ";"
                    : "-fx-background-color:white; -fx-text-fill:" + INK + "; -fx-border-color:" + LINE + ";"));
    }

    // =====================================================
    //  Body: tabs on the left, next steps on the right
    // =====================================================
    private HBox buildBody() {
        tabBar.setPadding(new Insets(0, 16, 0, 16));
        tabBar.setStyle("-fx-border-color:transparent transparent " + LINE + " transparent; -fx-border-width:0 0 1 0;");

        panel.setPadding(new Insets(28));
        panel.setMinHeight(520);

        VBox tabsCard = new VBox(tabBar, panel);
        tabsCard.setStyle(cardStyle(18));
        HBox.setHgrow(tabsCard, Priority.ALWAYS);
        tabsCard.setPrefWidth(560);
        tabsCard.setMaxWidth(Double.MAX_VALUE);

        VBox aside = buildAside();
        aside.setPrefWidth(320);
        aside.setMinWidth(300);
        aside.setMaxWidth(340);

        HBox body = new HBox(24, tabsCard, aside);
        body.setFillHeight(false);   // the right column keeps its own height
        return body;
    }

    /** Draws the tab buttons and the content of the chosen tab. */
    private void showTab(int index) {
        tabBar.getChildren().clear();
        for (int i = 0; i < TAB_NAMES.length; i++) {
            boolean active = (i == index);
            final int tabNumber = i;
            Button tab = new Button(TAB_NAMES[i]);
            tab.setPrefHeight(56);
            tab.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-font-size:14px;"
                    + "-fx-font-weight:" + (active ? "bold" : "normal") + ";"
                    + "-fx-text-fill:" + (active ? INK : BODY) + ";"
                    + "-fx-background-radius:0; -fx-padding:0 16; -fx-cursor:hand;"
                    + "-fx-border-width:0 0 3 0; -fx-border-color:transparent transparent "
                    + (active ? BLUE : "transparent") + " transparent;");
            tab.setOnAction(e -> showTab(tabNumber));
            tabBar.getChildren().add(tab);
        }

        Node content;
        switch (index) {
            case 0:  content = overviewPanel();    break;
            case 1:  content = eligibilityPanel(); break;
            case 2:  content = documentsPanel();   break;
            case 3:  content = chargesPanel();     break;
            case 4:  content = featuresPanel();    break;
            default: content = faqPanel();         break;
        }
        panel.getChildren().setAll(content);
    }

    // ---------- The six tab contents ----------
    private VBox overviewPanel() {
        Label description = wrapped("[Loan purpose and general description, as published by the bank.]", 15);
        description.setMaxWidth(720);

        HBox cards = new HBox(16,
                infoCard("Loan amount", "[Minimum and maximum amounts, as published by the bank.]"),
                infoCard("Interest rate", "[Applicable rates and conditions.]"),
                infoCard("Repayment period", "[Available repayment tenure.]"));

        return new VBox(22, new VBox(8, UiFactory.text("Loan overview", 20, true, INK), description), cards);
    }

    private VBox eligibilityPanel() {
        return new VBox(18,
                heading("Eligibility criteria", "Read the bank's requirements to see whether you may qualify."),
                rowsBox(new String[][]{
                        {"Nationality", "[Requirement]"},
                        {"Age", "[Requirement]"},
                        {"Admission", "[Requirement]"},
                        {"Courses covered", "[Requirement]"},
                        {"Academic record", "[Requirement]"},
                        {"Co-applicant", "[Requirement]"}}));
    }

    private VBox documentsPanel() {
        VBox list = new VBox(10,
                documentRow("Identity proof", null),
                documentRow("Address proof", null),
                documentRow("Academic records", null),
                documentRow("Admission letter", null),
                documentRow("Income proof", "Where applicable"),
                documentRow("Co-applicant documents", "Where applicable"));
        return new VBox(18,
                heading("Required documents", "Documents the bank may ask for. Confirm the final list with the bank."),
                list);
    }

    private VBox chargesPanel() {
        Button emi = linkButton("Calculate EMI", AppConstants.PAGE_EMI);
        emi.setStyle(emi.getStyle() + "-fx-font-size:14px; -fx-font-weight:bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox tip = new HBox(12, UiFactory.text("Want to see what this costs each month?", 14, false, INK), spacer, emi);
        tip.setAlignment(Pos.CENTER_LEFT);
        tip.setPadding(new Insets(14, 18, 14, 18));
        tip.setStyle("-fx-background-color:#E8EEFA; -fx-background-radius:12;");

        return new VBox(18,
                heading("Interest rate and charges", "Published rates and fees. Last verified on [Date]."),
                rowsBox(new String[][]{
                        {"Interest rate", loan.getRate() + " p.a."},
                        {"Processing fee", loan.getProcessingFee()},
                        {"Prepayment charges", "[Details]"},
                        {"Late payment charges", "[Details]"},
                        {"Other charges", "[Details]"}}),
                tip);
    }

    private VBox featuresPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        for (int i = 0; i < 2; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(50);
            grid.getColumnConstraints().add(column);
        }
        grid.add(infoCard("Eligible expenses", "[Expenses the loan can cover.]"), 0, 0);
        grid.add(infoCard("Repayment options", "[Available repayment options.]"), 1, 0);
        grid.add(infoCard("Security arrangements", "[Collateral and security details.]"), 0, 1);
        grid.add(infoCard("Repayment holiday", "[Course period and grace period details.]"), 1, 1);

        return new VBox(18, heading("Key features", "Important benefits and conditions of this loan."), grid);
    }

    private VBox faqPanel() {
        String answer = "[Answer from the bank's published information.]";
        VBox list = new VBox(10,
                faqItem("Who can apply?", answer, true),
                faqItem("Is a co-applicant required?", answer, false),
                faqItem("What expenses can the loan cover?", answer, false),
                faqItem("How does repayment work?", answer, false));
        return new VBox(18, heading("Frequently asked questions", "Quick answers about this loan."), list);
    }

    // ---------- Pieces used inside the tabs ----------
    private VBox heading(String title, String subtitle) {
        return new VBox(6, UiFactory.text(title, 20, true, INK), wrapped(subtitle, 14));
    }

    private VBox infoCard(String title, String text) {
        VBox card = new VBox(6, UiFactory.text(title, 15, true, INK), wrapped(text, 14));
        card.setPadding(new Insets(20));
        card.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:14;");
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setPrefWidth(160);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    /** A bordered table of "name  |  value" rows. */
    private VBox rowsBox(String[][] rows) {
        VBox box = new VBox();
        for (int i = 0; i < rows.length; i++) {
            Label key = UiFactory.text(rows[i][0], 14, true, INK);
            key.setMinWidth(200);
            key.setPrefWidth(200);

            Label value = wrapped(rows[i][1], 14);
            HBox.setHgrow(value, Priority.ALWAYS);

            HBox row = new HBox(24, key, value);
            row.setPadding(new Insets(16, 20, 16, 20));
            if (i > 0) {
                row.setStyle("-fx-border-color:#EEF1F6 transparent transparent transparent; -fx-border-width:1 0 0 0;");
            }
            box.getChildren().add(row);
        }
        box.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:14;");
        return box;
    }

    /** A document line with a tick box the student can use as a checklist. */
    private HBox documentRow(String name, String note) {
        CheckBox box = new CheckBox(name);
        box.setStyle(Styles.FONT + "-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:" + INK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(14, box, spacer);
        if (note != null) {
            row.getChildren().add(UiFactory.text(note, 12, false, BODY));
        }
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 18, 14, 18));
        row.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:12;");
        return row;
    }

    /** A question that opens and closes when you click it. */
    private VBox faqItem(String question, String answer, boolean open) {
        Label sign = UiFactory.text(open ? "\u2212" : "+", 18, true, BODY);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, UiFactory.text(question, 14, true, INK), spacer, sign);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(56);
        header.setMinHeight(56);
        header.setCursor(Cursor.HAND);

        Label reply = wrapped(answer, 14);
        reply.setPadding(new Insets(0, 0, 18, 0));
        reply.setVisible(open);
        reply.setManaged(open);

        header.setOnMouseClicked(e -> {
            boolean show = !reply.isVisible();
            reply.setVisible(show);
            reply.setManaged(show);
            sign.setText(show ? "\u2212" : "+");
        });

        VBox item = new VBox(header, reply);
        item.setPadding(new Insets(0, 20, 0, 20));
        item.setStyle("-fx-border-color:" + LINE + "; -fx-border-radius:12;");
        return item;
    }

    // =====================================================
    //  Right column
    // =====================================================
    private VBox buildAside() {
        return new VBox(24, buildNextStepCard(), buildBeforeApplyCard());
    }

    private VBox buildNextStepCard() {
        Button visit = actionButton("Visit Official Bank Website", true, 50);
        visit.setGraphic(icon("M7 17 17 7M8 7h9v9", "#FFFFFF", 0.75, 18));
        visit.setContentDisplay(ContentDisplay.RIGHT);
        visit.setGraphicTextGap(8);
        visit.setOnAction(e -> {
            boolean opened = controller.openBankWebsite(loan);
            if (!opened) {
                showNotice("Official website link not added yet",
                        "We have not verified an official website link for this loan yet, "
                        + "so we are not sending you anywhere. Please search for the bank's official website.");
            }
        });

        Button emi = actionButton("Calculate EMI", false, 50);
        emi.setOnAction(e -> controller.openPage(AppConstants.PAGE_EMI));   // the link

        Button brochure = actionButton("Download Brochure", false, 50);
        brochure.setOnAction(e -> showNotice("Brochure not available yet",
                "We have not verified an official brochure for this loan, so we are not showing a "
                + "download link. Visit the bank's website for the latest documents."));

        notice.setVisible(false);
        notice.setManaged(false);

        Button compare = new Button("Compare with other loans");
        compare.setMaxWidth(Double.MAX_VALUE);
        compare.setPrefHeight(44);
        compare.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-text-fill:" + BLUE + ";"
                + "-fx-font-size:14px; -fx-font-weight:bold; -fx-cursor:hand;");
        compare.setOnAction(e -> controller.openPage(AppConstants.PAGE_COMPARE));   // the link

        Label small = wrapped("If the bank has no verified brochure, we will tell you instead of "
                + "showing a broken link.", 12);

        VBox card = new VBox(14, UiFactory.text("Take the next step", 18, true, INK),
                visit, emi, brochure, notice, compare, small);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle(18));
        return card;
    }

    /** Shows the orange message box inside the "next step" card. */
    private void showNotice(String title, String text) {
        Button close = new Button("Close");
        close.setPrefHeight(44);
        close.setStyle(Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                + "-fx-font-size:13px; -fx-font-weight:bold; -fx-background-radius:10;"
                + "-fx-border-color:#D5DCE7; -fx-border-radius:10; -fx-padding:0 16; -fx-cursor:hand;");
        close.setOnAction(e -> {
            notice.setVisible(false);
            notice.setManaged(false);
        });

        notice.getChildren().setAll(UiFactory.text(title, 14, true, INK), wrapped(text, 13), close);
        notice.setPadding(new Insets(16, 18, 16, 18));
        notice.setStyle("-fx-background-color:#FFF4E5; -fx-background-radius:12;"
                + "-fx-border-color:#F3D9AE; -fx-border-radius:12;");
        notice.setVisible(true);
        notice.setManaged(true);
    }

    private StackPane buildBeforeApplyCard() {
        Label title = UiFactory.text("Before you apply", 18, true, "white");
        Label text = UiFactory.text("ScholarX provides information and sends you to the official bank website. "
                + "It does not approve loans or submit bank applications. Confirm current terms with the bank.",
                14, false, "#C9D6EA");
        text.setWrapText(true);

        VBox content = new VBox(8, title, text);
        content.setPadding(new Insets(24));

        StackPane card = new StackPane();
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color:#0F2A4A; -fx-background-radius:18;");

        Circle circle = new Circle(100, Color.rgb(255, 255, 255, 0.06));
        circle.setManaged(false);
        circle.centerXProperty().bind(card.widthProperty().subtract(50));
        circle.setCenterY(40);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        clip.widthProperty().bind(card.widthProperty());
        clip.heightProperty().bind(card.heightProperty());
        card.setClip(clip);

        card.getChildren().addAll(circle, content);
        return card;
    }

    // =====================================================
    //  Small helpers
    // =====================================================
    private String cardStyle(int radius) {
        return "-fx-background-color:white; -fx-background-radius:" + radius + ";"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:" + radius + ";";
    }

    private Label wrapped(String text, int size) {
        Label label = UiFactory.text(text, size, false, BODY);
        label.setWrapText(true);
        return label;
    }

    private Label tag(String text, String background, String color) {
        Label label = UiFactory.text(text, 12, true, color);
        label.setStyle(label.getStyle() + "-fx-background-color:" + background + ";"
                + "-fx-background-radius:6; -fx-padding:4 10;");
        return label;
    }

    private Button actionButton(String text, boolean filled, int height) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(height);
        button.setStyle(filled
                ? Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-padding:0 20; -fx-cursor:hand;"
                : Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-border-color:#D5DCE7; -fx-border-radius:12; -fx-padding:0 20; -fx-cursor:hand;");
        return button;
    }

    private Button linkButton(String text, String page) {
        Button button = new Button(text);
        button.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0; -fx-cursor:hand;"
                + "-fx-text-fill:" + BLUE + "; -fx-font-size:13px;");
        button.setOnAction(e -> controller.openPage(page));
        return button;
    }

    private StackPane bookmarkIcon(boolean filled) {
        SVGPath svg = new SVGPath();
        svg.setContent(ICON_BOOKMARK);
        svg.setFill(filled ? Color.web(BLUE) : null);
        svg.setStroke(Color.web(filled ? BLUE : INK));
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

    private StackPane arrowIcon() {
        return icon("M19 12H5M11 6l-6 6 6 6", INK, 0.75, 18);
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