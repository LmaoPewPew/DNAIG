package com.softpro.dnaig.objData;

import com.softpro.dnaig.utils.Vector3D;

import java.util.ArrayList;

/**
 * Represents an Entity in 3D space, with properties such as its name, position, orientation,
 * and a collection of faces that make up the Entity.
 */
public class Entity {
    // Static unique identifier for each object.
    private static int entityID = 0;

    // Face objects that make up the Entity.
    private final ArrayList<Face> faces;
    // Object name.
    private String objName;
    // 3D Vector which contains the position of the Entity in the application world space.
    private Vector3D pivot;
    // 3D Vector which contains the orientation of the Entity.
    private Vector3D orient;
    // Number of vertices
    private final int vertexCount;
    // Unique identifier.
    private final int id;

    /**
     * Creates a new Entity with default values.
     */
    public Entity() {
        this.id = entityID++;

        this.faces = new ArrayList<>(0);
        this.pivot = new Vector3D();

        this.vertexCount = 0;

        this.objName = String.format("object%d", id);
        this.orient = new Vector3D(1, 0, 0);
    }

    /**
     * Creates a new Entity with specified properties.
     *
     * @param objName     The name of the Entity.
     * @param faces       A list of Face objects that make up the Entity.
     * @param vertexCount The number of vertices in the Entity
     */
    public Entity(String objName, ArrayList<Face> faces, int vertexCount) {
        this.id = entityID++;

        this.faces = faces;
        this.pivot = new Vector3D();

        this.vertexCount = vertexCount;

        this.objName = objName.isEmpty() ? String.format("object%d", id) : objName;
        this.orient = new Vector3D(1, 0, 0);
    }

    /**
     * Scales the Entity by a specified factor.
     *
     * @param factor The scaling factor.
     */
    public void scale(float factor) {

    }

    /**
     * Rotates the Entity by a specified angle.
     *
     * @param angle The rotation angle in radians.
     */
    public void rotate(double angle) {

    }

    /**
     * Gets the unique identifier of the Entity.
     *
     * @return The unique identifier (ID) of the Entity.
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the orientation of the Entity as a Vector3D
     *
     * @return The orientation of the Entity.
     */
    public Vector3D getOrient() {
        return orient;
    }

    /**
     * Gets the pivot (position) of the Entity as a Vector3D.
     *
     * @return The pivot (position) of the Entity.
     */
    public Vector3D getPivot() {
        return this.pivot;
    }

    /**
     * Gets the name of the Entity.
     *
     * @return The name of the Entity.
     */
    public String getObjName() {
        return this.objName;
    }

    /**
     * Gets the number of vertices in the Entity.
     *
     * @return The number of vertices in the Entity.
     */
    public int getVertexCount() {
        return this.vertexCount;
    }

    /**
     * Sets the orientation of the Entity to a specified Vector3D.
     *
     * @param orient The new orientation for the Entity.
     */
    public void setOrient(Vector3D orient) {
        this.orient = orient;
    }

    /**
     * Sets the pivot (position) of the Entity to a specified Vector3D.
     *
     * @param pivot The new pivot (position) for the Entity.
     */
    public void setPivot(Vector3D pivot) {
        this.pivot = pivot;
    }

    /**
     * Sets the name of the Entity to a specified string.
     *
     * @param objName The new name for the Entity.
     */
    public void setObjName(String objName) {
        this.objName = objName;
    }

    /**
     * Provides a formatted string representation of the Entity, including its name, ID, face count,
     * vertex count, and position.
     *
     * @return A formatted string representation of the Entity.
     */
    @Override
    public String toString() {
        return String.format("""
                Object name: %s
                ID: %d
                Faces: %d
                Vertices: %d
                Position: %s
                """, objName, id, faces.size(), vertexCount, pivot);
    }
}
