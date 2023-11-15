package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.object.ObjectProperties;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.utils.ObjFileReader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    //IMG
    String objectIMG = "https://i.imgur.com/4JzMipP.png";
    String lightbulbIMG = "https://i.imgur.com/nbKsECu.png";

    //Objects
    @FXML
    private ListView<ImageView> objectListView;
    private List<ObjectProperties> propertiesList = new ArrayList<>();

    //FileChooser
    private final FileChooser fileChooser = new FileChooser();
    File latestFile = null;

    //Rest
    @FXML
    private TextField coordTextField;
    @FXML
    private StackPane previewPane;
    private PreviewWindow previewWindow;

    //private final ObjFileReader objImporter = new ObjFileReader();
    private List<Entity> entityList = new ArrayList<>();



    /************METHODS************/

    @FXML
    void importLightObject(MouseEvent event) {
        System.out.println("Testst");
        //showObjects(1);
    }

    @FXML
    void importObject(MouseEvent event) {
        File entityFile = fileChooser();
        try {
            Entity entity = ObjFileReader.createObject(entityFile.getPath());
            System.out.println(entity);
            entityList.add(entity);
            coordTextField.setText(entity.toString());

            showObjects(entity, 0);
            previewWindow.addObject(entityFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File fileChooser() {
        fileChooser.setTitle("Open .obj File");
        String fileString = "";

        latestFile = fileChooser.showOpenDialog(new Stage());
        fileString = String.valueOf(latestFile.getParentFile());
        fileChooser.setInitialDirectory(new File(fileString));

        return latestFile;
    }

    void showObjects(Entity e, int i) {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        if (i == 0) {
            fillObjectProperties(e, i);
            objectListView.getItems().add(propertiesList.get(0).getImageView());
        } else if (i == 1) {
            objectListView.getItems().add(new ImageView(lightbulbIMG));

        } else System.out.println("n/a");
    }

    void fillObjectProperties(Entity e, int id) {
        ObjectProperties op = new ObjectProperties(e.getObjName(), Integer.toString(e.getID()),
                Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()),
                new String[]{Float.toString(e.getPivot().getX()), Float.toString(e.getPivot().getY()), Float.toString(e.getPivot().getZ())},
                new String[]{Float.toString(e.getOrient().getX()), Float.toString(e.getOrient().getY()), Float.toString(e.getOrient().getZ())});
        //OBJECTIMG
        Image image = new Image(objectIMG);
        op.setImageView(new ImageView(image));

        propertiesList.add(op);
    }

    void fillObjectProperties(int id) {


    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        //Button für RayTracer!
        System.out.println("loading External Window for RayTracer...");
    }

    public void initialize() {
        previewWindow = new PreviewWindow(previewPane);
    }

    public void handleKey(KeyEvent event) {
        if (previewWindow != null)
            previewWindow.handleKey(event);
    }
}
