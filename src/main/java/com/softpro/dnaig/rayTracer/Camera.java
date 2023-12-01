package com.softpro.dnaig.rayTracer;

import com.softpro.dnaig.Output;
import com.softpro.dnaig.utils.Vector3D;

public class Camera {
    private int l = -Output.WIDTH/2;
    private int r = -l;
    private int t = Output.HEIGHT/2;
    private int b = -t;

    private Vector3D UP = new Vector3D(0, 1, 0);
    private Vector3D eye = new Vector3D(40, 40, -45);
    private Vector3D Z = new Vector3D(0, 0, 0);

    private Vector3D screen;

    private Vector3D W = eye.subtract(Z).normalize();
    private Vector3D U = UP.crossProduct(W).normalize();
    private Vector3D V = W.crossProduct(U).normalize();

    private double d = t/Math.tan(Math.PI/4)/2;
    private Vector3D W_d_negated = W.scalarMultiplication(-d);
    private boolean test;

    public Camera(){
        screen = Util.add(eye, W.scalarMultiplication(-40));
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

    public void setUP(Vector3D UP) {
        this.UP = UP;
    }

    public void setEye(Vector3D eye) {
        this.eye = eye;
    }

    public void setZ(Vector3D z) {
        Z = z;
    }

    public void setW(Vector3D w) {
        W = w;
    }

    public void setU(Vector3D u) {
        U = u;
    }

    public void setV(Vector3D v) {
        V = v;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setW_d_negated(Vector3D w_d_negated) {
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

    public Vector3D getUP() {
        return UP;
    }

    public Vector3D getEye() {
        return eye;
    }

    public Vector3D getZ() {
        return Z;
    }

    public Vector3D getW() {
        return W;
    }

    public Vector3D getU() {
        return U;
    }

    public Vector3D getV() {
        return V;
    }

    public double getD() {
        return d;
    }

    public Vector3D getW_d_negated() {
        return W_d_negated;
    }


    public Vector3D getScreen() {
        return screen;
    }

    public void setScreen(Vector3D screen) {
        this.screen = screen;
        eye = Util.add(screen, W.scalarMultiplication(40));
    }
}