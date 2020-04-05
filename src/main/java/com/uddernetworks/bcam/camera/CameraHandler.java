package com.uddernetworks.bcam.camera;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Consumer;

public interface CameraHandler {

    double getFPS();

    void setFPS(double fps);

    Optional<BufferedImage> singleImage();

    int listenCamera(Consumer<BufferedImage> listener);

    void removeListener(int id);

    void stopListening();

}
