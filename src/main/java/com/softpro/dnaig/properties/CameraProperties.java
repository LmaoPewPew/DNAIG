package com.softpro.dnaig.properties;

import com.softpro.dnaig.utils.Config;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class CameraProperties implements Properties{
    Config.type categoryType;
    private Config.cameravariants cameravariants;
    private String name;
    private String id;
    private String[] pos;
    private String[] rot;
    private int width;
    private int length;
    private Button button;


    public CameraProperties(Config.type categoryType, Config.cameravariants cameravariants, String objName, String objID, String[] pos, String[] objRot, int width, int length) {
        this.categoryType = categoryType;
        this.cameravariants = cameravariants;
        this.name = objName;
        this.id = objID;
        this.pos = pos;
        this.rot = objRot;
        this.width = width;
        this.length = length;
        this.button = new Button();
    }

    public String[] getAll() {
        return new String[]{this.id, this.name, String.valueOf(this.cameravariants), this.pos[0],this.pos[1],this.pos[2], this.rot[0], this.rot[1], this.rot[2]}; //Return objID, objName, cameravariant, pos x, pos y, pos z, rot x, rot y, rot z
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    }

    public String[] getRot() {
        return rot;
    }

    @Override
    public String getScale() {
        return null;
    }

    public void setRot(String[] rot) {
        this.rot = rot;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Config.cameravariants getCameravariants() {
        return cameravariants;
    }

    public void setLightvariants(Config.cameravariants cameravariants) {
        this.cameravariants = cameravariants;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
