package com.uddernetworks.bcam.image;

import java.awt.image.BufferedImage;

public interface ImageProcessor {

    double getThreshold();

    void setThreshold(double threshold);

    void initProcessor();

    ProcessedImage processImage(BufferedImage image);
}
