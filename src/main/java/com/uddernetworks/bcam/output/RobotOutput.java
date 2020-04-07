package com.uddernetworks.bcam.output;

import com.uddernetworks.bcam.FloatingInfo;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.AWTException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.uddernetworks.bcam.Utility.binToDec;

public class RobotOutput implements OutputHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RobotOutput.class);

    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final LightQueue lightQueue = new LightQueue(8, 500);
    private final FloatingInfo floatingInfo;
    private final RobotController robotController;

    private long lastOff = -1;
    private Future<?> timeout;

    public RobotOutput(Stage stage) throws AWTException {
        floatingInfo = new FloatingInfo();
        floatingInfo.start(stage);

        lightQueue.listenSingle(floatingInfo.getController()::addValue);

        robotController = new RobotController();


        lightQueue.listenMax(list -> {
            floatingInfo.getController().reset();

            clearTimeout();

            LOGGER.info("Got queue {}", list);

            var ascii = binToDec(list);

            LOGGER.info("Printing {}", (char) ascii);

            robotController.clickKey(ascii);
        });
    }

    @Override
    public void handleToggle(boolean lightsOn) {
        if (lastOff == -1) {
            lastOff = System.currentTimeMillis();
            return;
        }

        if (!lightsOn) {
            var time = System.currentTimeMillis() - lastOff;
            LOGGER.info("Light was on for {}ms", time);
            lightQueue.add(new ToggleState(time));
        } else {
            lastOff = System.currentTimeMillis();
        }

        setTimeout();
    }

    private void clearTimeout() {
        if (timeout != null) {
            timeout.cancel(true);
        }
    }

    private void setTimeout() {
        clearTimeout();

        timeout = service.submit(() -> {
            try {
                Thread.sleep(5000);
                LOGGER.info("Current character has been reset due to inactivity.");
                lastOff = -1;
                lightQueue.reset();
                floatingInfo.getController().reset(true);
            } catch (InterruptedException ignored) {
            }
        });
    }

}
