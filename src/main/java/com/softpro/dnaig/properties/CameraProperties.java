package com.softpro.dnaig.properties;

import com.softpro.dnaig.ApplicationController;
import com.softpro.dnaig.utils.Config;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.function.Consumer;

public class CameraProperties implements Properties{
    Config.type categoryType;
    private final ApplicationController ac;
    private Config.cameravariants cameravariants;
    private String name;
    private String id;
    private String[] pos;
    private String[] rot;
    private ImageView imageView;
    private final Consumer<Integer> previewCallbackWhenSelected;
    private Button button;

    public CameraProperties(Config.type categoryType, Config.cameravariants cameravariants, ApplicationController ac, String objName, String objID, String[] pos, String[] objRot, ImageView imageView, Consumer<Integer> previewCallbackWhenSelected, Button button) {
        this.categoryType = categoryType;
        this.cameravariants = cameravariants;
        this.ac = ac;
        this.name = objName;
        this.id = objID;
        this.pos = pos;
        this.rot = objRot;
        this.imageView = imageView;
        this.previewCallbackWhenSelected = previewCallbackWhenSelected;
        this.button = button;
    }

    public void setImageView(ImageView imageViews) {
        this.imageView = imageViews;
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);

        this.button.setGraphic(this.imageView);
        //this.button.setStyle("-fx-background-color: transparent;");
        this.button.setOnAction(e -> {
            ac.setLastClickedID(getId());
            ac.updateObjectPropertiesMenu(this.getAll());
            previewCallbackWhenSelected.accept(Integer.parseInt(getId()));
        });
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
}
