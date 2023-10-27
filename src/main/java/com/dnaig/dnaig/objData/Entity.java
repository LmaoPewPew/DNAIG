package com.dnaig.dnaig.objData;

import com.dnaig.dnaig.utils.Vector3D;

import java.util.ArrayList;

public class Entity {
    private static int entityID = 0;    // global id property

    private final ArrayList<Material> materials;
    private final ArrayList<Group> groups;
    private String objName;
    private Vector3D pivot;
    private Vector3D orient;    // entity orientation
    private boolean smoothShading;
    private final int vertexCount;
    private final int edgeCount;
    private final int faceCount;
    private final int id;   // unique identifier

    // default constructor
    public Entity(){
        this.id = entityID++;

        this.groups = new ArrayList<>(0);
        this.materials = new ArrayList<>(0);
        this.pivot = new Vector3D();

        this.vertexCount = 0;
        this.edgeCount = 0;
        this.faceCount = 0;

        this.objName = String.format("object%d", id);
        this.smoothShading = false;
        this.orient = new Vector3D(1,0,0);
    }

    public Entity(String objName, ArrayList<Group> groups, ArrayList<Material> materials, int vertexCount, int edgeCount, boolean smoothShading){
        this.id = entityID++;

        this.groups = groups;
        this.materials = materials == null ? new ArrayList<>(0) : materials;
        this.pivot = new Vector3D();

        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;

        int temp = 0;
        for (Group g: groups) {
            temp += g.faceCount();
        }
        this.faceCount = temp;

        this.objName = objName.equals("") ? String.format("object%d", id) : objName;
        this.smoothShading = smoothShading;

        this.orient = new Vector3D(1,0,0);
    }

    // function to scale entity
    public void scale(float factor){

    }

    // function to rotate entity
    public void rotate(double angle){

    }


    public int getID(){
        return id;
    }
    public Vector3D getOrient(){
        return orient;
    }
    public Vector3D getPivot(){
        return this.pivot;
    }
    public String getObjName(){
        return this.objName;
    }
    public int getVertexCount(){
        return this.vertexCount;
    }
    public int getEdgeCount(){ return this.edgeCount; }
    public boolean getSmoothShading(){
        return this.smoothShading;
    }

    public void setOrient(Vector3D orient){
        this.orient = orient;
    }
    public void setPivot(Vector3D pivot){
        this.pivot = pivot;
    }
    public void setObjName(String objName){
        this.objName = objName;
    }
    public void setSmoothShading(boolean smoothShading){
        this.smoothShading = smoothShading;
    }

    @Override
    public String toString(){
        return String.format(
                """
                        Object name: %s
                        ID: %d
                        Faces: %d
                        Vertices: %d
                        Edges: %d
                        Position: %s
                        Smooth Shading enabled: %s""",
                objName, id, faceCount, vertexCount, edgeCount, pivot, smoothShading
        );
    }
}
