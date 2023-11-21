package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.object.ObjectProperties;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.utils.ObjFileReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ApplicationController {
    //IMG
    String objectIMG = "https://i.imgur.com/4JzMipP.png";
    String lightOBJIMG = "https://i.imgur.com/nbKsECu.png";

    //Objects
    @FXML
    private ChoiceBox<String> choiceBoxLightProperties;

    @FXML
    private ListView<Button> objectListView;
    private LinkedList<ObjectProperties> propertiesList = new LinkedList<>();
    private String lastClickedID = "0";
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
    void importLightObject(MouseEvent event) throws IOException{
        String values = open_propertieWindows();
        createGUIObject(null);
    }

    private String open_propertieWindows() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("lightproperties.fxml"));

        Scene scene =new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("lightproperties");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();


        return "0";
    }

    @FXML
    void importObject(MouseEvent event) {
        File entityFile = fileChooser();
        try {
            Entity entity = ObjFileReader.createObject(entityFile.getPath());
            System.out.println(entity);
            entityList.add(entity);

            createGUIObject(entity);
            previewWindow.addObject(entityFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLastClickedID(String s) {
        this.lastClickedID = s;
    }

    void createGUIObject(Entity e) {
        loadObjectProperties(e);
        setObjectListView();
    }

    private void loadObjectProperties(Entity e) {
        ObjectProperties op = null;
        int id;

        if (e == null) {     //load light properties
            id = 1;
            int objID;
            if (!propertiesList.isEmpty()) {
                objID = Integer.parseInt(propertiesList.getLast().getObjID()) + 1;
            } else {
                objID = 0;
            }
            op = new ObjectProperties(String.valueOf(objID), "light", "0", "0", new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, null, this);

        } else {          //load object properties
            id = 0;
            int objID;
            if (!propertiesList.isEmpty()) {
                objID = Integer.parseInt(propertiesList.getLast().getObjID()) + 1;
            } else {
                objID = 0;
            }

            op = new ObjectProperties(String.valueOf(objID), e.getObjName(), Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()), new String[]{Float.toString(e.getPivot().getX()), Float.toString(e.getPivot().getY()), Float.toString(e.getPivot().getZ())}, new String[]{Float.toString(e.getOrient().getX()), Float.toString(e.getOrient().getY()), Float.toString(e.getOrient().getZ())}, previewWindow::updateSelected, this);
        }
        loadImage(op, id);
    }

    private void setObjectListView() {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.get(propertiesList.size() - 1).getButton());
    }

    private void loadImage(ObjectProperties op, int id) {
        Image image;
        if (id == 0) {      //object
            image = new Image(objectIMG);
        } else {       //light
            image = new Image(lightOBJIMG);
        }
        op.setImageView(new ImageView(image), coordTextField);
        propertiesList.add(op);
    }

    void deleteObject() {
        objectListView.getItems().remove(Integer.parseInt(lastClickedID));
    }


    File fileChooser() {
        fileChooser.setTitle("Open .obj File");
        String fileString;

        latestFile = fileChooser.showOpenDialog(new Stage());
        fileString = String.valueOf(latestFile.getParentFile());
        fileChooser.setInitialDirectory(new File(fileString));

        return latestFile;
    }


    @FXML
    void loadRayTracer(MouseEvent event) {
        //Button für RayTracer!
        System.out.println("loading External Window for RayTracer...");
    }

    public void initialize() {
        previewWindow = new PreviewWindow(previewPane);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wavefront OBJ Files", "*.obj"));
    }

    public void handleKey(KeyEvent event) {
        if (previewWindow != null) previewWindow.handleKey(event);
    }
}
