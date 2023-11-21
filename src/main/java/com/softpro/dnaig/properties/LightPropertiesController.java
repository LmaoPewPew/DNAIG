package com.softpro.dnaig.properties;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LightPropertiesController implements Initializable{

    @FXML
    private ChoiceBox<String> choiceBoxLightProperties;
    String[] choice = {"Licht A","Licht B", "Licht C"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxLightProperties.getItems().setAll(choice);
    }
}
