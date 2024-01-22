package com.softpro.dnaig.utils;

public class Config {
    public static int HEIGHT = 720;
    public static int WIDTH = 1280;
    public static int THREADS = 4;
    public static int TILES = 10;

    public static enum lightvariants{
        POINT,  //musthave
    }

    public static enum cameravariants{
        HD,
        FullHD,
        Custom
    }

    public static enum type{
        OBJECT,
        LIGHT,
        CAMERA
    }
}
