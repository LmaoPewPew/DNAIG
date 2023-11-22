package com.softpro.dnaig;

import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.properties.ObjectProperties;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.utils.ObjFileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class ApplicationController {

    // Root
    @FXML
    private VBox parent;

    //IMGs
    String objectIMG = "https://i.imgur.com/4JzMipP.png";
    String lightObjImg = "https://i.imgur.com/nbKsECu.png";
    String cameraObjImg = "https://i.imgur.com/nbKsECu.png"; //WRONG IMAGE LINK

    /**
     * //Relative Path, won't work
     * String objectIMG = "../resources/com/softpro/dnaig/sprites/Obj_img_OLD.png";
     * String lightObjImg = "../resources/com/softpro/dnaig/sprites/light_Img_OLD.png";
     * String cameraObjImg = "../resources/com/softpro/dnaig/sprites/Camera_img.png";
     */


    //ObjectTextField
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

    @FXML
    private Text facesTXT;
    @FXML
    private Text verticesTXT;

    //private  TextField[] textFieldArray = {nameTXT, xPosTXT, yPosTXT, zPosTXT, xRotTXT, yRotTXT, zRotTXT};
    //private  Text[] textArray = {facesTXT, verticesTXT};

    //Menu
    @FXML
    private MenuItem menuTheme;
    private boolean isLightMode = true;


    //Rest
    @FXML
    private ChoiceBox<?> choiceBoxLightProperties;
    @FXML
    private ListView<Button> objectListView;
    private final LinkedList<ObjectProperties> propertiesList = new LinkedList<>();
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
        String values = openPropertiesWindows();
        System.out.println("importLightObjects Values: " + values);
        createGUIObject(null);
    }

    @FXML
    void importCameraObject(MouseEvent event) {
        System.out.println("ADD CAMERA");
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
        } catch (IOException ignored) {
        }
    }

    @FXML
    void loadRayTracer(MouseEvent event) {
        //TODO: open new scene/window from RayTracer after button Pressed!
        System.out.println("loading External Window for RayTracer...");
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
    private String openPropertiesWindows() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lightProperties.fxml")));

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("lightProperties");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

        return "0";
    }

    // ListView
    void createGUIObject(Entity e) {
        loadObjectProperties(e);
        setObjectListView();
    }

    // ListView add Objects (IMG-View)
    private void loadObjectProperties(Entity e) {
        ObjectProperties op;
        int id;

        //TODO
        // 3D model from a light and camera in loadOBJ folder, needs to be added when button clicked
        // /////////////////////////////////////////////////////////////////////////////////////////  //

        if (e == null) {     //load light properties
            id = 1;
            int objID;
            if (!propertiesList.isEmpty()) {
                objID = Integer.parseInt(propertiesList.getLast().getObjID()) + 1;
            } else {
                objID = 0;
            }
            op = new ObjectProperties(String.valueOf(objID), "light", "N/A", "N/A", new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, null, this);

        } else {          //load object properties
            id = 0;
            int objID;
            if (!propertiesList.isEmpty()) {
                objID = Integer.parseInt(propertiesList.getLast().getObjID()) + 1;
            } else {
                objID = 0;
            }

            String objFileName = latestFile.getName().substring(0, latestFile.getName().lastIndexOf('.'));
            e.setObjName(objFileName);

            op = new ObjectProperties(String.valueOf(objID), e.getObjName(), Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()), new String[]{Float.toString(e.getPivot().getX()), Float.toString(e.getPivot().getY()), Float.toString(e.getPivot().getZ())}, new String[]{Float.toString(e.getOrient().getX()), Float.toString(e.getOrient().getY()), Float.toString(e.getOrient().getZ())}, previewWindow::updateSelected, this);

        }

        updateObjectPropertiesMenu(op);
        loadImage(op, id);
    }

    private void setObjectListView() {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.get(propertiesList.size() - 1).getButton());
    }

    // GET IMAGES
    private void loadImage(ObjectProperties op, int id) {
        Image image;
        if (id == 0) {      //object
            image = new Image(objectIMG);
        } else if (id == 1) {       //light
            image = new Image(lightObjImg);
        } else {
            image = new Image(cameraObjImg);
        }
        op.setImageView(new ImageView(image));
        propertiesList.add(op);
    }


    /*************OBJECT COORDINATES**************/

    // Write values into Coord-Sys
    void updateObjectPropertiesMenu(ObjectProperties op) {

        this.nameTXT.setText(op.getObjName());
        this.facesTXT.setText(op.getObjFaces());
        this.verticesTXT.setText(op.getObjVertices());

        this.xPosTXT.setText(op.getObjPos()[0]);
        this.yPosTXT.setText(op.getObjPos()[1]);
        this.zPosTXT.setText(op.getObjPos()[2]);

        this.xRotTXT.setText(op.getObjRot()[0]);
        this.yRotTXT.setText(op.getObjRot()[1]);
        this.zRotTXT.setText(op.getObjRot()[2]);
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
        String stylesheetPath = getClass().getResource("/com/softpro/dnaig/style/" + stylesheet).toExternalForm();
        parent.getStylesheets().add(stylesheetPath);
    }

    private void removeStylesheet(String stylesheet) {
        String stylesheetPath = getClass().getResource("/com/softpro/dnaig/style/" + stylesheet).toExternalForm();
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
    void deleteObject() {
        //?? onKeyPressed (del or backspace)
        objectListView.getItems().remove(Integer.parseInt(lastClickedID));
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
        previewWindow = new PreviewWindow(previewPane);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ Files", "*.obj"));
    }

    public void handleKey(KeyEvent event) {
        if (previewWindow != null) previewWindow.handleKey(event);
    }
}
