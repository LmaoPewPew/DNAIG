package com.softpro.dnaig.properties;

import com.softpro.dnaig.ApplicationController;
import com.softpro.dnaig.utils.Config;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.function.Consumer;

public class LightProperties {

    private int brightness;

    private final ApplicationController ac;

    private Config.lightvariants lightvariants;
    private String name;
    private String id;
    private String[] pos;
    private String[] rot;
    private ImageView imageView;
    private final Consumer<Integer> previewCallbackWhenSelected;
    private Button button;

    public LightProperties(Config.lightvariants lightvariants, int brightness, ApplicationController ac, String objName, String objID, String[] pos, String[] objRot, ImageView imageView, Consumer<Integer> previewCallbackWhenSelected, Button button) {
        this.lightvariants = lightvariants;
        this.brightness = brightness;
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
            ac.updateObjectPropertiesMenuOnLoad(this.getAll());
            previewCallbackWhenSelected.accept(Integer.parseInt(getId()));
        });
    }

    public String[] getAll() {
        String[] ret = {this.id, this.name, String.valueOf(this.lightvariants), this.pos[0],this.pos[1],this.pos[2], this.rot[0], this.rot[1], this.rot[2]};
        return ret; //Return objID, objName, lightvariant, pos x, pos y, pos z, rot x, rot y, rot z
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

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public Config.lightvariants getLightvariants() {
        return lightvariants;
    }

    public void setLightvariants(Config.lightvariants lightvariants) {
        this.lightvariants = lightvariants;
    }
}
