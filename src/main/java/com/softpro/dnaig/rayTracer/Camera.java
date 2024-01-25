package com.softpro.dnaig.rayTracer;

import com.softpro.dnaig.Output;
import com.softpro.dnaig.utils.Config;
import com.softpro.dnaig.utils.Vector3D;

import java.util.Locale;

public class Camera {
    private int l = -Config.getInstance().getWIDTH()/2;
    private int r = -l;
    private int t = Config.getInstance().getHEIGHT()/2;
    private int b = -t;
    private double fov = (45.0/360)*2*Math.PI;

    private Vector3D UP = new Vector3D(0, 1, 0);
    private Vector3D eye = new Vector3D(4, 4, -4);
    private Vector3D Z = new Vector3D(0, 0, 0);

    private Vector3D W = eye.subtract(Z).normalize();
    private Vector3D U = UP.crossProduct(W).normalize();
    private Vector3D V = W.crossProduct(U).normalize();

    private double d = t/(Math.tan(fov/2)*2);
    private Vector3D W_d_negated = W.scalarMultiplication(-d);

    private int height;
    private int width;

    public Camera(){}
    public Camera(Vector3D position, Vector3D rotation, int width, int height){
        eye = position;
        //Z = new Vector3D(0, 0, 0);
        //Vector3D dir = Z.subtract(eye);
        eye.rotate(rotation.getX(), rotation.getY(), rotation.getZ());
        //Z = new Vector3D(eye.getX(), eye.getY(), eye.getZ());
        //Z.add(dir);
        UP = new Vector3D(0, 1, 0);
        if(UP.crossProduct(eye.subtract(Z)).length()==0){
            UP = new Vector3D(0, 0, 1);
        }
        W = eye.subtract(Z).normalize();
        U = UP.crossProduct(W).normalize();
        V = W.crossProduct(U).normalize();

        d = t/(Math.tan(fov/2)*2);
        W_d_negated = W.scalarMultiplication(-d);

        this.width = width;
        this.height = height;

        /*
        l = -width/2;
        r = -l;
        t = height/2;
        b = -t;

         */
    }

    public Camera(Vector3D eye, Vector3D lookAt, Vector3D up, double fov, int width, int height) {
        this.eye = eye;
        this.Z = lookAt;
        this.UP = up;
        this.fov = fov;

        if(UP.crossProduct(eye.subtract(Z)).length()==0){
            UP = new Vector3D(0, 0, 1);
        }
        W = eye.subtract(Z).normalize();
        U = UP.crossProduct(W).normalize();
        V = W.crossProduct(U).normalize();

        d = t/(Math.tan(fov/2)*2);
        W_d_negated = W.scalarMultiplication(-d);

        this.width = width;
        this.height = height;
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

    public void setFov(double fov) {
        this.fov = fov;
        d = t/(Math.tan(fov/2)*2);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getFov() {
        return fov;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    public String toYaml(){
        return String.format(
                Locale.US,
                """
                \tposition: %s
                \tlookAt: %s
                \tupVec: %s
                \tfieldOfView: %f
                \twidth: %d
                \theight: %d       \s
                """, eye.toYaml(), Z.toYaml(), UP.toYaml(), fov, Config.getInstance().getWIDTH(), Config.getInstance().getHEIGHT()
        );
    }
}