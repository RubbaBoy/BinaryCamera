package com.uddernetworks.bcam.output;

import com.uddernetworks.bcam.FloatingInfo;
import com.uddernetworks.bcam.Utility;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.uddernetworks.bcam.Utility.binToDec;
import static com.uddernetworks.bcam.Utility.sleep;

public class RobotOutput implements OutputHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RobotOutput.class);

    private final ExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private final LightQueue lightQueue = new LightQueue(8, 500);
    private final FloatingInfo floatingInfo;
    private final RobotController robotController;

    private long lastOff = -1;
    private Future<?> timeout;

    public RobotOutput() throws AWTException {
        CompletableFuture.runAsync(FloatingInfo::launchStuff);

        floatingInfo = FloatingInfo.getInstance();

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
            LOGGER.info("Starting on empty queue");
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

    static class LightQueue {

        private final List<Boolean> buffer = new LinkedList<>();
        private final int byteSize;
        private final long time;

        private Consumer<LinkedList<Boolean>> callback;
        private Consumer<Boolean> singleCallback;

        public LightQueue(int byteSize, long time) {
            this.byteSize = byteSize;
            this.time = time;
        }

        public void listenSingle(Consumer<Boolean> singleCallback) {
            this.singleCallback = singleCallback;
        }

        public void listenMax(Consumer<LinkedList<Boolean>> callback) {
            this.callback = callback;
        }

        public void add(ToggleState element) {
            var val = element.getTimeOn() > time;
            buffer.add(val);
            singleCallback.accept(val);

            if (buffer.size() == byteSize) {
                callback.accept(new LinkedList<>(buffer));
                buffer.clear();
            }
        }

        public void reset() {
            buffer.clear();
        }
    }

    static class ToggleState {
        private long timeOn;

        public ToggleState(long timeOn) {
            this.timeOn = timeOn;
        }

        public long getTimeOn() {
            return timeOn;
        }
    }

}
