package com.softpro.dnaig.objData;

import com.softpro.dnaig.utils.Vector3D;


/**
 * Represents a vertex in 3D space with associated coordinates, normal, and texture information.
 */
public class Vertex {
    private Vector3D coordinates;
    private Vector3D normal;
    private Vector3D texture;


    /**
     * Initializes a vertex with all components set to zero vectors.
     */
    public Vertex() {
        this.coordinates = new Vector3D();
        this.normal = new Vector3D();
        this.texture = new Vector3D();
    }

    /**
     * Initializes a vertex with the specified coordinates, normal, and texture components.
     *
     * @param coordinates The 3D coordinates of the vertex.
     * @param normal      The normal vector of the vertex.
     * @param texture     The texture coordinates at the vertex.
     */
    public Vertex(Vector3D coordinates, Vector3D normal, Vector3D texture) {
        this.coordinates = coordinates;
        this.texture = texture;
        this.normal = normal;
    }

    /**
     * Gets the normal vector associated with the vertex.
     *
     * @return The normal vector.
     */
    public Vector3D getNormal() {
        return normal;
    }

    /**
     * Gets the texture coordinates associated with the vertex.
     *
     * @return The texture coordinates.
     */
    public Vector3D getTexture() {
        return texture;
    }

    /**
     * Gets the 3D coordinates of the vertex.
     *
     * @return The 3D coordinates.
     */
    public Vector3D getCoordinates() {
        return coordinates;
    }


    /**
     * Sets the normal vector associated with the vertex.
     *
     * @param normal The new normal vector.
     */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    /**
     * Sets the texture coordinates associated with the vertex.
     *
     * @param texture The new texture coordinates.
     */
    public void setTexture(Vector3D texture) {
        this.texture = texture;
    }

    /**
     * Sets the 3D coordinates of the vertex.
     *
     * @param coordinates The new 3D coordinates.
     */
    public void setCoordinates(Vector3D coordinates) {
        this.coordinates = coordinates;
    }


    /**
     * Provides a string representation of the vertex, including its coordinates, normal, and texture information.
     *
     * @return A formatted string representation of the vertex.
     */
    @Override
    public String toString() {
        return String.format("\tcoordinates: %s\n\tnormal: %s\n\ttexture: %s", coordinates, normal, texture);
    }
}
