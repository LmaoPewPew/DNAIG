package com.dnaig.dnaig.objData;

import com.dnaig.dnaig.utils.Vector3D;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Entity {
    private static int entityID = 0;    // global id property

    private final ArrayList<Face> faces;
    private String objName;
    private Vector3D pivot;
    private boolean smoothShading;
    private final int vertexCount;
    private final int id;   // unique identifier

    // default constructor
    public Entity(){
        this.id = entityID++;
        this.faces = new ArrayList<>(0);
        this.pivot = new Vector3D();
        this.vertexCount = 0;
        this.objName = String.format("object%d", id);
        this.smoothShading = false;
    }

    public Entity(String objName, ArrayList<Face> faces, int vertexCount, boolean smoothShading){
        this.id = entityID++;
        this.faces = faces;
        this.pivot = new Vector3D();
        this.vertexCount = vertexCount;
        this.objName = objName.equals("") ? String.format("object%d", id) : objName;
        this.smoothShading = smoothShading;
    }

    public int getID(){
        return id;
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

    public boolean getSmoothShading(){
        return this.smoothShading;
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
                        Position: %s
                        Smooth Shading enabled: %s""",
                objName, id, faces.size(), vertexCount, pivot, smoothShading
        );
    }
}
