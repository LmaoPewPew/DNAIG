package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.properties.ObjectProperties;
import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.utils.Config;
import com.softpro.dnaig.utils.ObjFileReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ApplicationController {

    // Root
    @FXML
    private VBox parent;

    //IMGs
    URL objectIMG = getClass().getResource("sprites/Obj_img.png");
    URL lightObjImg = getClass().getResource("sprites/light_Img.png");
    URL cameraObjImg = getClass().getResource("sprites/Camera_img.png");

    /**
     * //Relative Path, won't work
     * String objectIMG = "../resources/com/softpro/dnaig/sprites/Obj_img_OLD.png";
     * String lightObjImg = "../resources/com/softpro/dnaig/sprites/light_Img_OLD.png";
     * String cameraObjImg = "../resources/com/softpro/dnaig/sprites/Camera_img.png";
     */


    //ObjectTextField
    //EDITABLE
    @FXML
    private TextField nameTXT;
    @FXML
    private TextField xPosTXT;
    @FXML
    private TextField xRotTXT;
    @FXML
    private TextField yPosTXT;
    @FXML
    private TextField yRotTXT;
    @FXML
    private TextField zPosTXT;
    @FXML
    private TextField zRotTXT;
    //NOT EDITABLE
    @FXML
    private TextField idTXT;
    @FXML
    private TextField facesTXT;
    @FXML
    private TextField verticesTXT;

    //Menu
    @FXML
    private MenuItem menuTheme;
    private boolean isLightMode = true;


    @FXML
    private ListView<Button> objectListView;
    private final LinkedList<Properties> propertiesList = new LinkedList<>();
    private String lastClickedID = "0";
    private final FileChooser fileChooser = new FileChooser();
    File latestFile = null;
    @FXML
    private StackPane previewPane;
    private PreviewWindow previewWindow;

    //private final ObjFileReader objImporter = new ObjFileReader();
    final List<Entity> entityList = new ArrayList<>();

    public ApplicationController() {
    }


    //TODO: cache ThemeMode and last opened FilePath, even after Closed

    /* *****************************************METHODS***************************************** */


    /*************BUTTON CLICK ACTION METHODS**************/

    @FXML
    void importLightObject(MouseEvent event) throws IOException {
        openLightPropertiesWindows();
        previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/cube/cube.obj", -1);
        createGUIObject(null, Config.type.LIGHT);
    }

    @FXML
    void importCameraObject(MouseEvent event) throws IOException{
        openCameraPropertiesWindows();
        previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/cube/cube.obj", -1);
        createGUIObject(null, Config.type.CAMERA);
    }

    @FXML
    void importObject(MouseEvent event) {
        File entityFile = fileChooser();
        try {
            Entity entity = ObjFileReader.createObject(entityFile.getPath());
            System.out.println(entity);
            entityList.add(entity);

            createGUIObject(entity, Config.type.OBJECT);
            previewWindow.addObject(entityFile.getPath(), entity.getID());
        } catch (IOException ignored) {
        }
    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        //TODO: open new scene/window from RayTracer after button Pressed!
        System.out.println("loading External Window for RayTracer...");
        //openRayTracer(propertiesList)

        //for (Properties properties : propertiesList) {
        //    System.out.println("Test -> propertiesID: " + properties.getId());
        //}
    }


    /*************MENU-ITEM METHODS**************/
    //FILE

    //EDIT
    @FXML
    void changeThemeMode(ActionEvent event) {
        isLightMode = !isLightMode;
        if (isLightMode) setLightMode();
        else setDarkMode();
    }

    //HELP

    /*************OBJECTS**************/

    // OPEN OBJECT SETTINGS SCENE (LIGHT AND/OR CAMERA)
    private String openLightPropertiesWindows() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lightProperties.fxml")));

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("lightProperties");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

        return "0";
    }

    private String openCameraPropertiesWindows() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cameraProperties.fxml")));

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("cameraProperties");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

        return "0";
    }

    // ListView
    void createGUIObject(Entity e, Config.type objectType) {
        loadObjectProperties(e,objectType);
        setObjectListView();
    }

    // ListView add Objects (IMG-View)
    private void loadObjectProperties(Entity e, Config.type categoryType) {
        ObjectProperties op;

        // TODO
        //  3D model from a light and camera in loadOBJ folder, needs to be added when button clicked   //
        //  /////////////////////////////////////////////////////////////////////////////////////////  //

        if (categoryType == Config.type.LIGHT) {     //load light properties
            //TODO: fix ID
            int objID;
            if (!propertiesList.isEmpty()) {
                objID = Integer.parseInt(propertiesList.getLast().getId()) + 1;
            } else {
                objID = 0;
            }
            op = new ObjectProperties(
                    categoryType,
                    String.valueOf(objID),
                    "light",
                    "N/A",
                    "N/A",
                    new String[]{"0", "0", "0"},
                    new String[]{"0", "0", "0"},
                    null,
                    this);

        } else if(categoryType == Config.type.OBJECT){          //load object properties
            ////TODO: fix id -> id ist noch nicht gesynced mit der ID in der LinkedList
            String objFileName = latestFile.getName().substring(0, latestFile.getName().lastIndexOf('.'));
            e.setObjName(objFileName);

            op = new ObjectProperties(
                    categoryType,
                    String.valueOf(e.getID()),
                    e.getObjName(),
                    Integer.toString(e.getFaces().size()),
                    Integer.toString(e.getVertexCount()),
                    new String[]{
                            Float.toString(e.getPivot().getX()),
                            Float.toString(e.getPivot().getY()),
                            Float.toString(e.getPivot().getZ())},
                    new String[]{
                            Float.toString(e.getOrient().getX()),
                            Float.toString(e.getOrient().getY()),
                            Float.toString(e.getOrient().getZ())},
                    previewWindow::updateSelected,
                    this);
        }else{          //load camera properties
            int objID;
            if (!propertiesList.isEmpty()) {
                objID = Integer.parseInt(propertiesList.getLast().getId()) + 1;
            } else {
                objID = 0;
            }

            op = new ObjectProperties(
                    categoryType,
                    String.valueOf(objID),
                    "camera",
                    "N/A",
                    "N/A",
                    new String[]{"0", "0", "0"},
                    new String[]{"0", "0", "0"},
                    null,
                    this);
        }

        updateObjectPropertiesMenu(op);
        loadImage(op, categoryType);
    }

    private void setObjectListView() {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.get(propertiesList.size() - 1).getButton());
    }

    // GET IMAGES
    private void loadImage(ObjectProperties op, Config.type categoryType) {
        Image image;
        if (categoryType == Config.type.OBJECT) {      //object
            try {
                image = new Image(objectIMG.toURI().getPath().substring(1));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else if (categoryType == Config.type.LIGHT) {       //light
            try {
                image = new Image(lightObjImg.toURI().getPath().substring(1));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {    // camera
            try {
                image = new Image(cameraObjImg.toURI().getPath().substring(1));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        op.setImageView(new ImageView(image));
        propertiesList.add(op);
    }


    /*************OBJECT COORDINATES**************/

    // Write values into Coord-Sys
    void updateObjectPropertiesMenu(ObjectProperties op) {

        this.idTXT.setText(op.getId());
        this.nameTXT.setText(op.getName());
        this.facesTXT.setText(op.getFaces());
        this.verticesTXT.setText(op.getVertices());

        this.xPosTXT.setText(op.getPos()[0]);
        this.yPosTXT.setText(op.getPos()[1]);
        this.zPosTXT.setText(op.getPos()[2]);

        this.xRotTXT.setText(op.getRot()[0]);
        this.yRotTXT.setText(op.getRot()[1]);
        this.zRotTXT.setText(op.getRot()[2]);
    }

    public void updateObjectPropertiesMenu(String[] s) {

        this.idTXT.setText(s[0]);
        this.nameTXT.setText(s[1]);
        this.facesTXT.setText(s[2]);
        this.verticesTXT.setText(s[3]);

        this.xPosTXT.setText(s[4]);
        this.yPosTXT.setText(s[5]);
        this.zPosTXT.setText(s[6]);

        this.xRotTXT.setText(s[7]);
        this.yRotTXT.setText(s[8]);
        this.zRotTXT.setText(s[9]);
    }


    // Live Update Coord-Sys Bar


    // Read Values from Coord-Sys


    // Update Object from updated values


    /*************THEME CHANGE**************/


    private void setLightMode() {
        removeStylesheet("DarkMode.css");
        addStylesheet("LightMode.css");
    }

    private void setDarkMode() {
        removeStylesheet("LightMode.css");
        addStylesheet("DarkMode.css");
    }

    private void addStylesheet(String stylesheet) {
        String stylesheetPath = Objects.requireNonNull(getClass().getResource("/com/softpro/dnaig/style/" + stylesheet)).toExternalForm();
        parent.getStylesheets().add(stylesheetPath);
    }

    private void removeStylesheet(String stylesheet) {
        String stylesheetPath = Objects.requireNonNull(getClass().getResource("/com/softpro/dnaig/style/" + stylesheet)).toExternalForm();
        parent.getStylesheets().remove(stylesheetPath);
    }



    /*
    private void setLightMode() {
        parent.getStylesheets().remove("../resources/com/softpro/dnaig/style/DarkMode.css");
        parent.getStylesheets().add("../resources/com/softpro/dnaig/style/LightMode.css");

        //parent.getStylesheets().remove("D:\\Code\\java\\school\\DNAIG\\src\\main\\resources\\com\\softpro\\dnaig\\style\\DarkMode.css");
        //parent.getStylesheets().add("D:\\Code\\java\\school\\DNAIG\\src\\main\\resources\\com\\softpro\\dnaig\\style\\LightMode.css");

    }

    private void setDarkMode() {
        parent.getStylesheets().remove("../resources/com/softpro/dnaig/style/LightMode.css");
        parent.getStylesheets().add("../resources/com/softpro/dnaig/style/DarkMode.css");


        //parent.getStylesheets().remove("D:\\Code\\java\\school\\DNAIG\\src\\main\\resources\\com\\softpro\\dnaig\\style\\LightMode.css");
        //parent.getStylesheets().add("D:\\Code\\java\\school\\DNAIG\\src\\main\\resources\\com\\softpro\\dnaig\\style\\DarkMode.css");
    }

    /*************One Time METHODS**************/

    // Location might change once finished (?)
    public void deleteObject(int objID) {
        for (Properties properties : propertiesList) {
            if (properties.getId().equals(String.valueOf(objID))) {
                objectListView.getItems().remove(properties.getButton());
                propertiesList.remove(properties);
                break;
            }
        }
    }

    // FILES AND PATHS
    File fileChooser() {
        fileChooser.setTitle("Open .obj File");
        String fileString;

        latestFile = fileChooser.showOpenDialog(new Stage());
        fileString = String.valueOf(latestFile.getParentFile());
        fileChooser.setInitialDirectory(new File(fileString));

        return latestFile;
    }

    public void setLastClickedID(String s) {
        this.lastClickedID = s;
    }

    public void initialize() {
        previewWindow = new PreviewWindow(previewPane, this);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ Files", "*.obj"));
    }

    public void handleKey(KeyEvent event) {
        if (previewWindow != null) previewWindow.handleKey(event);
    }
}
