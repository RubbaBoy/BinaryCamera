package com.uddernetworks.bcam;

import com.uddernetworks.bcam.output.BinaryController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static com.uddernetworks.bcam.Utility.sleep;

public class FloatingInfo extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloatingInfo.class);

    private static FloatingInfo instance;
    private static AtomicBoolean initialized = new AtomicBoolean();

    private final int paddingX = 10;
    private final int paddingY = 10;
    private final BinaryController controller = new BinaryController(createLabels(8));

    public FloatingInfo() {
        instance = this;
        initialized.set(true);
    }

    @Override
    public void start(Stage stage) {
        var hbox = new HBox(controller.getLabels());

        hbox.setStyle("-fx-background-color: #FFF");
        hbox.setPadding(new Insets(5));

        stage.initStyle(StageStyle.TRANSPARENT);
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

    public static FloatingInfo getInstance() {
        while (true) {
            if (!initialized.get()) {
                sleep(100);
                continue;
            }

            return instance;
        }
    }

    public static void launchStuff() {
        launch();
    }
}
