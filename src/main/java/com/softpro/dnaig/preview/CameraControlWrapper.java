package com.softpro.dnaig.preview;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraControlWrapper {

    private Rotate rX;
    private Rotate rY;
    private Translate t;

    public CameraControlWrapper(Rotate rX, Rotate rY, Translate t) {
        this.rX = rX;
        this.rY = rY;
        this.t = t;
    }

    public Rotate getrX() {
        return rX;
    }

    public void setrX(Rotate rX) {
        this.rX = rX;
    }

    public Rotate getrY() {
        return rY;
    }

    public void setrY(Rotate rY) {
        this.rY = rY;
    }

    public Translate getT() {
        return t;
    }

    public void setT(Translate t) {
        this.t = t;
    }
}
