package com.softpro.dnaig.properties;

import com.softpro.dnaig.ApplicationController;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.function.Consumer;

public class ObjectProperties {
    //Object name: object0
    // ID: 0
    // Faces: 8207
    // Vertices: 11312
    // Position: x: 0,000000 y: 0,000000 z: 0,000000
    private final ApplicationController ac;
    private String objName;
    private String objID;
    private String objFaces;
    private String objVertices;
    private String[] objPos;
    private String[] objRot;
    private ImageView imageView;
    private final Consumer<Integer> previewCallbackWhenSelected;

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


    public void setImageView(ImageView imageViews) {
        this.imageView = imageViews;
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);

        this.button.setGraphic(this.imageView);
        //this.button.setStyle("-fx-background-color: transparent;");
        this.button.setOnAction(e -> {
            ac.setLastClickedID(getObjID());
            ac.updateObjectPropertiesMenu(this.getAll());
            previewCallbackWhenSelected.accept(Integer.parseInt(objID));

        });
    }

    public ObjectProperties(String objID, String objName, String objFaces, String objVertices, String[] objPos, String[] objRot, Consumer<Integer> previewCallbackWhenSelected, ApplicationController ac) {
        this.objID = objID;
        this.objName = objName;
        this.objFaces = objFaces;
        this.objVertices = objVertices;
        this.objPos = objPos;
        this.objRot = objRot;
        this.previewCallbackWhenSelected = previewCallbackWhenSelected;
        this.button = new Button();
        this.ac = ac;
    }


    public String[] getAll() {
        String[] ret = {this.objID, this.objName, this.objFaces, this.objVertices, this.objPos[0],this.objPos[1],this.objPos[2], this.objRot[0], this.objRot[1], this.objRot[2]};
        return ret; //Return objID, objName, objFaces, objVertices, pos x, pos y, pos z, rot x, rot y, rot z
    }

/*
    //set Value of more Important Values
    public TextField[] getTextFieldValues(TextField[] textFieldArray) {
        String[] valuesArr = {objName, objPos[0], objPos[1], objPos[2], objRot[0], objRot[1], objRot[2]};

        System.out.println("\n\n\n\n");
        for (int i = 0; i < textFieldArray.length && i < valuesArr.length; i++) {
            // textFieldArray[i].setText(valuesArr[i]);
            System.out.println(valuesArr[i]);
        }
        return textFieldArray;

 */

/*
        nameTXT.setText(e.getObjName());
        setFaces.setText(String.valueOf(e.getFaces().size()));
        setVertices.setText(String.valueOf(e.getVertexCount()));

        xPosTXT.setText(String.valueOf(e.getPivot().getX()));
        yPosTXT.setText(String.valueOf(e.getPivot().getY()));
        zPosTXT.setText(String.valueOf(e.getPivot().getZ()));

        xRotTXT.setText(String.valueOf(e.getOrient().getX()));
        yRotTXT.setText(String.valueOf(e.getOrient().getY()));
        zRotTXT.setText(String.valueOf(e.getOrient().getZ()));
 */

/*
    public Text[] getTextValues() {
        Text[] textArray = new Text[2];

        textArray[0].setText("objFaces");
        textArray[1].setText("objVertices");

        return textArray;
    }

 */

    public void setImage(Image image) {
        this.imageView = new ImageView(image);
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }
}
