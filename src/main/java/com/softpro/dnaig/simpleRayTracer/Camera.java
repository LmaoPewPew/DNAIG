package com.softpro.dnaig.simpleRayTracer;

import com.softpro.dnaig.Output;

public class Camera {
    // Screen coordinates
    private int screenLeft = -Output.WIDTH/2;
    private int screenRight = -screenLeft;
    private int screenTop = Output.HEIGHT/2;
    private int screenBottom = -screenTop;

    // Camera position
    private Vector3D_RT UP = new Vector3D_RT(0, 1, 0);
    private Vector3D_RT eye = new Vector3D_RT(40, 40, -45);
    private Vector3D_RT Z = new Vector3D_RT(0, 0, 0);

    // Screen position
    private Vector3D_RT screen;

    // Camera coordinate system
    private Vector3D_RT W = eye.subtract(Z).normalize();
    private Vector3D_RT U = UP.cross(W).normalize();
    private Vector3D_RT V = W.cross(U).normalize();

    // Distance from eye to screen
    private double d = screenTop / Math.tan(Math.PI / 4) / 2;

    // W * -d
    private Vector3D_RT W_d_negated = W.skalarmultiplication(-d);
    private boolean test;

    public Camera(){
        screen = Util.add(eye, W.skalarmultiplication(-40));
    }


    public void setScreenLeft(int l) {
        this.screenLeft = l;
    }

    public void setScreenRight(int r) {
        this.screenRight = r;
    }

    public void setScreenTop(int t) {
        this.screenTop = t;
    }

    public void setScreenBottom(int b) {
        this.screenBottom = b;
    }

    public void setUP(Vector3D_RT UP) {
        this.UP = UP;
    }

    public void setEye(Vector3D_RT eye) {
        this.eye = eye;
    }

    public void setZ(Vector3D_RT z) {
        Z = z;
    }

    public void setW(Vector3D_RT w) {
        W = w;
    }

    public void setU(Vector3D_RT u) {
        U = u;
    }

    public void setV(Vector3D_RT v) {
        V = v;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setW_d_negated(Vector3D_RT w_d_negated) {
        W_d_negated = w_d_negated;
    }

    public int getScreenLeft() {
        return screenLeft;
    }

    public int getScreenRight() {
        return screenRight;
    }

    public int getScreenTop() {
        return screenTop;
    }

    public int getScreenBottom() {
        return screenBottom;
    }

    public Vector3D_RT getUP() {
        return UP;
    }

    public Vector3D_RT getEye() {
        return eye;
    }

    public Vector3D_RT getZ() {
        return Z;
    }

    public Vector3D_RT getW() {
        return W;
    }

    public Vector3D_RT getU() {
        return U;
    }

    public Vector3D_RT getV() {
        return V;
    }

    public double getD() {
        return d;
    }

    public Vector3D_RT getW_d_negated() {
        return W_d_negated;
    }


    public Vector3D_RT getScreen() {
        return screen;
    }

    public void setScreen(Vector3D_RT screen) {
        this.screen = screen;
        eye = Util.add(screen, W.skalarmultiplication(40));
    }
}
