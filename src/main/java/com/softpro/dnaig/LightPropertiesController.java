package com.softpro.dnaig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LightPropertiesController implements Initializable {


    @FXML
    private ChoiceBox<String> choiceBoxLightProperties;

    @FXML
    void cancelPropertiesWindow(ActionEvent event) {

        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void submitProperties(ActionEvent event) {
        System.out.println("subit settings ");
    }

    String[] choice = {"Licht A", "Licht B", "Licht C"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxLightProperties.getItems().setAll(choice);
    }
}
