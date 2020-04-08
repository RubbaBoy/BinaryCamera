package com.uddernetworks.bcam.camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Handles camera input.
 */
public interface CameraHandler {

    /**
     * Gets the set FPS. By default, this is 30.
     *
     * @return The set FPS
     */
    double getFPS();

    /**
     * Sets the FPS the camera should be read at. Once a {@link #listenCamera(Consumer)} is invoked, changes to the FPS
     * will have no effect.
     *
     * @param fps The FPS to set
     */
    void setFPS(double fps);

    /**
     * Gets whatever single image is being displayed by the camera, even if it is a duplicate of another image gotten.
     *
     * @return The image, or an empty {@link Optional} if an error occurred
     */
    Optional<BufferedImage> singleImage();

    /**
     * Listens to the camera, returning a listener ID. This may be called multiple times. The FPS set by
     * {@link #setFPS(double)} or the default 30 does NOT ensure how often this is called. In the default
     * implementation, this is only invoked when a new image occurs and is simply checked at the interval of the FPS.
     *
     * @param listener The listener, invoked when a new image is received following the above rules
     * @return The listener ID which may be used for removal via {@link #removeListener(int)}
     */
    int listenCamera(Consumer<BufferedImage> listener);

    /**
     * Removes and stops the listener with the given ID.
     *
     * @param id The listener ID gotten via {@link #listenCamera(Consumer)}
     */
    void removeListener(int id);

    /**
     * Gets the real dimensions of the camera, initializing it if necessary.
     *
     * @return The active dimensions of the camera
     */
    Dimension getCameraDimensions();

    /**
     * Stops all listeners and webcam activity.
     */
    void stopListening();

}
