package com.softpro.dnaig.objData;

import java.util.ArrayList;

public class Group {
    private final String name;
    private ArrayList<Face> faces;

    public Group(String name, ArrayList<Face> faces) {
        this.name = name;
        this.faces = faces;
    }

    public Group(String name){
        this.name = name;
        faces = new ArrayList<>();
    }

    public void addFace(Face face){
        faces.add(face);
    }
    public int faceCount(){
        return faces.size();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

}
