package com.softpro.dnaig;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ApplicationController {

    @FXML
    private TextArea CoordTextField;
    @FXML
    private Button btnLight;
    @FXML
    private Button btnObjImp;
    @FXML
    private Button btnRender;

    FileChooser fileChooser = new FileChooser();

    @FXML
    void importLightObject(MouseEvent event) {
        System.out.println("Testst");
    }

    @FXML
    void importObject(MouseEvent event) {
        File loadObjectFilePath = fileChooser.showOpenDialog(new Stage());
        System.out.println(loadObjectFilePath);
    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        System.out.println("Open RayTracer xoxo UwU");

    }

}
