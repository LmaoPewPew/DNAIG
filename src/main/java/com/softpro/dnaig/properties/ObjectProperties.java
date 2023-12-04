package com.softpro.dnaig.properties;

import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.utils.Config;
import javafx.scene.control.Button;

public class ObjectProperties implements Properties {
    //ect name: ect0
    // ID: 0
    // Faces: 8207
    // Vertices: 11312
    // Position: x: 0,000000 y: 0,000000 z: 0,000000
    Config.type categoryType;
    private String name;
    private String id;
    private String scale;
    private String faces;
    private String vertices;
    private String[] pos;
    private String[] rot;
    private Button button;
    private Entity entity;

    public ObjectProperties(Config.type categoryType, Entity entity, String id, String name, String faces, String vertices, String[] pos, String[] rot) {
        this.categoryType = categoryType;
        this.entity = entity;
        this.id = id;
        this.name = name;
        this.faces = faces;
        this.vertices = vertices;
        this.pos = pos;
        this.rot = rot;
        this.button = new Button();
    }

    public String[] getAll() {
        return new String[]{this.id, this.name, this.faces, this.vertices, this.pos[0],this.pos[1],this.pos[2], this.rot[0], this.rot[1], this.rot[2]}; //Return id, Name, Faces, Vertices, pos x, pos y, pos z, rot x, rot y, rot z
    }

    public String getFaces() {
        return faces;
    }

    public void setFaces(String Faces) {
        this.faces = Faces;
    }

    public String getVertices() {
        return vertices;
    }

    public void setVertices(String Vertices) {
        this.vertices = Vertices;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getPos() {
        return pos;
    }

    public void setPos(String[] pos) {
        this.pos = pos;
        System.out.println("x" + pos[0] + "y" + pos[1] + "z" + pos[2]);
    }

    public String[] getRot() {
        return rot;
    }

    public void setRot(String[] rot) {
        this.rot = rot;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }



    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
