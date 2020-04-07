package com.uddernetworks.bcam;

import com.uddernetworks.bcam.camera.CameraHandlerImpl;
import com.uddernetworks.bcam.image.DefaultImageProcessor;
import com.uddernetworks.bcam.image.sectioner.DefaultSectionManager;
import com.uddernetworks.bcam.output.RobotOutput;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BCam extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(BCam.class);

    private static boolean lastState = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);

        var handler = new CameraHandlerImpl();
        var sectionManager = new DefaultSectionManager(1920, 1080);
        var processor = new DefaultImageProcessor(sectionManager);

        var setupHandler = new SetupHandler(stage, handler, processor, sectionManager);
        setupHandler.setup(() -> {
            try {
                var output = new RobotOutput(stage);

                handler.listenCamera(image -> {
                    var processed = processor.processImage(image);

                    var curr = processed.lightsOn();

                    if (lastState == curr) {
                        return;
                    }

                    lastState = curr;

                    output.handleToggle(curr);

                    LOGGER.info("Lights are now {}", curr ? "on" : "off");
                });
            } catch (Exception e) {
                LOGGER.error("Error after setup", e);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
