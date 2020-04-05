package com.uddernetworks.bcam;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

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

}
