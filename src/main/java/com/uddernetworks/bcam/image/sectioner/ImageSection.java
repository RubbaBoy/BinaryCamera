package com.uddernetworks.bcam.image.sectioner;

import com.uddernetworks.bcam.Utility;

import java.awt.Graphics;

public class ImageSection {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int index;

    public ImageSection(int x, int y, int width, int height, int index) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.index = index;
    }

    public void drawTo(Graphics graphics, boolean text) {
        graphics.drawRect(x, y, width, height);

        if (text) {
            Utility.drawTo(graphics, index, x + (width * 0.25), y + (height * 0.25));
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIndex() {
        return index;
    }

    public boolean isWithin(int x, int y) {
        return x > this.x && x < this.x + width &&
                y > this.y && y < this.y + height;
    }

    @Override
    public String toString() {
        return "ImageSection{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", index=" + index +
                '}';
    }
}
