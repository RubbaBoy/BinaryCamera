package com.uddernetworks.bcam.image;

import java.awt.image.BufferedImage;

/**
 * A manager to handle the processing of images to be used primarily for creating {@link ProcessedImage}s.
 */
public interface ImageProcessor {

    /**
     * Gets the set brightness threshold set via {@link #setThreshold(double)}.
     *
     * @return The 0-255 brightness threshold.
     */
    double getThreshold();

    /**
     * Sets the brightness threshold for lights to be detected as on/off.
     *
     * @param threshold The brightness threshold 0-255.
     */
    void setThreshold(double threshold);

    /**
     * Initializes the processor with set values.
     */
    void initProcessor();

    /**
     * Creates a {@link ProcessedImage} from a given {@link BufferedImage}.
     *
     * @param image A raw {@link BufferedImage} directly from the camera
     * @return The resulting {@link ProcessedImage}
     */
    ProcessedImage processImage(BufferedImage image);
}
