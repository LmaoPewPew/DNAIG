package com.softpro.dnaig.utils;

public class Config {
    private int HEIGHT = 720;
    private int WIDTH = 1280;
    private int THREADS = 4;
    private int TILES = 10;

    private static Config instance;

    public Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

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

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getTHREADS() {
        return THREADS;
    }

    public int getTILES() {
        return TILES;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public void setTHREADS(int THREADS) {
        this.THREADS = THREADS;
    }

    public void setTILES(int TILES) {
        this.TILES = TILES;
    }
}
