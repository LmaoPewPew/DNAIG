package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.properties.CameraProperties;
import com.softpro.dnaig.properties.LightProperties;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.PortUnreachableException;
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

    //Menu
    @FXML
    private MenuItem menuTheme;
    @FXML
    private ScrollPane scrollPaneProperties;
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
    final List<Entity> entityList = new ArrayList<>();
    private static int objectID = 0;

    public ApplicationController() {
    }


    //TODO: cache ThemeMode and last opened FilePath, even after Closed

    /* *****************************************METHODS***************************************** */


    /*************BUTTON CLICK ACTION METHODS**************/

    @FXML
    void importLightObject(MouseEvent event) throws IOException {
        int id = objectID++;
        openLightPropertiesWindows();
        previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/lightbulb/lightbulb.obj", id);
        createGUIObject(null, id, Config.type.LIGHT);
    }

    @FXML
    void importCameraObject(MouseEvent event) throws IOException{
        int id = objectID++;
        openCameraPropertiesWindows();
        previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/camera/camera.obj", id);
        createGUIObject(null, id, Config.type.CAMERA);
    }

    @FXML
    void importObject(MouseEvent event) {
        File entityFile = fileChooser();
        try {
            int id = objectID++;
            Entity entity = ObjFileReader.createObject(entityFile.getPath(), id);
            System.out.println(entity);
            entityList.add(entity);

            createGUIObject(entity, id, Config.type.OBJECT);
            previewWindow.addObject(entityFile.getPath(), entity.getID());
        } catch (IOException ignored) {
        }
    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        //TODO: open new scene/window from RayTracer after button Pressed!
        System.out.println("loading External Window for RayTracer...");
        Output.openRayTracer(propertiesList);
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
    void createGUIObject(Entity e, int id, Config.type objectType) {
        loadObjectProperties(e, id, objectType);
        setObjectListView();
    }

    // ListView add Objects (IMG-View)
    private void loadObjectProperties(Entity e, int id, Config.type categoryType) {
        ObjectProperties op;
        LightProperties lp;
        CameraProperties cp;

        // TODO
        //  3D model from a light and camera in loadOBJ folder, needs to be added when button clicked   //
        //  /////////////////////////////////////////////////////////////////////////////////////////  //

        if (categoryType == Config.type.LIGHT) {     //load light properties
            lp = new LightProperties(
                    categoryType,
                    Config.lightvariants.POINT,
                    100,
                    this,
                    "light",
                    String.valueOf(id),
                    new String[]{"0", "0", "0"},
                    new String[]{"0", "0", "0"},
                    previewWindow::updateSelected);
            //updateObjectPropertiesMenu(lp);
            loadImage(lp, categoryType);
        } else if (categoryType == Config.type.OBJECT) {          //load object properties
            String objFileName = latestFile.getName().substring(0, latestFile.getName().lastIndexOf('.'));
            e.setObjName(objFileName);

            op = new ObjectProperties(
                    categoryType,
                    String.valueOf(id),
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
                    this,
                    e.getPath());
            //updateObjectPropertiesMenu(op);
            loadImage(op, categoryType);
        } else {          //load camera properties
            cp = new CameraProperties(
                    categoryType,
                    Config.cameravariants.CAM1,
                    this,
                    "camera",
                    String.valueOf(id),
                    new String[]{"0", "0", "0"},
                    new String[]{"0", "0", "0"},
                    1920,
                    1080,
                    previewWindow::updateSelected);
            //updateObjectPropertiesMenu(cp);
            loadImage(cp, categoryType);
        }
    }

    private void setObjectListView() {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.get(propertiesList.size() - 1).getButton());
    }

    // GET IMAGES
    private void loadImage(Properties op, Config.type categoryType) {
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


    public void updateProperties(Config.type contentType){
        GridPane gp = new GridPane();
        gp.setVisible(true);
        gp.addColumn(2);
        gp.addRow(10);
        int id=0;

        for(int i = 0; i < propertiesList.size();i++){
            if (propertiesList.get(i).getId().equals(String.valueOf(lastClickedID))) {
                id = i;
            }
        }


        //add properties: id, name, pos xyz, rot xyz
        gp.add(new Text("ID:"),0,0);
        gp.add(new TextField(propertiesList.get(id).getId()),1,0);

        gp.add(new Text("Name:"),0,1);
        gp.add(new TextField(propertiesList.get(id).getName()),1,1);

        gp.add(new Text("Position:"),0,2);
        gp.add(new Text("X:"),0,3);
        gp.add(new TextField(propertiesList.get(id).getPos()[0]),1,3);
        gp.add(new Text("Y:"),0,4);
        gp.add(new TextField(propertiesList.get(id).getPos()[1]),1,4);
        gp.add(new Text("Z:"),0,5);
        gp.add(new TextField(propertiesList.get(id).getPos()[2]),1,5);

        gp.add(new Text("Rotation:"),0,6);
        gp.add(new Text("X:"),0,7);
        gp.add(new TextField(propertiesList.get(id).getRot()[0]),1,7);
        gp.add(new Text("Y:"),0,8);
        gp.add(new TextField(propertiesList.get(id).getRot()[1]),1,8);
        gp.add(new Text("Z:"),0,9);
        gp.add(new TextField(propertiesList.get(id).getRot()[2]),1,9);

        if(contentType == Config.type.OBJECT){
            System.out.println("Object");
            ObjectProperties op = (ObjectProperties) propertiesList.get(id);
            gp.addRow(2);

            gp.add(new Text("Faces:"),0,10);
            gp.add(new TextField(op.getFaces()),1,10);

            gp.add(new Text("Vertices:"),0,11);
            gp.add(new TextField(op.getVertices()),1,11);
        } else if (contentType == Config.type.LIGHT) {
            System.out.println("Light");
            LightProperties lp = (LightProperties) propertiesList.get(id);
            gp.addRow(2);

            gp.add(new Text("Brigthness:"),0,10);
            gp.add(new TextField(String.valueOf(lp.getBrightness())),1,10);

            gp.add(new Text("Variant:"),0,11);
            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"Light A", "Light B", "Light C"};
            cb.getItems().setAll(choice);
            gp.add(cb,1,11);
        }else if(contentType == Config.type.CAMERA){
            System.out.println("Camera");
            CameraProperties cp = (CameraProperties) propertiesList.get(id);
            gp.addRow(3);

            gp.add(new Text("Length:"),0,10);
            gp.add(new TextField(String.valueOf(cp.getLength())),1,10);

            gp.add(new Text("Width:"),0,11);
            gp.add(new TextField(String.valueOf(cp.getWidth())),1,11);

            gp.add(new Text("Variants:"),0,12);
            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"Camera A", "Camera B", "Camera C"};
            cb.getItems().setAll(choice);
            gp.add(cb,1,12);
        }
        scrollPaneProperties.setContent(gp);
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
