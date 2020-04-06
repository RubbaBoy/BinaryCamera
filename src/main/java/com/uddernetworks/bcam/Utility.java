package com.uddernetworks.bcam;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Utility {

    public static BufferedImage copyImage(BufferedImage image) {
        var colorModel = image.getColorModel();
        return new BufferedImage(colorModel, image.copyData(null), colorModel.isAlphaPremultiplied(), null);
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        var out2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        out2.getGraphics().drawImage(image, 0, 0, width, height, null);
        return out2;
    }

    public static void drawTo(Graphics graphics, Object text, double x, double y) {
        graphics.drawString(text.toString(), Double.valueOf(x).intValue(), Double.valueOf(y).intValue());
    }

    public static byte[] toUnboxedBytes(Byte[] array) {
        var out = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        return out;
    }

    public static int binToDec(LinkedList<Boolean> input) {
        var num = 0;

        for (int i = 0, size = input.size(); i < size; i++) {
            num += input.removeLast() ? Math.pow(2, i) : 0;
        }

        return num;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}
