package com.dnaig.dnaig.utils;

public class Vector3D {
    private float x, y, z;

    // default constructor initializes the vector to (0, 0, 0)
    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    // constructor sets the vector to the parameters
    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // add a vector to current vector and return a new vector
    public Vector3D add(Vector3D v) {
        return new Vector3D(
                this.x = v.getX(),
                this.y = v.getY(),
                this.z = v.getZ()
        );
    }

    // subtract a vector to current vector and return a new vector
    public Vector3D subtract(Vector3D v) {
        return new Vector3D(
                this.x - v.getX(),
                this.y - v.getY(),
                this.z - v.getZ()
        );
    }

    // normalize the vector and return a new vector
    public Vector3D normalize() {
        float len = length();
        if (len != 0) {
            return new Vector3D(
                    this.x / len,
                    this.y / len,
                    this.z / len
            );
        }

        // if the length is 0, return new vector (0, 0, 0) to avoid division by zero
        return new Vector3D();
    }

    // multiply the vector by another vector and return a new vector
    public Vector3D mul(Vector3D v) {
        return new Vector3D(
                this.x * v.getX(),
                this.y * v.getY(),
                this.z * v.getZ()
        );
    }

    // calculates the cross product of two vector and return a new vector
    public Vector3D crossProduct(Vector3D v) {
        return new Vector3D(
                this.y * v.getZ() - v.getY() * this.z,
                this.z * v.getX() - v.getZ() * this.x,
                this.x * v.getY() - v.getX() * this.y
        );
    }

    // calculate the dot product of two vectors and return a scalar value
    public float skalarProduct(Vector3D v) {
        return (float) Math.sqrt(
                this.x * v.getX() + this.y * v.getY() + this.z * v.getZ()
        );
    }

    // multiply the vector by a scalar value and return a new vector
    public Vector3D skalarMultiplication(float f) {
        return new Vector3D(
                this.x * f,
                this.y * f,
                this.z * f
        );
    }

    // calculate the length of the vector
    public float length() {
        return (float) Math.sqrt(
                this.x * this.x * this.y * this.y + this.z * this.z
        );
    }

    // getter methods to access encapsulated properties
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    // setter methods to modify encapsulated properties
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    // string representation of the vector
    @Override
    public String toString() {
        return String.format("x: %f\t y: %f\t z: %f",
                x, y, z
        );
    }
}
