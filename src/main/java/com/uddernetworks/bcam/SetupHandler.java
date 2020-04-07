package com.uddernetworks.bcam;

import com.uddernetworks.bcam.camera.CameraHandler;
import com.uddernetworks.bcam.gui.javafx.PreviewWindow;
import com.uddernetworks.bcam.image.ImageProcessor;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SetupHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupHandler.class);

    private static final Pattern NUMBER_LIST = Pattern.compile("(\\d+)([,\\s+]*)");

    private final Stage stage;
    private final CameraHandler handler;
    private final ImageProcessor processor;
    private final SectionManager sectionManager;
    private PreviewWindow preview;

    public SetupHandler(Stage stage, CameraHandler handler, ImageProcessor processor, SectionManager sectionManager) {
        this.stage = stage;
        this.handler = handler;
        this.processor = processor;
        this.sectionManager = sectionManager;
    }

    private double onBrightness = -1;
    private double offBrightness = -1;

    public void setup(Runnable complete) throws IOException {
        processor.initProcessor();
        preview = new PreviewWindow(stage, handler, sectionManager, () -> {
            onBrightness = getCurrentBrightness();
            checkComplete(complete);
        }, () -> {
            offBrightness = getCurrentBrightness();
            checkComplete(complete);
        });

        preview.showInterface();
    }

    private void checkComplete(Runnable onComplete) {
        if (onBrightness != -1 && offBrightness != -1) {

            LOGGER.info("On: {} Off: {}", onBrightness, offBrightness);

            processor.setThreshold((onBrightness - offBrightness) / 4 + offBrightness);

            preview.close();
            onComplete.run();
        }
    }

    private List<Integer> getIntList(String list) {
        return NUMBER_LIST.matcher(list).results().filter(res -> !res.group(1).isBlank()).mapToInt(res -> Integer.parseInt(res.group(1))).boxed().collect(Collectors.toList());
    }

    private double getCurrentBrightness() {
        var single = handler.singleImage();

        if (single.isEmpty()) {
            LOGGER.error("Could not get image!");
            System.exit(0);
        }

        var proc = processor.processImage(single.get());

        return proc.getBrightness();
    }
}
