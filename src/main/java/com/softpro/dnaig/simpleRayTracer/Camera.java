package com.softpro.dnaig.simpleRayTracer;

import com.softpro.dnaig.Output;

public class Camera {
    private int l = -Output.WIDTH/2;
    private int r = -l;
    private int t = Output.HEIGHT/2;
    private int b = -t;

    private Vector3D_RT UP = new Vector3D_RT(0, 1, 0);
    private Vector3D_RT eye = new Vector3D_RT(40, 40, -45);
    private Vector3D_RT Z = new Vector3D_RT(0, 0, 0);

    private Vector3D_RT screen;

    private Vector3D_RT W = eye.subtract(Z).normalize();
    private Vector3D_RT U = UP.cross(W).normalize();
    private Vector3D_RT V = W.cross(U).normalize();

    private double d = t/Math.tan(Math.PI/4)/2;
    private Vector3D_RT W_d_negated = W.skalarmultiplication(-d);
    private boolean test;

    public Camera(){
        screen = Util.add(eye, W.skalarmultiplication(-40));
    }


    public void setL(int l) {
        this.l = l;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setT(int t) {
        this.t = t;
    }

    public void setB(int b) {
        this.b = b;
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

    public int getL() {
        return l;
    }

    public int getR() {
        return r;
    }

    public int getT() {
        return t;
    }

    public int getB() {
        return b;
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
