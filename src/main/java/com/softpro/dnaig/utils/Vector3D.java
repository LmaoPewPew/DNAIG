package com.softpro.dnaig.utils;

import com.softpro.dnaig.rayTracer.Util;

public class Vector3D {

    private double x, y, z;



    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }


    public void add(Vector3D v) {
        x += v.getX();
        y += v.getY();
        z += v.getZ();

    }


    public Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.getX(), y - v.getY(), z - v.getZ());
    }

    public Vector3D normalize() {
        double l = length();
        if (l != 0) {
            return new Vector3D(x / l, y / l, z / l);
        }
        return new Vector3D();
    }


    public Vector3D crossProduct(Vector3D v) {
        return new Vector3D(y * v.getZ() - v.getY() * z, z * v.getX() - v.getZ() * x, x * v.getY() - v.getX() * y);
    }


    public double length() {
        return this.scalarProduct(this);
    }


    public void setX(double x) {
        this.x = x;
    }


    public void setY(double y) {
        this.y = y;
    }


    public void setZ(double z) {
        this.z = z;
    }


    public double scalarProduct(Vector3D v) {
        double tmp = x * v.getX() + y * v.getY() + z * v.getZ();
        if (tmp == 0) {
            return 0;
        }
        return Math.sqrt(tmp);
    }


    public Vector3D scalarMultiplication(double d) {
        return new Vector3D(x * d, y * d, z * d);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double product(Vector3D v) {
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public Vector3D move(double epsilon, Vector3D positionToLight) {
        return Util.add(this, positionToLight.scalarMultiplication(epsilon));
    }

    public Vector3D multiply(Vector3D b) {
        return new Vector3D(x * b.getX(), y * b.getY(), z * b.getZ());
    }

    public void setValue(int idx, double value) {
        switch (idx) {
            case 0:
                x = value;
                return;
            case 1:
                y = value;
                return;
            case 2:
                z = value;
                return;
            default:
        }
    }

    public double getValue(int idx) {
        return switch (idx) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> Double.MAX_VALUE;
        };
    }

    @Override
    public String toString() {
        return String.format("x: %f\t y: %f\t z: %f", x, y, z);
    }
}
