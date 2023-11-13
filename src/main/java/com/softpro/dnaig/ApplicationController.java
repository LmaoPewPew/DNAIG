package com.softpro.dnaig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ApplicationController {

    @FXML
    private Button btnLight;

    @FXML
    private Button btnObjImp;

    @FXML
    private Button btnRender;

    @FXML
    void importLightObject(ActionEvent event) {
        System.out.println("Test");
    }

    @FXML
    void importObject(ActionEvent event) {

    }

    @FXML
    void loadRayTracer(ActionEvent event) {

    }

}
