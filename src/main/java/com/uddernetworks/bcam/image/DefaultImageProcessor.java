package com.uddernetworks.bcam.image;

import com.uddernetworks.bcam.image.sectioner.DefaultSectionManager;
import com.uddernetworks.bcam.image.sectioner.ImageSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.List;

public class DefaultImageProcessor implements ImageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImageProcessor.class);

    private final DefaultSectionManager sectionManager;
    private double threshold = 127;
    private List<ImageSection> removed;

    public DefaultImageProcessor(DefaultSectionManager sectionManager) {
        this.sectionManager = sectionManager;
    }

    @Override
    public double getThreshold() {
        return threshold;
    }

    @Override
    public void setThreshold(double threshold) {
        this.threshold = threshold;
        LOGGER.info("Setting threshold to {}", threshold);
    }

    @Override
    public void initProcessor() {
        removed = sectionManager.getSections(false);
    }

    @Override
    public ProcessedImage processImage(BufferedImage image) {
        return new DefaultProcessedImage(image, threshold, sectionManager, removed);
    }
}
