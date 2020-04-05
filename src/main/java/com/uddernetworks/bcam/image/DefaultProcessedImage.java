package com.uddernetworks.bcam.image;

import com.uddernetworks.bcam.image.sectioner.DefaultSectionManager;
import com.uddernetworks.bcam.image.sectioner.ImageSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

import static com.uddernetworks.bcam.image.DefaultProcessedImage.Offset.BLUE;
import static com.uddernetworks.bcam.image.DefaultProcessedImage.Offset.GREEN;
import static com.uddernetworks.bcam.image.DefaultProcessedImage.Offset.RED;

public class DefaultProcessedImage implements ProcessedImage {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessedImage.class);

    private BufferedImage image;
    private double threshold;
    private final DefaultSectionManager sectionManager;
    private final List<ImageSection> removed;

    public DefaultProcessedImage(BufferedImage image, double threshold, DefaultSectionManager sectionManager, List<ImageSection> removed) {
        this.image = image;
        this.threshold = threshold;
        this.sectionManager = sectionManager;
        this.removed = removed;
    }

    @Override
    public double getBrightness() {
        return removed.stream().flatMapToInt(section ->
                IntStream.of(image.getRGB(section.getX(), section.getY(), section.getWidth(), section.getHeight(), null, 0, section.getWidth())))
                .parallel()
                .map(DefaultProcessedImage::getBrighness)
                .average()
                .orElse(0);
    }

    @Override
    public boolean lightsOn() {
        return getBrightness() > threshold;
    }

    @Override
    public boolean lightsOff() {
        return !lightsOn();
    }

    public BufferedImage getImage() {
        return image;
    }

    private static int getBrighness(int rgb) {
        return (int) Math.floor((RED.color(rgb) * 0.299) + (GREEN.color(rgb) * 0.587) + (BLUE.color(rgb) * 0.114));
    }

    enum Offset {
        RED(16), GREEN(8), BLUE(0);

        private final int shift;

        Offset(int shift) {
            this.shift = shift;
        }

        public int getShift() {
            return shift;
        }

        public int color(int rgb) {
            return (rgb >> shift) & 0xFF;
        }
    }
}
