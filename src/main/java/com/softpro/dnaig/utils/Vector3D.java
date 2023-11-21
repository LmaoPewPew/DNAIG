package com.softpro.dnaig.utils;

import com.softpro.dnaig.simpleRayTracer.Util;

/**
 * Represents a 3D vector with components for x, y, and z coordinates.
 */
public class Vector3D {
    private float x, y, z;

    /**
     * Initializes a 3D vector with default values (0, 0, 0).
     */
    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Initializes a 3D vector with specified x, y, and z coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds the current vector to another vector and returns a new vector as the result.
     *
     * @param v The vector to be added to the current vector.
     * @return A new vector representing the addition result.
     */
    public Vector3D add(Vector3D v) {
        return new Vector3D(
                this.x + v.getX(),
                this.y + v.getY(),
                this.z + v.getZ()
        );
    }

    /**
     * Subtracts another vector from the current vector and returns a new vector as the result.
     *
     * @param v The vector to be subtracted from the current vector.
     * @return A new vector representing the subtraction result.
     */
    public Vector3D subtract(Vector3D v) {
        return new Vector3D(
                this.x - v.getX(),
                this.y - v.getY(),
                this.z - v.getZ()
        );
    }

    /**
     * Normalizes the vector and returns a new vector as the result.
     *
     * @return A new vector representing the normalized vector.
     */
    public Vector3D normalize() {
        float len = length();
        if (len != 0) {
            return new Vector3D(
                    this.x / len,
                    this.y / len,
                    this.z / len
            );
        }

        // Return a new vector (0, 0, 0) to avoid division by zero if the length is 0.
        return new Vector3D();
    }

    /**
     * Multiplies the current vector by another vector and returns a new vector as the result.
     *
     * @param v The vector to multiply by the current vector.
     * @return A new vector representing the multiplication result.
     */
    public Vector3D multiply(Vector3D v) {
        return new Vector3D(
                this.x * v.getX(),
                this.y * v.getY(),
                this.z * v.getZ()
        );
    }

    public float product(Vector3D v){
        return this.x * v.getX() + this.y * v.getY() + this.z * v.getZ();
    }

    /**
     * Calculates the cross product of two vectors and returns a new vector as the result.
     *
     * @param v The other vector for the cross product calculation.
     * @return A new vector representing the cross product result.
     */
    public Vector3D crossProduct(Vector3D v) {
        return new Vector3D(
                this.y * v.getZ() - v.getY() * this.z,
                this.z * v.getX() - v.getZ() * this.x,
                this.x * v.getY() - v.getX() * this.y
        );
    }

    /**
     * Calculates the dot product of two vectors and returns a scalar value as the result.
     *
     * @param v The other vector for the dot product calculation.
     * @return The dot product value.
     */
    public float scalarProduct(Vector3D v) {
        float tmp = this.x * v.getX() + this.y * v.getY() + this.z * v.getZ();

        if(tmp == 0)
            return 0;

        return (float) Math.sqrt(tmp);
    }

    /**
     * Multiplies the current vector by a scalar value and returns a new vector as the result.
     *
     * @param f The scalar value to multiply by the current vector.
     * @return A new vector representing the scalar multiplication result.
     */
    public Vector3D scalarMultiplication(float f) {
        return new Vector3D(
                this.x * f,
                this.y * f,
                this.z * f
        );
    }

    /*
    public Vector3D move(float epsilon, Vector3D positionToLight) {
        return Util.add(this, positionToLight.scalarMultiplication(epsilon));
    }

     */

    /**
     * Rotating the vector on the x-axis, using standard matrix multiplication.
     *
     * @param angle Angle as radiant
     * @return Returns a new Vector3D object.
     */
    public Vector3D rotateX(double angle){
        double cosinus = Math.cos(angle);
        double sinus = Math.sin(angle);

        float newY = (float)(this.y * cosinus - this.z * sinus);
        float newZ = (float)(this.y * sinus + this.z * cosinus);

        return new Vector3D(this.x, newY, newZ);
    }

    /**
     * Rotating the vector on the y-axis, using standard matrix multiplication.
     *
     * @param angle Angle as radiant.
     * @return Returns a new Vector3D object.
     */
    public Vector3D rotateY(double angle){
        double cosinus = Math.cos(angle);
        double sinus = Math.sin(angle);

        float newX = (float)(this.x * cosinus + this.z * sinus);
        float newZ = (float)(-this.x * sinus + this.z * cosinus);

        return new Vector3D(newX, this.y, newZ);
    }

    /**
     * Rotating the vector on the y-axis, using standard matrix multiplication.
     *
     * @param angle Angle as radiant.
     * @return Returns a new Vector3D object.
     */
    public Vector3D rotateZ(double angle){
        double cosinus = Math.cos(angle);
        double sinus = Math.sin(angle);

        float newX = (float)(this.x * cosinus - this.y * sinus);
        float newY = (float)(this.x * sinus + this.y * cosinus);

        return new Vector3D(newX, newY, this.z);
    }

    /**
     * Sets the value of the x, y and z coordinates depending on their index.
     * x: index = 0
     * y: index = 1
     * z: index = 2
     *
     * @param idx
     * @param value
     */
    public void setValue(int idx, float value){
        switch (idx) {
            case 0 -> x = value;
            case 1 -> y = value;
            case 2 -> z = value;
        }
    }

    /**
     * Returns the value of the x, y and z coordinates depending on their index.
     * If the index is out of bounds, the maximum value of the float-type is returned.
     * x: index = 0
     * y: index = 1
     * z: index = 2
     *
     * @param idx Index for the x, y and z values.
     * @return The value of the corresponding index.
     */
    public float getValue(int idx){
        switch (idx) {
            case 0 -> {
                return x;
            }
            case 1 -> {
                return y;
            }
            case 2 -> {
                return z;
            }
            default -> {
                return Float.MAX_VALUE;
            }
        }
    }

    /**
     * Calculates the length (magnitude) of the vector.
     *
     * @return The length of the vector.
     */
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Gets the x-coordinate of the vector.
     *
     * @return The x-coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the vector.
     *
     * @return The y-coordinate.
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the z-coordinate of the vector.
     *
     * @return The z-coordinate.
     */
    public float getZ() {
        return z;
    }

    /**
     * Sets the x-coordinate of the vector to a specified value.
     *
     * @param x The new x-coordinate.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the vector to a specified value.
     *
     * @param y The new y-coordinate.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the z-coordinate of the vector to a specified value.
     *
     * @param z The new z-coordinate.
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Provides a string representation of the vector, including its x, y, and z coordinates.
     *
     * @return A formatted string representation of the vector.
     */
    @Override
    public String toString() {
        return String.format("x: %f\t y: %f\t z: %f", x, y, z);
    }
}
