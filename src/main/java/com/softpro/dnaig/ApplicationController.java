package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.object.ObjectProperties;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.utils.ObjFileReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.SplitPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    //Buttons
    @FXML
    private Button btnLight;
    @FXML
    private Button btnObjImp;
    @FXML
    private Button btnRender;

    //Objects
    @FXML
    private ListView<ImageView> objectListView;

    private List<ObjectProperties> propertiesList = new ArrayList<>();

    //Rest
    @FXML
    private TextField coordTextField;
    @FXML
    private StackPane previewPane;

    private final FileChooser fileChooser = new FileChooser();
    private PreviewWindow previewWindow;

    //private final ObjFileReader objImporter = new ObjFileReader();
    private List<Entity> entityList = new ArrayList<>();
    //vlt Flowpane?

    @FXML
    void importLightObject(MouseEvent event) {
        System.out.println("Testst");
        //showObjects(1);
    }

    @FXML
    void importObject(MouseEvent event) {
        File loadObjectFilePath = fileChooser.showOpenDialog(null);
        System.out.println(loadObjectFilePath);

        try {
            Entity entity = ObjFileReader.createObject(loadObjectFilePath.getPath());
            System.out.println(entity);
            entityList.add(entity);
            coordTextField.setText(entity.toString());

            showObjects(entity, 0);
            previewWindow.addObject(loadObjectFilePath.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showObjects(Entity e, int i) {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        if (i == 0) {
            fillObjectProperties(e, i);
            objectListView.getItems().add(propertiesList.get(0).getImageView()); //Nix gehen
        } else if (i == 1) {
            objectListView.getItems().add(new ImageView("D:\\Code\\java\\school\\sofo\\src\\main\\resources\\com\\softpro\\dnaig\\sprites\\light_Img_LIGHTTHEME.png"));
            fillObjectProperties(i);
        } else System.out.println("n/a");


        //TODO:: Set Fix Image View SIZE!!!!

    }

    void fillObjectProperties(Entity e, int id) {
        ObjectProperties op = new ObjectProperties(e.getObjName(), Integer.toString(e.getID()), Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()), new String[]{Float.toString(e.getPivot().getX()), Float.toString(e.getPivot().getY()), Float.toString(e.getPivot().getZ())}, new String[]{Float.toString(e.getOrient().getX()), Float.toString(e.getOrient().getY()), Float.toString(e.getOrient().getZ())});

        op.setImageView(new ImageView("D:\\Code\\java\\school\\sofo\\src\\main\\resources\\com\\softpro\\dnaig\\sprites\\obj_Img_LIGHTTHEME.png"));
        propertiesList.add(op);
    }

    void fillObjectProperties(int id) {


    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        System.out.println("Open RayTracer ");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        previewWindow = new PreviewWindow(previewPane);
    }

    //TODO: initializer, get last used filepath
    public void initialize() {

    }

    public void handleKey(KeyEvent event) {
        if (previewWindow != null)
            previewWindow.handleKey(event);
    }

    //initializer, get last used filepath

}
