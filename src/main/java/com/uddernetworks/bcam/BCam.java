package com.uddernetworks.bcam;

import com.uddernetworks.bcam.camera.CameraHandlerImpl;
import com.uddernetworks.bcam.image.DefaultImageProcessor;
import com.uddernetworks.bcam.image.sectioner.DefaultSectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BCam {

    private static final Logger LOGGER = LoggerFactory.getLogger(BCam.class);

    private static boolean lastState = false;

    public static void main(String[] args) {
        var handler = new CameraHandlerImpl();
        var sectionManager = new DefaultSectionManager(1920, 1080);
        var processor = new DefaultImageProcessor(sectionManager);

        var setupHandler = new SetupHandler(handler, processor, sectionManager);
        setupHandler.setup(() -> {
            handler.listenCamera(image -> {
                var processed = processor.processImage(image);
                var curr = processed.lightsOn();
                if (lastState == curr) {
                    return;
                }

                lastState = curr;

                LOGGER.info("Lights are now {}", curr ? "on" : "off");
            });
        });
    }
}
