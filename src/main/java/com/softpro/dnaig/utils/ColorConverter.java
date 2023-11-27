package com.softpro.dnaig.utils;

import javafx.scene.paint.Color;

public class ColorConverter {


    public static int colorToRGBConverter(Color color){
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return
                (red << 16) & 0xFF0000 | (green << 8) & 0xFF00 | blue & 0xFF;
    }

    public static Color rgbToColorConverter(int rgb){
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb) & 0xFF;

        return Color.rgb(red, green, blue);
    }
}
