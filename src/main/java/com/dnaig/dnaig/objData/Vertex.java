package com.dnaig.dnaig.objData;

import com.dnaig.dnaig.utils.Vector3D;

public class Vertex {
    private Vector3D coordinates;   // 3D coordinates of the vertex
    private Vector3D normal;    // normal vector of the vertex
    private Vector3D texture;   // texture coordinates at the vertex

    // default constructor initializes all components to zero vectors
    public Vertex(){
        this.coordinates = new Vector3D();
        this.normal = new Vector3D();
        this.texture = new Vector3D();
    }

    // constructor to set the vertex components
    public Vertex(Vector3D coordinates, Vector3D normal, Vector3D texture) {
        this.coordinates = coordinates;
        this.texture = texture;
        this.normal = normal;
    }

    // getter methods to access properties
    public Vector3D getNormal() {
        return normal;
    }
    public Vector3D getTexture() {
        return texture;
    }
    public Vector3D getCoordinates() {
        return coordinates;
    }

    // setter methods to set properties
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }
    public void setTexture(Vector3D texture) {
        this.texture = texture;
    }
    public void setCoordinates(Vector3D coordinates) {
        this.coordinates = coordinates;
    }

    // string representation of the vertex
    @Override
    public String toString(){
        return String.format(
                "\tcoordinates: %s\n\tnormal: %s\n\ttexture: %s", coordinates, normal, texture);
    }
}
