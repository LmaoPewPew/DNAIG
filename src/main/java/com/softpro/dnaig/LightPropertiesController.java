package com.softpro.dnaig;

import com.softpro.dnaig.utils.Vector3D;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class LightPropertiesController implements Initializable {


    @FXML
    private TextField brightnessTF;
    @FXML
    private TextField xPosTF, yPosTF, zPosTF;

    @FXML
    private ChoiceBox<String> choiceBoxLightProperties;

    @FXML
    void cancelPropertiesWindow(ActionEvent event) {

        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void submitProperties(ActionEvent event) {
        System.out.println("submit settings ");
    }

    String[] choice = {"Licht A", "Licht B", "Licht C"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxLightProperties.getItems().setAll(choice);
    }

    public Vector3D[] getValues() {

        double[] pos = new double[]{0,0,0};
        double[] brightness = new double[]{0,0,0};
        try{
            pos[0] = Double.parseDouble(xPosTF.getText());
            pos[1] = Double.parseDouble(yPosTF.getText());
            pos[2] = Double.parseDouble(zPosTF.getText());
        } catch (Exception ignored) {
            System.out.println("Fehler");
        }

        String[] brightnessString = brightnessTF.getText().split(",");
        try{
            brightness[0] = Double.parseDouble(brightnessString[0]);
            brightness[1] = Double.parseDouble(brightnessString[1]);
            brightness[2] = Double.parseDouble(brightnessString[2]);
        } catch (Exception ignored) {
            System.out.println("Fehler");
        }

        return new Vector3D[]{
                new Vector3D(pos[0], pos[1], pos[2]),
                new Vector3D(brightness[0], brightness[1], brightness[2])
        };
    }
}
