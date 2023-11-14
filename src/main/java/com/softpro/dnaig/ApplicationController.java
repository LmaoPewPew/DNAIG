package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.utils.ObjFileReader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    @FXML
    private TextField coordTextField;

    @FXML
    private ListView<ImageView> objectListView;

    @FXML
    private Button btnLight;
    @FXML
    private Button btnObjImp;
    @FXML
    private Button btnRender;

    private final FileChooser fileChooser = new FileChooser();

    //private final ObjFileReader objImporter = new ObjFileReader();
    private List<Entity> entityList = new ArrayList<>();
    //vlt Flowpane?

    @FXML
    void importLightObject(MouseEvent event) {
        System.out.println("Testst");
        showObjects(1);
    }

    @FXML
    void importObject(MouseEvent event) {
        File loadObjectFilePath = fileChooser.showOpenDialog(new Stage());
        System.out.println(loadObjectFilePath);

        try {
            Entity entity = ObjFileReader.createObject(loadObjectFilePath.getPath());
            System.out.println(entity);
            entityList.add(entity);
            coordTextField.setText(entity.toString());

            showObjects(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showObjects(int i) {
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        if (i == 0)
            objectListView.getItems().add(new ImageView("D:\\Code\\java\\school\\sofo\\SoftProject\\src\\main\\resources\\com\\softpro\\dnaig\\sprites\\obj_Img_LIGHTTHEME.png"));
        else if (i == 1)
            objectListView.getItems().add(new ImageView("D:\\Code\\java\\school\\sofo\\SoftProject\\src\\main\\resources\\com\\softpro\\dnaig\\sprites\\light_Img_LIGHTTHEME.png"));
        else System.out.println("n/a");
    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        System.out.println("Open RayTracer ");

    }

}
