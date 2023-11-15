package com.softpro.dnaig.object;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

import java.util.Arrays;

public class ObjectProperties {
    //Object name: object0
    // ID: 0
    // Faces: 8207
    // Vertices: 11312
    // Position: x: 0,000000 y: 0,000000 z: 0,000000
    private String objName;
    private String objID;
    private String objFaces;
    private String objVertices;
    private String objPos[];
    private String objRot[];
    private ImageView imageView;

    private Button button;


    public String getObjID() {
        return objID;
    }

    public void setObjID(String objID) {
        this.objID = objID;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjFaces() {
        return objFaces;
    }

    public void setObjFaces(String objFaces) {
        this.objFaces = objFaces;
    }

    public String getObjVertices() {
        return objVertices;
    }

    public void setObjVertices(String objVertices) {
        this.objVertices = objVertices;
    }

    public String[] getObjPos() {
        return objPos;
    }

    public void setObjPos(String[] objPos) {
        this.objPos = objPos;
    }

    public String[] getObjRot() {
        return objRot;
    }

    public void setObjRot(String[] objRot) {
        this.objRot = objRot;
    }

    public ImageView getImageView() {
        return imageView;
    }


    public void setImageView(ImageView imageViews, TextField coordTextField) {
        this.imageView = imageViews;
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);

        this.button.setGraphic(this.imageView);
        //this.button.setStyle("-fx-background-color: transparent;");
        System.out.println("hi 1");
        this.button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {
                coordTextField.setText(getAll());
            }
        });
    }

    public ObjectProperties(String objID, String objName, String objFaces, String objVertices, String[] objPos, String[] objRot) {
        this.objID = objID;
        this.objName = objName;
        this.objFaces = objFaces;
        this.objVertices = objVertices;
        this.objPos = objPos;
        this.objRot = objRot;
        this.button = new Button();
    }



    public String getAll() {
        return "ObjectProperties{" + "objID='" + objID + '\'' + ", objName='" + objName + '\'' + ", objFaces='" + objFaces + '\'' + ", objVertices='" + objVertices + '\'' + ", objPos=" + Arrays.toString(objPos) + ", objRot=" + Arrays.toString(objRot) + '}';
    }
    public void setImage(Image image) {
        this.imageView = new ImageView(image);;
    }

    public void setButton(Button button) {
        this.button = button;
    }
    public Button getButton() {
        return button;
    }
}
