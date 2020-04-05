package com.uddernetworks.bcam.camera;

import com.github.sarxos.webcam.Webcam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CameraHandlerImpl implements CameraHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final Map<Integer, Consumer<BufferedImage>> listeners = new HashMap<>();

    private int lastId = 0;
    private double fps = 5;
    private Webcam webcam;
    private ScheduledFuture<?> masterListener;

    @Override
    public double getFPS() {
        return fps;
    }

    @Override
    public void setFPS(double fps) {
        this.fps = fps;
    }

    @Override
    public Optional<BufferedImage> singleImage() {
        if (!initWebcam()) {
            LOGGER.error("Webcam could not be opened!");
            return Optional.empty();
        }

        var img = webcam.getImage();

        return Optional.ofNullable(img);
    }

    @Override
    public int listenCamera(Consumer<BufferedImage> listener) {
        if (!initWebcam()) {
            LOGGER.error("Webcam could not be opened!");
            return -1;
        }

        if (masterListener == null) {
            this.masterListener = executor.scheduleAtFixedRate(() -> {
                if (!webcam.isImageNew()) {
                    return;
                }

                listeners.forEach((id, child) -> child.accept(webcam.getImage()));
            }, 0, (long) (1000D / fps), TimeUnit.MILLISECONDS);
        }

        listeners.put(++lastId, listener);
        return lastId;
    }

    @Override
    public void removeListener(int id) {
        listeners.remove(id);
    }

    @Override
    public void stopListening() {
        executor.shutdown();
        masterListener.cancel(true);
        listeners.clear();
    }

    /**
     * Initializes the local {@link #webcam} variable. If the webcam could not be initialized and opened, false is returned.
     *
     * @return If the webcam could be (or already has been) initialized and opened
     */
    private boolean initWebcam() {
        if (webcam == null) {
            webcam = Webcam.getDefault();
            webcam.setCustomViewSizes(new Dimension(1920, 1080));
            webcam.setViewSize(new Dimension(1920, 1080));
            return webcam.open(true);
        }

        return webcam.isOpen();
    }
}
