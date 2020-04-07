package com.uddernetworks.bcam;

import com.uddernetworks.bcam.output.BinaryController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Toolkit;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.uddernetworks.bcam.Utility.sleep;

public class FloatingInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloatingInfo.class);

    private final int paddingX = 10;
    private final int paddingY = 10;
    private final BinaryController controller = new BinaryController(createLabels(8));

    public void start(Stage stage) {
        var hbox = new HBox(controller.getLabels());

        var hboxStyle = "-fx-background-color: #FFF; -fx-border-width: 3px;";
        hbox.setStyle(hboxStyle);
        hbox.setPadding(new Insets(5));

        controller.onReset(() -> {
            hbox.setStyle(hboxStyle + "-fx-border-color: red");
            CompletableFuture.runAsync(() -> {
                sleep(500);
                Platform.runLater(() -> hbox.setStyle(hboxStyle));
            });
        });

        stage.setAlwaysOnTop(true);

        stage.setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 210 - paddingX);
        stage.setY(paddingY);

        var scene = new Scene(hbox);
        stage.setScene(scene);

        stage.show();
    }

    private Label[] createLabels(int count) {
        return IntStream.range(0, count).mapToObj(this::getLabel).toArray(Label[]::new);
    }

    private Label getLabel(int id) {
        var label = new Label();
        label.setFont(Font.font("Consolas", 32));
        label.setStyle("-fx-text-fill: #2b2b2b");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setText("");
        label.setPrefWidth(25);
        label.setMinWidth(25);
        label.setMaxWidth(25);

        label.setPrefHeight(35);
        label.setMinHeight(35);
        label.setMaxHeight(35);

        return label;
    }

    public BinaryController getController() {
        return controller;
    }
}
