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
import java.util.LinkedList;
import java.util.List;

public class ApplicationController {
    //IMG
    String objectIMG = "https://i.imgur.com/4JzMipP.png";
    String lightbulbIMG = "https://i.imgur.com/nbKsECu.png";

    //Objects
    @FXML
    private ListView<Button> objectListView;
    private LinkedList<ObjectProperties> propertiesList = new LinkedList<>();

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
        createGUIObject(null);
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

    void createGUIObject(Entity e) {
        loadObjectPorperties(e);
        setObjectListView();
    }

    private void loadObjectPorperties(Entity e) {
        ObjectProperties op = null;
        int id;

        if (e == null) {     //load light properties
            id = 1;
            int objID;
            if(propertiesList.size()!=0){
                objID= Integer.valueOf(propertiesList.getLast().getObjID())+1;
            }else{
                objID=0;
            }
            op = new ObjectProperties(String.valueOf(objID), "light",
                 "0",
                    "0", new String[]{"0","0","0"},
                    new String[]{"0","0","0"}, null);

        }else{          //load object properties
            id = 0;
            int objID;
            if(propertiesList.size()!=0){
                objID= Integer.valueOf(propertiesList.getLast().getObjID())+1;
            }else{
                objID=0;
            }

            op = new ObjectProperties(
                    String.valueOf(objID), e.getObjName(),
                    Integer.toString(e.getFaces().size()),
                    Integer.toString(e.getVertexCount()), new String[]{Float.toString(e.getPivot().getX()),
                    Float.toString(e.getPivot().getY()), Float.toString(e.getPivot().getZ())},
                    new String[]{
                            Float.toString(e.getOrient().getX()),
                            Float.toString(e.getOrient().getY()),
                            Float.toString(e.getOrient().getZ())},
                    previewWindow::updateSelected);
        }
        loadImage(op,id);
    }
    private void setObjectListView() {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.get(propertiesList.size()-1).getButton());
    }
    private void loadImage(ObjectProperties op, int id) {
        if(id==0){      //object
            Image image = new Image(objectIMG);
            op.setImageView(new ImageView(image),coordTextField);
            propertiesList.add(op);
        }
        else{       //light
            Image image = new Image(lightbulbIMG);
            op.setImageView(new ImageView(image),coordTextField);
            propertiesList.add(op);
        }
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
    }

    public void handleKey(KeyEvent event) {
        if (previewWindow != null)
            previewWindow.handleKey(event);
    }
}
