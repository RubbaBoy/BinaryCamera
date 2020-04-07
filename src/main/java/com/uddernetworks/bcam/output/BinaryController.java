package com.uddernetworks.bcam.output;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.uddernetworks.bcam.Utility.sleep;

public class BinaryController {
    private final LinkedList<Boolean> values = new LinkedList<>();
    private final Label[] labels;
    private Consumer<Boolean> resetCallback;

    public BinaryController(Label[] labels) {
        this.labels = labels;
    }

    public Label[] getLabels() {
        return labels;
    }

    public List<Boolean> getValues() {
        return values;
    }

    public void addValue(boolean value) {
        Platform.runLater(() -> {
            labels[values.size()].setText(value ? "1" : "0");
            values.add(value);
        });
    }

    public void onReset(Consumer<Boolean> resetCallback) {
        this.resetCallback = resetCallback;
    }

    public void reset(boolean successful, boolean instant) {
        resetCallback.accept(successful);

        if (instant) {
            values.clear();

            Platform.runLater(() -> {
                for (var label : labels) {
                    label.setText("");
                }
            });
        } else {
            reset(successful);
        }
    }

    public void reset(boolean successful) {
        CompletableFuture.runAsync(() -> {
            sleep(750);
            reset(successful, true);
        });
    }
}
