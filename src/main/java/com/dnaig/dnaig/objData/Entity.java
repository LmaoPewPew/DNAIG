package com.dnaig.dnaig.objData;

import com.dnaig.dnaig.utils.Vector3D;

import java.util.ArrayList;

public class Entity {
    private static int entityID = 0;    // global id property

    private final ArrayList<Face> faces;
    private String objName;
    private Vector3D pivot;
    private Vector3D orient;    // entity orientation
    private final int vertexCount;
    private final int edgeCount;
    private final int id;   // unique identifier

    // default constructor
    public Entity(){
        this.id = entityID++;

        this.faces = new ArrayList<>(0);
        this.pivot = new Vector3D();

        this.vertexCount = 0;
        this.edgeCount = 0;

        this.objName = String.format("object%d", id);
        this.orient = new Vector3D(1,0,0);
    }

    public Entity(String objName, ArrayList<Face> faces, int vertexCount, int edgeCount){
        this.id = entityID++;

        this.faces = faces;
        this.pivot = new Vector3D();

        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;

        this.objName = objName.isEmpty() ? String.format("object%d", id) : objName;
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

    public void setOrient(Vector3D orient){
        this.orient = orient;
    }
    public void setPivot(Vector3D pivot){
        this.pivot = pivot;
    }
    public void setObjName(String objName){
        this.objName = objName;
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
                        Position: %s""",
                objName, id, faces.size(), vertexCount, edgeCount, pivot
        );
    }
}
