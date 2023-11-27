package com.softpro.dnaig.preview;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraControlWrapper {

    private Rotate rX;
    private Rotate rY;
    private Rotate rZ;
    private Translate t;

    public CameraControlWrapper(Rotate rX, Rotate rY, Rotate rZ, Translate t) {
        this.rX = rX;
        this.rY = rY;
        this.rZ = rZ;
        this.t = t;
    }

    public void updatePivotAfterMove() {
        double x = t.getX();
        double y = t.getY();
        double z = t.getZ();
        rX.setPivotX(x);
        rX.setPivotY(y);
        rX.setPivotZ(z);
        rY.setPivotX(x);
        rY.setPivotY(y);
        rY.setPivotZ(z);
        rZ.setPivotX(x);
        rZ.setPivotY(y);
        rZ.setPivotZ(z);
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

    public Rotate getrZ() {
        return rZ;
    }

    public void setrZ(Rotate rZ) {
        this.rZ = rZ;
    }

    public Translate getT() {
        return t;
    }

    public void setT(Translate t) {
        this.t = t;
    }
}
