package com.corex.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Java-only ScholarX opening screen. Call show(stage, callback) from Main.
 */
public final class SplashScreen extends Application {

    private static final Color NAVY = Color.web("#112F52");
    private static final Color LIGHT_NAVY = Color.web("#1D426A");
    private static final Color ACCENT = Color.web("#5B8CFF");
    private static final Duration LOADING_TIME = Duration.seconds(2.5);
    public static Stage primaryStage;

    public SplashScreen() {
    }

    /** Shows the splash for 2.6 seconds, then invokes nextScreen. */
    public static void show(Stage stage, Runnable nextScreen) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #112F52;");

        Pane decoration = createDecoration(root);
        VBox content = createContent();
        VBox footer = createFooter();

        // BorderPane guarantees that the footer stays at the bottom instead
        // of sharing the same central area as the branding content.
        BorderPane layout = new BorderPane();
        layout.setCenter(content);
        layout.setBottom(footer);
        layout.setPadding(new Insets(0, 0, 54, 0));
        BorderPane.setAlignment(content, Pos.CENTER);
        BorderPane.setAlignment(footer, Pos.CENTER);

        root.getChildren().addAll(decoration, layout);

        Scene scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setTitle("ScholarX");
        stage.show();

        playLoadingAnimation(content, nextScreen);
    }

    /** Entry point used by Application.launch(SplashScreen.class, args). */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        show(stage, () -> {
            primaryStage.setScene(new LoginPage().getLoginScene());
            primaryStage.setTitle("ScholarX - Login");
            primaryStage.centerOnScreen();
        });
    }

    private static Pane createDecoration(StackPane root) {
        Pane pane = new Pane();
        pane.setMouseTransparent(true);

        Circle topRight = new Circle();
        topRight.setRadius(365);
        topRight.setFill(LIGHT_NAVY.deriveColor(0, 1, 1, 0.48));
        topRight.centerXProperty().bind(root.widthProperty().add(170));
        topRight.setCenterY(-70);

        Circle bottomLeft = new Circle();
        bottomLeft.setRadius(245);
        bottomLeft.setFill(LIGHT_NAVY.deriveColor(0, 1, 1, 0.52));
        bottomLeft.setCenterX(-85);
        bottomLeft.centerYProperty().bind(root.heightProperty().add(125));

        Circle bottomRight = new Circle();
        bottomRight.setRadius(185);
        bottomRight.setFill(Color.web("#204790", 0.92));
        bottomRight.centerXProperty().bind(root.widthProperty().subtract(135));
        bottomRight.centerYProperty().bind(root.heightProperty().add(100));

        pane.getChildren().addAll(topRight, bottomLeft, bottomRight);
        return pane;
    }

    private static VBox createContent() {
        StackPane iconCard = createIconCard();

        Text scholar = new Text("Scholar");
        scholar.setFill(Color.WHITE);
        scholar.setFont(Font.font("System", FontWeight.BOLD, 54));

        Text x = new Text("X");
        x.setFill(ACCENT);
        x.setFont(Font.font("System", FontWeight.BOLD, 54));
        HBox title = new HBox(scholar, x);
        title.setAlignment(Pos.CENTER);

        Label tagline = new Label("Your Education. Our Support.");
        tagline.setTextFill(Color.web("#C8D6EB"));
        tagline.setFont(Font.font("System", FontWeight.NORMAL, 19));

        ProgressBar progress = new ProgressBar(0);
        progress.setPrefWidth(305);
        progress.setPrefHeight(8);
        progress.setMaxWidth(305);
        progress.setStyle(
                "-fx-accent: white;"
                        + "-fx-control-inner-background: #496887;"
                        + "-fx-background-radius: 8;"
                        + "-fx-background-insets: 0;");
        progress.setId("splashProgress");

        Label loading = new Label("Getting things ready");
        loading.setTextFill(Color.web("#C8D6EB"));
        loading.setFont(Font.font("System", FontWeight.NORMAL, 14));

        VBox loadingBox = new VBox(12, progress, loading);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setMaxWidth(VBox.USE_PREF_SIZE);

        VBox box = new VBox(20, iconCard, title, tagline, loadingBox);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8));
        VBox.setMargin(title, new Insets(12, 0, -8, 0));
        VBox.setMargin(box.getChildren().get(3), new Insets(42, 0, 0, 0));
        return box;
    }

    private static StackPane createIconCard() {
        Rectangle card = new Rectangle(104, 104);
        card.setArcWidth(31);
        card.setArcHeight(31);
        card.setFill(Color.web("#FCFCFD"));

        Polygon cap = new Polygon(28.0, 46.0, 52.0, 33.0, 76.0, 46.0, 52.0, 59.0);
        cap.setFill(Color.TRANSPARENT);
        cap.setStroke(NAVY);
        cap.setStrokeWidth(4.5);
        cap.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);

        Polygon base = new Polygon(39.0, 54.0, 39.0, 68.0, 52.0, 73.0, 65.0, 68.0, 65.0, 54.0, 52.0, 61.0);
        base.setFill(Color.TRANSPARENT);
        base.setStroke(NAVY);
        base.setStrokeWidth(4.5);
        base.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);

        Line tassel = new Line(76, 46, 76, 61);
        tassel.setStroke(NAVY);
        tassel.setStrokeWidth(3.5);
        tassel.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        StackPane cardPane = new StackPane(card, cap, base, tassel);
        cardPane.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 25, 0.15, 0, 7);");
        return cardPane;
    }

    private static VBox createFooter() {
        HBox chips = new HBox(12,
                chip("Education loans"), chip("Scholarships"), chip("Student benefits"));
        chips.setAlignment(Pos.CENTER);

        Label copyright = new Label("© 2026 ScholarX. All rights reserved.");
        copyright.setTextFill(Color.web("#9CB4CF"));
        copyright.setFont(Font.font("System", FontWeight.NORMAL, 13));

        VBox footer = new VBox(21, chips, copyright);
        footer.setAlignment(Pos.CENTER);
        return footer;
    }

    private static Label chip(String text) {
        Label chip = new Label(text);
        chip.setPadding(new Insets(9, 19, 9, 19));
        chip.setTextFill(Color.web("#D5E2F3"));
        chip.setFont(Font.font("System", FontWeight.NORMAL, 13));
        chip.setStyle("-fx-border-color: #55718F; -fx-border-radius: 20;"
                + "-fx-background-radius: 20; -fx-border-width: 1.2;");
        return chip;
    }

    private static void playLoadingAnimation(VBox content, Runnable nextScreen) {
        ProgressBar progress = (ProgressBar) content.lookup("#splashProgress");
        Timeline loading = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress.progressProperty(), 0)),
                new KeyFrame(LOADING_TIME, new KeyValue(progress.progressProperty(), 1)));
        loading.play();

        if (nextScreen != null) {
            PauseTransition pause = new PauseTransition(LOADING_TIME);
            pause.setOnFinished(event -> nextScreen.run());
            pause.play();
        }
    }
}
