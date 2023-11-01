package com.softpro.dnaig.objData;

/**
 * Represents a Face in a 3D model, composed of vertices, a smoothing group, and a material.
 */
public class Face {
    // Array of Vertex objects that make up a Face object.
    private final Vertex[] vertices;
    // Integer that contains the smoothing group mode.
    private int smoothingGroup;
    // Material object for each Face object.
    private Material material;

    /**
     * Constructs a default Face with three default vertices.
     */
    public Face() {
        this.vertices = new Vertex[]{
                new Vertex(),
                new Vertex(),
                new Vertex()
        };
    }

    /**
     * Constructs a Face with specified vertices, a material, and a smoothing group.
     *
     * @param vertices       An array of vertices making up the face.
     * @param material       The material associated with the face.
     * @param smoothingGroup The smoothing group assigned to the face.
     */
    public Face(Vertex[] vertices, Material material, int smoothingGroup) {
        this.vertices = vertices;
        this.smoothingGroup = smoothingGroup;
        this.material = material;
    }

    /**
     * Gets the number of vertices in the face.
     *
     * @return The number of vertices in the face.
     */
    public int getVertexCount() {
        return this.vertices.length;
    }

    /**
     * Gets the smoothing group associated with the face.
     *
     * @return The smoothing group assigned to the face.
     */
    public int getSmoothingGroup() {
        return this.smoothingGroup;
    }

    /**
     * Gets the material associated with the face.
     *
     * @return The material associated with the face.
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Provides a string representation of the Face, including the vertices and their positions.
     *
     * @return A string representation of the Face.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (Vertex vertex : vertices) {
            str.append(
                    String.format(
                            "vert %d\n%s\n", ++i, vertex)
            );
        }

        return str.toString();
    }
}
