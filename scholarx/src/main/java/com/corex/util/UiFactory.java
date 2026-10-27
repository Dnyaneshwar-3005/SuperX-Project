package com.corex.util;

import com.corex.view.Styles;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class UiFactory {

    /** A single-line label (titles, small text). */
    public static Label text(String content, int size, boolean bold, String color) {
        Label label = new Label(content);
        label.setStyle(Styles.FONT + "-fx-font-size:" + size + "px;"
                + "-fx-font-weight:" + (bold ? "bold" : "normal") + ";"
                + "-fx-text-fill:" + color + ";");
        return label;
    }

    /** A centered paragraph that wraps onto several lines. */
    public static Label paragraph(String content, int size, String color) {
        Label label = text(content, size, false, color);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    /** A large filled button. */
    public static Button filledButton(String content, String color) {
        Button button = new Button(content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(50);
        button.setStyle(Styles.FONT + "-fx-background-color:" + color + ";"
                + "-fx-text-fill:white; -fx-font-size:15px; -fx-font-weight:bold;"
                + "-fx-background-radius:10; -fx-cursor:hand;");
        return button;
    }
}