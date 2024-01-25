package com.softpro.dnaig.preview;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class CameraControlWrapper {

    private Rotate rX;
    private Rotate rY;
    private Rotate rZ;
    private Translate t;
    private Scale s;

    /**
     * A wrapper class for controlling the camera in a 3D view.
     */
    public CameraControlWrapper(Rotate rX, Rotate rY, Rotate rZ, Translate t, Scale s) {
        this.rX = rX;
        this.rY = rY;
        this.rZ = rZ;
        this.t = t;
        this.s = s;
    }

    /**
     * Updates the pivot points of the three rotations (rX, rY, rZ) after moving the camera or object.
     */
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

    /**
     * Retrieves the rotation object along the X-axis.
     *
     * @return The Rotate object representing rotation along the X-axis.
     */
    public Rotate getrX() {
        return rX;
    }

    /**
     * Sets the rotation object along the X-axis.
     *
     * @param rX The Rotate object representing rotation along the X-axis.
     */
    public void setrX(Rotate rX) {
        this.rX = rX;
    }

    /**
     * Retrieves the rotation object along the Y-axis.
     *
     * @return The Rotate object representing rotation along the Y-axis.
     */
    public Rotate getrY() {
        return rY;
    }

    /**
     *
     * Sets the rotation object along the Y-axis.
     *
     * @param rY The Rotate object representing rotation along the Y-axis.
     */
    public void setrY(Rotate rY) {
        this.rY = rY;
    }

    /**
     * Retrieves the rotation object along the Z-axis.
     *
     * @return The Rotate object representing rotation along the Z-axis.
     */
    public Rotate getrZ() {
        return rZ;
    }

    /**
     * Sets the rotation object along the Z-axis.
     *
     * @param rZ The Rotate object representing rotation along the Z-axis.
     */
    public void setrZ(Rotate rZ) {
        this.rZ = rZ;
    }

    /**
     * Retrieves the Translate object representing the translation of the camera or object in a 3D view.
     *
     * @return The Translate object for the camera or object translation.
     */
    public Translate getT() {
        return t;
    }

    /**
     * Sets the Translate object representing the translation of the camera or object in a 3D view.
     *
     * @param t The Translate object for the camera or object translation.
     */
    public void setT(Translate t) {
        this.t = t;
    }

    /**
     * Retrieves the Scale object representing the scaling of the camera or object in a 3D view.
     *
     * @return The Scale object for the camera or object scaling.
     */
    public Scale getS() { return s; }

    /**
     * Sets the Scale object representing the scaling of the camera or object in a 3D view.
     *
     * @param s The Scale object for the camera or object scaling.
     */
    public void setS(Scale s) { this.s = s; }
}
