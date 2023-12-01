package com.softpro.dnaig.utils;

public class Config {
    public static int HEIGHT =720;
    public static int WIDTH = 1280;

    public static enum lightvariants{
        POINT,  //musthave
        SPOT,   //maybe implemented
        SUN,    //maybe implemented
        AREA    //maybe implemented
    }

    public static enum cameravariants{
        CAM1,
        CAM2,
        CAM3,
        CAM4
    }

    public static enum type{
        OBJECT,
        LIGHT,
        CAMERA
    }
}
