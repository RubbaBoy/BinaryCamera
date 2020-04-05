package com.uddernetworks.bcam;

import com.uddernetworks.bcam.camera.CameraHandler;
import com.uddernetworks.bcam.gui.SwingInterface;
import com.uddernetworks.bcam.image.ImageProcessor;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SetupHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupHandler.class);

    private static final Pattern NUMBER_LIST = Pattern.compile("(\\d+)([,\\s+]*)");

    private CameraHandler handler;
    private ImageProcessor processor;
    private SectionManager sectionManager;

    public SetupHandler(CameraHandler handler, ImageProcessor processor, SectionManager sectionManager) {
        this.handler = handler;
        this.processor = processor;
        this.sectionManager = sectionManager;
    }

    private Phase phase = Phase.SELECTING;
    private double initialBrightness = -1;
    private double offBrightness = -1;

    public void setup(Runnable complete) {
        var in = new SwingInterface(handler, sectionManager, swingInterface -> {
            switch (phase) {
                case SELECTING:
                    processor.initProcessor();

                    initialBrightness = getCurrentBrightness();

                    LOGGER.info("Turn your lights OFF and press enter/escape...");
                    phase = Phase.LIGHTS_OFF;
                    break;
                case LIGHTS_OFF:
                    phase = Phase.DONE;

                    offBrightness = getCurrentBrightness();
                    LOGGER.info("Turn your lights ON and press enter/escape...");

                    LOGGER.info("Initial: {} Off: {}", initialBrightness, offBrightness);

                    processor.setThreshold((initialBrightness - offBrightness) / 4 + offBrightness);

                    swingInterface.close();
                    complete.run();
                    break;
            }
        });

        LOGGER.info("Select what sections you want to be detected. Green means detected, red means ignored. Press 'I' to invert selection. Press enter/escape to continue.");
        in.showInterface();
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

    enum Phase {
        SELECTING,
        LIGHTS_OFF,
        DONE
    }

}
