package com.softpro.dnaig.utils;

import com.softpro.dnaig.rayTracer.Util;

import java.util.ArrayList;

public class Vector3D {

    /**
     * x, y, z coordinates of the vector
     */
    private double x, y, z;


    /**
     * Creates a new Vector3D with the given coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a null Vector3D
     */
    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Adds the given Vector3D to this Vector3D
     * @param v Vector3D to add
     */
    public void add(Vector3D v) {
        x += v.getX();
        y += v.getY();
        z += v.getZ();
    }

    /**
     * Rotates the Vector3D around the given angles
     *
     * @param angleX Rotation around the x-axis
     * @param angleY Rotation around the y-axis
     * @param angleZ Rotation around the z-axis
     */
    public void rotate(double angleX, double angleY, double angleZ){
        double cos = round(Math.cos((angleX/360)*2*Math.PI), 7);
        double sin = round(Math.sin((angleX/360)*2*Math.PI), 7);
        double[][] rotationX = {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        };

        cos = round(Math.cos((angleY/360)*2*Math.PI), 7);
        sin = round(Math.sin((angleY/360)*2*Math.PI), 7);
        double[][] rotationY = {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        };

        cos = round(Math.cos((angleZ/360)*2*Math.PI), 7);
        sin = round(Math.sin((angleZ/360)*2*Math.PI), 7);
        double[][] rotationZ = {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        double[][] rotationMatrix = multiplyMatrices(rotationX, rotationY, rotationZ);
        double[] result = multiplyMatrixWithVector(rotationMatrix, new double[]{x, y, z});

        x = result[0];
        y = result[1];
        z = result[2];
    }

    /**
     * Multiplies the given matrix with the given vector
     *
     * @param rotationMatrix Matrix to multiply with
     * @param doubles Vector to multiply with
     * @return Result of the multiplication
     */
    private double[] multiplyMatrixWithVector(double[][] rotationMatrix, double[] doubles) {
        double[] tmp = new double[4];
        for (int i = 0; i<doubles.length; i++) {
            tmp[i] = doubles[i];
        }
        doubles = tmp.clone();
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

    private double round(double num, int n){
        num = num*Math.pow(10, n);
        num = (int)num;
        num = num/Math.pow(10, n);
        return num;
    }

    private double[][] multiplyMatrices(double[][] rotationX, double[][] rotationY, double[][] rotationZ) {
        int rowsA = rotationX.length;
        int colsA = rotationX[0].length;
        int colsB = rotationY[0].length;
        double[][] result1 = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result1[i][j] += rotationX[i][k] * rotationY[k][j];
                }
            }
        }

        int rowsC = rotationZ.length;
        int colsC = rotationZ[0].length;
        int colsD = result1[0].length;
        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsC; i++) {
            for (int j = 0; j < colsD; j++) {
                for (int k = 0; k < colsC; k++) {
                    result[i][j] += rotationZ[i][k] * result1[k][j];
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
                "\n\t\tx: %f\n\t\ty: %f\n\t\tz: %f\n",
                x, y, z
        );
    }

    public String toColorYaml(){
        return String.format(
                "\n\t\tr: %f\n\t\tg: %f\n\t\tb: %f\n",
                x, y, z
        );
    }
}