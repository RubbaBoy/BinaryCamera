package com.uddernetworks.bcam.image;

import java.awt.image.BufferedImage;

public interface ProcessedImage {

    double getBrightness();

    boolean lightsOn();

    boolean lightsOff();

    BufferedImage getImage();

}
