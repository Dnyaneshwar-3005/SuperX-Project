package com.corex.util;

import com.corex.config.AppConstants;
import com.corex.view.Styles;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SceneNavigator {

    private static Scene scene;
    private static final Map<String, Supplier<Parent>> pages = new HashMap<>();

    /** Called once at startup. Sets up the window. */
    public static void init(Stage stage) {
        scene = new Scene(new VBox(), AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setMinWidth(AppConstants.MIN_WIDTH);
        stage.setMinHeight(AppConstants.MIN_HEIGHT);
    }

    /** Give a page a name, for example register("login", LoginView::new). */
    public static void register(String name, Supplier<Parent> page) {
        pages.put(name, page);
    }

    /** Open a page by its name. */
    public static void go(String name) {
        Supplier<Parent> maker = pages.get(name);
        Parent page = (maker != null) ? maker.get() : placeholder(name);

        // ScrollPane lets long pages scroll up and down
        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color:" + Styles.BG + "; -fx-background:" + Styles.BG + ";");
        scene.setRoot(scroll);
    }

    /** Shown when a page has not been built yet. */
    private static Parent placeholder(String name) {
        Button back = UiFactory.filledButton("Back to Welcome", Styles.BLUE);
        back.setMaxWidth(220);
        back.setOnAction(e -> go(AppConstants.PAGE_WELCOME));

        VBox box = new VBox(16,
                UiFactory.text("The \"" + name + "\" page is coming in a later step.", 20, true, Styles.TEXT),
                back);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color:" + Styles.BG + ";");
        return box;
    }
}