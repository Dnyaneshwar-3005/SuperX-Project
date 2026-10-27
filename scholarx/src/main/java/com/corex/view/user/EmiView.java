package com.corex.view.user;

import com.corex.config.AppConstants;
import com.corex.controller.user.EmiController;
import com.corex.service.EmiCalculator;
import com.corex.util.UiFactory;
import com.corex.view.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.DoubleConsumer;

/** The content of the EMI Calculator page (sidebar and top bar come from MainLayoutView). */
public class EmiView extends VBox {

    private static final String BLUE = "#1A4FC0";
    private static final String INK  = "#111827";
    private static final String BODY = "#4B5563";
    private static final String LINE = "#E5E9F0";
    private static final String NAVY = "#0F2A4A";

    private final EmiController controller = new EmiController();

    // The three inputs (starting values are examples only)
    private final NumberInput amountInput;
    private final NumberInput rateInput;
    private final NumberInput yearsInput;

    // Result labels that change when you move a slider
    private final Label emiLabel = UiFactory.text("", 44, true, "white");
    private final Label principalLabel = resultValue();
    private final Label interestLabel = resultValue();
    private final Label totalLabel = resultValue();
    private final Label monthsLabel = resultValue();

    // The two-color bar (principal / interest)
    private final ColumnConstraints principalColumn = new ColumnConstraints();
    private final ColumnConstraints interestColumn = new ColumnConstraints();

    public EmiView() {
        setSpacing(24);

        // slider min, slider max, step, box min, box max, decimals, start value, what to do on change
        amountInput = new NumberInput(100000, 5000000, 50000, 100000, 10000000, 0, 1000000, v -> update());
        rateInput   = new NumberInput(1, 20, 0.1, 1, 20, 1, 10, v -> update());
        yearsInput  = new NumberInput(1, 20, 1, 1, 20, 0, 10, v -> update());

        getChildren().addAll(buildTitleArea(), buildMainRow(), buildBottomRow());
        update();
    }

    // =====================================================
    //  Breadcrumb, title and Back button
    // =====================================================
    private VBox buildTitleArea() {
        Button dashboard = linkButton("Dashboard", AppConstants.PAGE_DASHBOARD);
        Button loans = linkButton("Education Loans", AppConstants.PAGE_LOANS);
        HBox breadcrumb = new HBox(8, dashboard, UiFactory.text("/", 13, false, BODY),
                loans, UiFactory.text("/", 13, false, BODY),
                UiFactory.text("EMI Calculator", 13, false, BODY));
        breadcrumb.setAlignment(Pos.CENTER_LEFT);

        Label title = UiFactory.text("EMI Calculator", 34, true, INK);
        Label sub = UiFactory.text("Estimate your monthly repayment before you borrow.", 15, false, BODY);

        Button back = new Button("Back to Education Loans");
        back.setGraphic(icon("M19 12H5M11 6l-6 6 6 6", INK, 0.75, 18));
        back.setGraphicTextGap(8);
        back.setPrefHeight(48);
        back.setStyle(Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:12; -fx-padding:0 22; -fx-cursor:hand;");
        back.setOnAction(e -> controller.openPage(AppConstants.PAGE_LOANS));   // the link

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(20, new VBox(4, title, sub), spacer, back);
        titleRow.setAlignment(Pos.BOTTOM_LEFT);

        return new VBox(10, breadcrumb, titleRow);
    }

    // =====================================================
    //  Inputs (left) and result (right)
    // =====================================================
       private HBox buildMainRow() {
        VBox inputs = buildInputsCard();
        StackPane result = buildResultCard();
        
        inputs.setPrefWidth(560);
        inputs.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inputs, Priority.ALWAYS);

        result.setPrefWidth(400);
        result.setMinWidth(340);
        result.setMaxWidth(460);
        HBox.setHgrow(result, Priority.ALWAYS);

        return new HBox(24, inputs, result);
    }

    private VBox buildInputsCard() {
        VBox heading = new VBox(6,
                UiFactory.text("Loan details", 22, true, INK),
                UiFactory.text("Enter your loan details or move the sliders. Starting values are examples only.",
                        14, false, BODY));

        VBox card = new VBox(30, heading,
                amountInput.build("Loan amount", "\u20B9", null, "\u20B9 1 lakh", "\u20B9 50 lakh"),
                rateInput.build("Annual interest rate", null, "% p.a.", "1%", "20%"),
                yearsInput.build("Repayment tenure", null, "years", "1 year", "20 years"));
        card.setPadding(new Insets(28));
        card.setStyle(cardStyle());
        return card;
    }

    private StackPane buildResultCard() {
        Label heading = UiFactory.text("Your estimated repayment", 18, true, "white");

        VBox emiBlock = new VBox(4, UiFactory.text("Monthly EMI", 13, false, "#C9D6EA"), emiLabel);

        // Bar: blue part = principal, orange part = interest
        Region principalPart = new Region();
        principalPart.setMinHeight(12);
        principalPart.setStyle("-fx-background-color:#7FA8F5;");
        Region interestPart = new Region();
        interestPart.setMinHeight(12);
        interestPart.setStyle("-fx-background-color:#F5B971;");

        GridPane bar = new GridPane();
        bar.getColumnConstraints().addAll(principalColumn, interestColumn);
        bar.add(principalPart, 0, 0);
        bar.add(interestPart, 1, 0);
        bar.setMinHeight(12);
        bar.setMaxHeight(12);
        bar.setStyle("-fx-background-color:rgba(255,255,255,0.15); -fx-background-radius:6;");
        Rectangle barClip = new Rectangle();
        barClip.setArcWidth(12);
        barClip.setArcHeight(12);
        barClip.widthProperty().bind(bar.widthProperty());
        barClip.heightProperty().bind(bar.heightProperty());
        bar.setClip(barClip);

        HBox legend = new HBox(18, legendItem("#7FA8F5", "Principal"), legendItem("#F5B971", "Interest"));
        VBox barBlock = new VBox(10, bar, legend);

        VBox rows = new VBox(
                resultRow("Principal amount", principalLabel),
                resultRow("Total interest payable", interestLabel),
                resultRow("Total repayment", totalLabel),
                resultRow("Number of EMIs", monthsLabel));

        Label note = UiFactory.text("Results are estimates and may differ from the bank's actual repayment schedule.",
                12, false, "#C9D6EA");
        note.setWrapText(true);

        VBox content = new VBox(22, heading, emiBlock, barBlock, rows, note);
        content.setPadding(new Insets(28));

        // Soft decorative circle and rounded clipping
        StackPane card = new StackPane();
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color:" + NAVY + "; -fx-background-radius:18;");
        Circle circle = new Circle(130, Color.rgb(255, 255, 255, 0.06));
        circle.setManaged(false);
        circle.centerXProperty().bind(card.widthProperty().subtract(70));
        circle.setCenterY(50);
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
    //  Two shortcut cards at the bottom
    // =====================================================
    private HBox buildBottomRow() {
        HBox row = new HBox(24,
                shortcutCard("Not sure which loan to use?", "Browse loans that match your details.",
                        "View loans", AppConstants.PAGE_LOANS, true),
                shortcutCard("Want to see every bank?", "Compare rates and terms side by side.",
                        "Compare loans", AppConstants.PAGE_COMPARE, false));
        return row;
    }

    private HBox shortcutCard(String title, String text, String buttonText, String page, boolean filled) {
        Label description = UiFactory.text(text, 14, false, BODY);
        description.setWrapText(true);
        VBox texts = new VBox(4, UiFactory.text(title, 17, true, INK), description);
        HBox.setHgrow(texts, Priority.ALWAYS);

        Button button = new Button(buttonText);
        button.setPrefHeight(48);
        button.setStyle(filled
                ? Styles.FONT + "-fx-background-color:" + BLUE + "; -fx-text-fill:white;"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-padding:0 22; -fx-cursor:hand;"
                : Styles.FONT + "-fx-background-color:white; -fx-text-fill:" + INK + ";"
                  + "-fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:12;"
                  + "-fx-border-color:#D5DCE7; -fx-border-radius:12; -fx-padding:0 22; -fx-cursor:hand;");
        button.setOnAction(e -> controller.openPage(page));   // the link

        HBox card = new HBox(16, texts, button);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(24));
        card.setStyle(cardStyle());
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setPrefWidth(340);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    // =====================================================
    //  The calculation: runs every time an input changes
    // =====================================================
    private void update() {
        EmiCalculator.Result result = controller.calculate(
                amountInput.getValue(), rateInput.getValue(), yearsInput.getValue());

        emiLabel.setText(rupees(result.getEmi()));
        principalLabel.setText(rupees(result.getPrincipal()));
        interestLabel.setText(rupees(result.getInterest()));
        totalLabel.setText(rupees(result.getTotal()));
        monthsLabel.setText(String.valueOf(result.getMonths()));

        double percent = (result.getTotal() > 0) ? result.getPrincipal() / result.getTotal() * 100 : 100;
        percent = Math.max(1, Math.min(99, percent));
        principalColumn.setPercentWidth(percent);
        interestColumn.setPercentWidth(100 - percent);
    }

    /** 1000000 becomes "₹ 10,00,000" (Indian digit grouping). */
    private static String rupees(double value) {
        NumberFormat format = NumberFormat.getIntegerInstance(new Locale("en", "IN"));
        return "\u20B9 " + format.format(Math.round(value));
    }

    // =====================================================
    //  Small helpers
    // =====================================================
    private static Label resultValue() {
        return UiFactory.text("", 15, true, "white");
    }

    private HBox resultRow(String label, Label value) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(12, UiFactory.text(label, 14, false, "#C9D6EA"), spacer, value);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 0, 14, 0));
        row.setStyle("-fx-border-color:rgba(255,255,255,0.16) transparent transparent transparent;"
                + "-fx-border-width:1 0 0 0;");
        return row;
    }

    private HBox legendItem(String color, String text) {
        Region square = new Region();
        square.setMinSize(10, 10);
        square.setMaxSize(10, 10);
        square.setStyle("-fx-background-color:" + color + "; -fx-background-radius:3;");
        HBox item = new HBox(8, square, UiFactory.text(text, 13, false, "#E3EAF2"));
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private String cardStyle() {
        return "-fx-background-color:white; -fx-background-radius:18;"
                + "-fx-border-color:" + LINE + "; -fx-border-radius:18;";
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

    // =====================================================
    //  One input group: label, number box and slider that stay in sync
    // =====================================================
    private static class NumberInput {

        private final Slider slider;
        private final TextField field = new TextField();
        private final double fieldMin;
        private final double fieldMax;
        private final int decimals;
        private final DoubleConsumer onChange;
        private boolean syncing = false;   // stops the slider and box from triggering each other
        private double value;

        NumberInput(double sliderMin, double sliderMax, double step, double fieldMin, double fieldMax,
                    int decimals, double start, DoubleConsumer onChange) {
            this.fieldMin = fieldMin;
            this.fieldMax = fieldMax;
            this.decimals = decimals;
            this.onChange = onChange;
            this.value = start;

            slider = new Slider(sliderMin, sliderMax, start);
            slider.setMajorTickUnit(step);
            slider.setMinorTickCount(0);
            slider.setSnapToTicks(true);

            field.setText(format(start));

            // Slider moved -> update the box
            slider.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (syncing) return;
                value = round(newValue.doubleValue());
                syncing = true;
                field.setText(format(value));
                syncing = false;
                onChange.accept(value);
            });

            // Box edited (Enter or click away) -> update the slider
            field.setOnAction(e -> commit());
            field.focusedProperty().addListener((obs, was, isNow) -> {
                if (!isNow) commit();
            });
        }

        double getValue() {
            return value;
        }

        private void commit() {
            double typed;
            try {
                typed = Double.parseDouble(field.getText().replace(",", "").trim());
            } catch (NumberFormatException e) {
                typed = value;   // not a number: keep the old value
            }
            value = round(Math.max(fieldMin, Math.min(fieldMax, typed)));

            syncing = true;
            slider.setValue(value);
            field.setText(format(value));
            syncing = false;
            onChange.accept(value);
        }

        private double round(double v) {
            double factor = Math.pow(10, decimals);
            return Math.round(v * factor) / factor;
        }

        private String format(double v) {
            return String.format(Locale.US, "%." + decimals + "f", v);
        }

        /** Builds the visible group. prefix/suffix are the small texts around the number box. */
        Node build(String labelText, String prefix, String suffix, String minText, String maxText) {
            field.setStyle(Styles.FONT + "-fx-background-color:transparent; -fx-padding:0;"
                    + "-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:" + INK + ";"
                    + "-fx-focus-color:transparent; -fx-faint-focus-color:transparent;");
            HBox.setHgrow(field, Priority.ALWAYS);

            HBox box = new HBox(6);
            box.setAlignment(Pos.CENTER_LEFT);
            if (prefix != null) box.getChildren().add(UiFactory.text(prefix, 15, false, BODY));
            box.getChildren().add(field);
            if (suffix != null) box.getChildren().add(UiFactory.text(suffix, 14, false, BODY));
            box.setPrefSize(200, 48);
            box.setMinSize(200, 48);
            box.setMaxSize(200, 48);
            box.setPadding(new Insets(0, 14, 0, 14));
            box.setStyle("-fx-background-color:white; -fx-background-radius:10;"
                    + "-fx-border-color:#D5DCE7; -fx-border-radius:10;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox top = new HBox(12, UiFactory.text(labelText, 15, true, INK), spacer, box);
            top.setAlignment(Pos.CENTER_LEFT);

            Region gap = new Region();
            HBox.setHgrow(gap, Priority.ALWAYS);
            HBox ends = new HBox(UiFactory.text(minText, 12, false, BODY), gap,
                    UiFactory.text(maxText, 12, false, BODY));

            slider.setMaxWidth(Double.MAX_VALUE);
            return new VBox(12, top, slider, ends);
        }
    }
}