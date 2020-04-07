package com.uddernetworks.bcam.image;

import java.awt.image.BufferedImage;

/**
 * Holds a {@link BufferedImage} and processes it to detect things like if a light is on/off.
 */
public interface ProcessedImage {

    /**
     * Gets the brightness of the image based off of the class's implementation.
     *
     * @return The 0-255 brightness of the image
     */
    double getBrightness();

    /**
     * Gets if the lights are on based off of the {@link #getBrightness()}.
     *
     * @return If the lights are on
     */
    boolean lightsOn();

    /**
     * Gets if the lights are off based off of the {@link #getBrightness()}.
     *
     * @return If the lights are off
     */
    boolean lightsOff();

    /**
     * Gets the original camera {@link BufferedImage} the other methods are using.
     *
     * @return The original camera {@link BufferedImage}
     */
    BufferedImage getImage();

}
