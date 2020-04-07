package com.uddernetworks.bcam.output;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.concurrent.CompletableFuture;

import static com.uddernetworks.bcam.Utility.sleep;

public class RobotController {

    private final KeyConverter keyConverter = new KeyConverter();
    private final Robot robot;

    public RobotController() throws AWTException {
        this.robot = new Robot();
    }

    public void clickKey(int key) {
        CompletableFuture.runAsync(() -> {
            var keys = keyConverter.getKeyCombo(key);

            for (var code : keys) {
                robot.keyPress(code);
            }

            sleep(100);

            for (int i = keys.size() - 1; i >= 0; i--) {
                robot.keyRelease(keys.get(i));
            }
        });
    }
}
