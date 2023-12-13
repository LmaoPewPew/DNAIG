package com.softpro.dnaig;

import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.properties.CameraProperties;
import com.softpro.dnaig.properties.LightProperties;
import com.softpro.dnaig.properties.ObjectProperties;
import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.rayTracer.CustomScene;
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
    private MenuItem mExport;
    @FXML
    private MenuItem mImport;
    @FXML
    private MenuItem mSave;
    @FXML
    private MenuItem mSaveAs;
    @FXML
    private MenuItem menuTheme;

    //Rest
    @FXML
    private Button renderButton;
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
    void importCameraObject(MouseEvent event) throws IOException {
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

    /*************MENU-ITEM METHODS**************/
    //FILE
    @FXML
    void saveFile(ActionEvent event) {
        // Automatically Save .YAML file in Downloads folder
        FileChooser directoryChooser = new FileChooser();

        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG/JPG", "*.jpg", "*.jpeg"));
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));

        directoryChooser.setTitle("Save Image");
        File file = directoryChooser.showSaveDialog(new Stage());
        try{
            Output.getOutput().exportImage(file);

        }catch (Exception e){
            System.out.println("No File selected");
        }
    }

    @FXML
    void saveFileAs(ActionEvent event) {
        // Save .YAML file in specific folder (Downloads will open first tho)
    }

    @FXML
    void exportYaml(ActionEvent event) {
        // Save Image of the Rendered image in specific Folder (like SaveFileAs)
        FileChooser directoryChooser = new FileChooser();

        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML", "*.yaml"));
        directoryChooser.setTitle("Save Scene");

        File file = directoryChooser.showSaveDialog(new Stage());

        try{
            System.out.println(file.getPath());
            CustomScene.getScene().yamlExport(file);
        }catch (Exception e) {
            System.out.println("No File selected");
        }
    }

    @FXML
    void importYaml(ActionEvent event) {
        // Loads .YAML file into project
    }

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
    private void openLightPropertiesWindows() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lightProperties.fxml")));

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("lightProperties");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

    }

    private void openCameraPropertiesWindows() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cameraProperties.fxml")));

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("cameraProperties");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

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

        if (categoryType == Config.type.LIGHT) {     //load light properties
            lp = new LightProperties(categoryType, Config.lightvariants.POINT, 100, "light", String.valueOf(id), new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"});
            loadImage(lp, categoryType);
        } else if (categoryType == Config.type.OBJECT) {          //load object properties
            String objFileName = latestFile.getName().substring(0, latestFile.getName().lastIndexOf('.'));
            e.setObjName(objFileName);

            op = new ObjectProperties(categoryType, e, String.valueOf(id), e.getObjName(), Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()), new String[]{Double.toString(e.getPivot().getX()), Double.toString(e.getPivot().getY()), Double.toString(e.getPivot().getZ())}, new String[]{Double.toString(e.getOrient().getX()), Double.toString(e.getOrient().getY()), Double.toString(e.getOrient().getZ())});
            loadImage(op, categoryType);
        } else {          //load camera properties
            cp = new CameraProperties(categoryType, Config.cameravariants.CAM1, "camera", String.valueOf(id), new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, 1920, 1080);
            loadImage(cp, categoryType);
        }
    }

    private void setObjectListView() {
        //ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.getLast().getButton()); //davor  propertiesList.get(propertiesList.size() - 1).getButton()
    }

    private void setButton(ImageView iv, Config.type categoryType) {
        Button button = propertiesList.getLast().getButton();
        iv.setFitWidth(100);
        iv.setFitHeight(100);
        button.setGraphic(iv);
        button.setOnAction(e -> {
            Object x = e.getSource();
            for (Properties properties : propertiesList) {
                if (properties.getButton().equals(x)) {
                    setLastClickedID(properties.getId());
                    break;
                }
            }
            updateProperties(categoryType);
            previewWindow.updateSelected(Integer.parseInt(lastClickedID));
        });
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
        propertiesList.add(op);
        setButton(new ImageView(image), categoryType); //setButton and call function
    }


    /*************OBJECT COORDINATES**************/


    public void updateProperties(Config.type contentType) {
        GridPane gp = new GridPane();
        gp.setVisible(true);
        gp.addColumn(2);
        gp.addRow(13);

        gp.setPadding(new Insets(5, 10, 5, 10));
        gp.setVgap(8);

        int id = 0;

        for (int i = 0; i < propertiesList.size(); i++) {
            if (propertiesList.get(i).getId().equals(String.valueOf(lastClickedID))) {
                id = i;
            }
        }

        //add properties: id, name, pos xyz, rot xyz
        String[] textFieldVALUES = new String[11];

        TextField idTF = new TextField(propertiesList.get(id).getId());
        idTF.setEditable(false);
        TextField nameTF = new TextField(propertiesList.get(id).getName());
        textFieldVALUES[0] = nameTF.getText();

        gp.add(new Text("ID:"), 0, 0);
        gp.add(idTF, 1, 0);

        gp.add(new Text("Name:"), 0, 1);
        gp.add(nameTF, 1, 1);


        gp.add(new Text("Position:"), 0, 2);

        TextField xPos = new TextField(propertiesList.get(id).getPos()[0]);
        TextField yPos = new TextField(propertiesList.get(id).getPos()[1]);
        TextField zPos = new TextField(propertiesList.get(id).getPos()[2]);
        numericOnly(xPos);
        numericOnly(yPos);
        numericOnly(zPos);
        textFieldVALUES[1] = xPos.getText();
        textFieldVALUES[2] = yPos.getText();
        textFieldVALUES[3] = zPos.getText();


        gp.add(new Text("X:"), 0, 3);
        gp.add(xPos, 1, 3);
        gp.add(new Text("Y:"), 0, 4);
        gp.add(yPos, 1, 4);
        gp.add(new Text("Z:"), 0, 5);
        gp.add(zPos, 1, 5);

        gp.add(new Text("Rotation:"), 0, 6);

        TextField xRot = new TextField(propertiesList.get(id).getRot()[0]);
        TextField yRot = new TextField(propertiesList.get(id).getRot()[1]);
        TextField zRot = new TextField(propertiesList.get(id).getRot()[2]);
        numericOnly(xRot);
        numericOnly(yRot);
        numericOnly(zRot);
        textFieldVALUES[4] = xRot.getText();
        textFieldVALUES[5] = yRot.getText();
        textFieldVALUES[6] = zRot.getText();


        gp.add(new Text("X:"), 0, 7);
        gp.add(xRot, 1, 7);
        gp.add(new Text("Y:"), 0, 8);
        gp.add(yRot, 1, 8);
        gp.add(new Text("Z:"), 0, 9);
        gp.add(zRot, 1, 9);


        if (contentType == Config.type.OBJECT) {
            System.out.println("Object");
            ObjectProperties op = (ObjectProperties) propertiesList.get(id);

            TextField scaleTF = new TextField(op.getRot()[0]);
            gp.add(new Text("Scale:"), 0, 10);
            gp.add(scaleTF, 1, 10);
            textFieldVALUES[7] = scaleTF.getText();

            TextField facesTF = new TextField(op.getFaces());
            facesTF.setEditable(false);

            TextField vertTF = new TextField(op.getVertices());
            vertTF.setEditable(false);

            gp.addRow(2);

            gp.add(new Text("Faces:"), 0, 11);
            gp.add(facesTF, 1, 11);
            gp.add(new Text("Vertices:"), 0, 12);
            gp.add(vertTF, 1, 12);

        } else if (contentType == Config.type.LIGHT) {
            System.out.println("Light");
            LightProperties lp = (LightProperties) propertiesList.get(id);
            gp.addRow(2);

            gp.add(new Text("Brigthness:"), 0, 11);
            gp.add(new TextField(String.valueOf(lp.getBrightness())), 1, 11);

            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"Light A", "Light B", "Light C"};
            cb.getItems().setAll(choice);
            gp.add(new Text("Variant:"), 0, 12);
            gp.add(cb, 1, 12);

            textFieldVALUES[7] = String.valueOf(lp.getBrightness());
            textFieldVALUES[8] = cb.getValue();
        } else if (contentType == Config.type.CAMERA) {
            System.out.println("Camera");
            CameraProperties cp = (CameraProperties) propertiesList.get(id);
            gp.addRow(3);

            gp.add(new Text("Length:"), 0, 11);
            gp.add(new TextField(String.valueOf(cp.getLength())), 1, 11);

            gp.add(new Text("Width:"), 0, 12);
            gp.add(new TextField(String.valueOf(cp.getWidth())), 1, 12);

            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"Camera A", "Camera B", "Camera C"};
            cb.getItems().setAll(choice);
            gp.add(new Text("Variants:"), 0, 13);
            gp.add(cb, 1, 13);

            textFieldVALUES[7] = String.valueOf(cp.getLength());
            textFieldVALUES[8] = String.valueOf(cp.getWidth());
            textFieldVALUES[9] = cb.getValue();
        }
        scrollPaneProperties.setContent(gp);
        //updateModelSettings(contentType, id, textFieldVALUES);
    }


    //////Da PreviewWindow nich wirklich wichtig ist, SOUT einfach den wert jedesMal

        //Muss schauen ob das unten auch klappt



    // Live Update Coord-Sys Bar
    public void updateModelSettings(Config.type contentType, int id, String[] values) {
        //change common settings:
        propertiesList.get(id).setName(values[0]);

        String[] pos = {values[1], values[2], values[3]};
        propertiesList.get(id).setPos(pos);

        String[] rot = {values[4], values[5], values[6]};
        propertiesList.get(id).setRot(rot);

        if (contentType == Config.type.OBJECT) {
            ObjectProperties properties = (ObjectProperties) propertiesList.get(id);

            properties.setScale(values[7]);
        } else if (contentType == Config.type.LIGHT) {
            LightProperties properties = (LightProperties) propertiesList.get(id);

            properties.setBrightness(values[7]);
            properties.setLightvariants(Config.lightvariants.valueOf(values[8]));
        } else if (contentType == Config.type.CAMERA) {
            CameraProperties properties = (CameraProperties) propertiesList.get(id);

            properties.setLength(Integer.parseInt(values[7]));
            properties.setWidth(Integer.parseInt(values[8]));
            properties.setLightvariants(Config.cameravariants.valueOf(values[9]));

        }
    }


    private void numericOnly(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) field.setText(newValue.replaceAll("[^\\d.]", ""));
        });
    }

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


    /*************Render Functions**************/
    @FXML
    void renderFunc(ActionEvent event) {
        switchRenderButtonStyleClass();

        System.out.println(renderButton.getText());
        System.out.println(renderButton.getStyleClass());

        if ((renderButton.getText().equals("Cancel"))) loadRayTracer();
        else cancelRayTracer();
    }

    void switchRenderButtonStyleClass() {
        if ((renderButton.getText().equals("Render"))) {
            renderButton.setText("Cancel");
            renderButton.getStyleClass().remove("button");
            renderButton.getStyleClass().add("renderButtonPressed");
        } else {
            renderButton.setText("Render");
            renderButton.getStyleClass().remove("renderButtonPressed");
            renderButton.getStyleClass().add("button");
        }
    }

    void cancelRayTracer() {
        //Code that stops RayTracer...
        System.out.println("getting Canceled!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //output.cancelRayTracer();
        Output.getOutput().cancelRayTracer();
        switchRenderButtonStyleClass();
    }

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void callbackWhenRayTracerFinished(Void unused) {
        switchRenderButtonStyleClass();
    }

    void loadRayTracer() {
        Output output = Output.getOutput();
        output.clear();
        output.openRayTracer(propertiesList, stage, this::callbackWhenRayTracerFinished);
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
