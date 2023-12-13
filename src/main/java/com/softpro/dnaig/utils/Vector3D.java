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

    public void rotate(double angleX, double angleY, double angleZ){
        double cos = Math.cos(angleX);
        double sin = Math.sin(angleX);
        double[][] rotationX = {
                {1, 0, 0},
                {0, cos, -sin},
                {0, sin, cos}
        };

        cos = Math.cos(angleY);
        sin = Math.sin(angleY);
        double[][] rotationY = {
                {cos, 0, sin},
                {0, 1, 0},
                {-sin, 0, cos}
        };

        cos = Math.cos(angleZ);
        sin = Math.sin(angleZ);
        double[][] rotationZ = {
                {cos, -sin, 0},
                {sin, cos, 0},
                {0, 0, 1}
        };

        double[][] rotationMatrix = multiplyMatrices(rotationX, rotationY, rotationZ);
        double[] result = multiplyMatrixWithVector(rotationMatrix, new double[]{x, y, z});

        x = result[0];
        y = result[1];
        z = result[2];
    }

    private double[] multiplyMatrixWithVector(double[][] rotationMatrix, double[] doubles) {
        int rows = rotationMatrix.length;
        int cols = rotationMatrix[0].length;
        double[] result = new double[rows];

        for (int i = 0; i < rows; i++) {
            double sum = 0;
            for (int j = 0; j < cols; j++) {
                sum += rotationMatrix[i][j] * doubles[j];
            }
            result[i] = sum;
        }

        return result;
    }

    private double[][] multiplyMatrices(double[][] rotationX, double[][] rotationY, double[][] rotationZ) {
        int rowsA = rotationX.length;
        int colsA = rotationX[0].length;
        int colsB = rotationY[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += rotationX[i][k] * rotationY[k][j];
                }
            }
        }

        return result;
    }

    public void rotateX(double angle){
        double rY = Math.cos(angle) * y - Math.sin(angle) * z;
        double rZ = Math.sin(angle) * y + Math.cos(angle) * z;

        y = rY;
        z = rZ;
    }

    public void rotateY(double angle){
        double rX = Math.cos(angle) * x + Math.sin(angle) * z;
        double rZ = -Math.sin(angle) * x + Math.cos(angle) * z;

        x = rX;
        z = rZ;
    }

    public void rotateZ(double angle){
        double rX = Math.cos(angle) * x - Math.sin(angle) * y;
        double rY = Math.sin(angle) * x + Math.cos(angle) * y;

        x = rX;
        y = rY;
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
            case 0 -> {
                x = value;
                return;
            }
            case 1 -> {
                y = value;
                return;
            }
            case 2 -> {
                z = value;
                return;
            }
            default -> {
            }
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

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
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

    @Override
    public String toString() {
        return String.format("x: %f\t y: %f\t z: %f", x, y, z);
    }

    public String toYaml(){
        return String.format(
                "{ x: %f, y: %f, z: %f }",
                x, y, z
        );
    }
}