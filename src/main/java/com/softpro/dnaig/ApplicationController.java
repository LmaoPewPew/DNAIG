package com.softpro.dnaig;

import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.light.PointLight;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.preview.PreviewWindow;
import com.softpro.dnaig.properties.CameraProperties;
import com.softpro.dnaig.properties.LightProperties;
import com.softpro.dnaig.properties.ObjectProperties;
import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.rayTracer.Camera;
import com.softpro.dnaig.rayTracer.CustomScene;
import com.softpro.dnaig.utils.Config;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;
import com.softpro.dnaig.utils.YAMLexporter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class ApplicationController {

    // Root
    @FXML
    private VBox parent;

    //IMGs
    URL objectIMG = getClass().getResource("sprites/Obj_img.png");
    URL lightObjImg = getClass().getResource("sprites/light_Img.png");
    URL cameraObjImg = getClass().getResource("sprites/Camera_img.png");

    //Menu
    @FXML
    private MenuItem mExport;
    @FXML
    private MenuItem mImport;
    @FXML
    private MenuItem mSave;
    @FXML
    private RadioMenuItem mTheme;
    @FXML
    private MenuItem mAbout;

    //Rest
    private final FileChooser directoryChooser = new FileChooser();
    @FXML
    private Button renderButton;
    @FXML
    private ScrollPane scrollPaneProperties;
    @FXML
    private ListView<Button> objectListView;
    private final LinkedList<Properties> propertiesList = new LinkedList<>();
    private String lastClickedID = "0";
    private final FileChooser fileChooser = new FileChooser();
    File latestFile = null;
    @FXML
    private StackPane previewPane;
    private PreviewWindow previewWindow;
    final ArrayList<Entity> entityList = new ArrayList<>();
    final ArrayList<Light> lightList = new ArrayList<>();
    private static int objectID = 0;
    private boolean camExist = false;
    private Stage stage;
    private boolean textFocus = false;


    /* *****************************************METHODS***************************************** */


    /*************BUTTON CLICK ACTION METHODS**************/

    /**
     * Imports a light object and adds it to the GUI and preview window.
     *
     * @param event The MouseEvent triggering the import operation.
     * @throws IOException If an I/O error occurs during the import process.
     */
    @FXML
    void importLightObject(MouseEvent event) throws IOException {
        int id = objectID;
        createGUIObject(null, id, Config.type.LIGHT);

        try {
            previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/lightbulb/lightbulb.obj", id);
        } catch (Exception e) {
            previewWindow.addObject("app/lightbulb/lightbulb.obj", id);
        }

        objectID++;
    }

    /**
     * Imports a camera object, sets the 'camExist' flag, and adds it to the GUI and preview window.
     *
     * @param event The MouseEvent triggering the import operation.
     * @throws IOException If an I/O error occurs during the import process.
     */
    @FXML
    void importCameraObject(MouseEvent event) throws IOException {
        if (!camExist) {
            camExist = true;
            System.out.println(camExist);

            int id = objectID++;

            try {
                previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/camera/camera.obj", id);
            } catch (Exception e) {
                previewWindow.addObject("app/camera/camera.obj", id);
            }

            createGUIObject(null, id, Config.type.CAMERA);
        }
    }

    /**
     * Imports an object from a specified file, creates an Entity, and adds it to the GUI and preview window.
     *
     * @param event The MouseEvent triggering the import operation.
     */
    @FXML
    void importObject(MouseEvent event) {
        File entityFile = fileChooser();
        if (entityFile == null) return;

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

    /**
     * Saves the rendered image in various image formats (PNG, JPEG/JPG, BMP) based on user selection.
     *
     * @param event The ActionEvent triggering the save operation.
     */
    @FXML
    void saveFile(ActionEvent event) {
        // Automatically Save .YAML file in Downloads folder
        //FileChooser directoryChooser = new FileChooser();

        directoryChooser.getExtensionFilters().clear();
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG/JPG", "*.jpg", "*.jpeg"));
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP", "*.bmp"));

        directoryChooser.setTitle("Save Image");
        File file = directoryChooser.showSaveDialog(new Stage());
        try {
            Output.getOutput().exportImage(file);

        } catch (Exception e) {
            System.out.println("No File selected");
        }
    }

    /**
     * Exports the current scene to a YAML file.
     *
     * @param event The ActionEvent triggering the export.
     */
    @FXML
    void exportYaml(ActionEvent event) {
        // Save Image of the Rendered image in specific Folder (like SaveFileAs)
        //FileChooser directoryChooser = new FileChooser();

        directoryChooser.getExtensionFilters().clear();
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML", "*.yaml"));
        directoryChooser.setTitle("Save Scene");

        File file = directoryChooser.showSaveDialog(new Stage());

        try {
            System.out.println(file.getPath());
            CustomScene.getScene().yamlExport(file);
        } catch (Exception e) {
            System.out.println("No File selected");
        }
    }

    @FXML
    void importYaml(ActionEvent event) {
        // Loads .YAML file into project
        //FileChooser directoryChooser = new FileChooser();

        directoryChooser.getExtensionFilters().clear();
        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML", "*.yaml"));
        directoryChooser.setTitle("Load Scene");

        File file = directoryChooser.showOpenDialog(new Stage());

        try {
            Camera camera =  new Camera();
            System.out.println(file.getPath());
            //CustomScene.getScene().yamlImport(file);
            YAMLexporter.importScene(file, entityList, lightList, camera);
            for (Entity entity : entityList) {
                int id = objectID++;
                latestFile = new File(entity.getObjPath());
                entity.setId(id);
                createGUIObject(entity, id, Config.type.OBJECT);
                previewWindow.addObject(entity.getObjPath(), entity.getID());
                previewWindow.updatePosition(id, entity.getPivot().getX(), entity.getPivot().getY(), entity.getPivot().getZ(),
                        entity.getOrient().getX(), entity.getOrient().getY(), entity.getOrient().getZ(), entity.getScale());

                for (Properties properties : propertiesList) {
                    if (properties instanceof ObjectProperties objectProperties) {
                        if (objectProperties.getId().equals(String.valueOf(id))) {
                            objectProperties.setScale(String.valueOf(entity.getScale()));
                            objectProperties.setPos(new String[]{String.valueOf(entity.getPivot().getX()), String.valueOf(entity.getPivot().getY()), String.valueOf(entity.getPivot().getZ())});
                            objectProperties.setRot(new String[]{String.valueOf(entity.getOrient().getX()), String.valueOf(entity.getOrient().getY()), String.valueOf(entity.getOrient().getZ()),});
                            break;
                        }
                    }
                }
            }
            for (Light light : lightList) {
                int id = objectID++;
                light.setID(id);
                System.out.println(light.toYaml());
                LightProperties lp = new LightProperties(Config.type.LIGHT, Config.lightvariants.POINT, String.valueOf(light.getIntensity()), "light", String.valueOf(id), new String[]{String.valueOf(light.getPosition().getX()), String.valueOf(light.getPosition().getY()), String.valueOf(light.getPosition().getZ())}, new String[]{"0","0","0"}, new String[]{String.valueOf(light.getRgb().getX()), String.valueOf(light.getRgb().getY()), String.valueOf(light.getRgb().getZ())});
                //loadImage(lp, Config.type.LIGHT);
                createGUIObject(null, id, Config.type.LIGHT);

                try {
                    previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/lightbulb/lightbulb.obj", id);
                } catch (Exception e) {
                    previewWindow.addObject("app/lightbulb/lightbulb.obj", id);
                }

                previewWindow.updatePosition(id, light.getPosition().getX(), light.getPosition().getY(), light.getPosition().getZ(),
                        0, 0, 0, 1);

                for (Properties properties : propertiesList) {
                    if (properties.getId().equals(String.valueOf(id))) {
                        if (properties instanceof LightProperties lightProperties) {
                            lightProperties.setIntensity(String.valueOf(light.getIntensity()));
                            lightProperties.setPos(new String[]{String.valueOf(light.getPosition().getX()), String.valueOf(light.getPosition().getY()), String.valueOf(light.getPosition().getZ())});
                            lightProperties.setRot(new String[]{"0", "0", "0"});
                            lightProperties.setRgb(new String[]{String.valueOf(light.getRgb().getX()), String.valueOf(light.getRgb().getY()), String.valueOf(light.getRgb().getZ())});
                        }
                    }
                }
            }

            int id = objectID++;

            try {
                previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/camera/camera.obj", id);
            } catch (Exception e) {
                previewWindow.addObject("app/camera/camera.obj", id);
            }

            createGUIObject(null, id, Config.type.CAMERA);
            CameraProperties cp = new CameraProperties(Config.type.CAMERA, Config.cameravariants.HD, "camera", String.valueOf(id), new String[]{String.valueOf(camera.getEye().getX()), String.valueOf(camera.getEye().getY()), String.valueOf(camera.getEye().getZ())}, new String[]{"0", "0", "0"}, 1280, 720);
            //loadImage(cp, Config.type.CAMERA);
            camExist = true;
            previewWindow.updatePosition(id, camera.getEye().getX(), camera.getEye().getY(), camera.getEye().getZ(),
                    0, 0, 0, 1);

            for (Properties properties : propertiesList) {
                if (properties.getId().equals(String.valueOf(id))) {
                    properties.setPos(new String[]{String.valueOf(camera.getEye().getX()), String.valueOf(camera.getEye().getY()), String.valueOf(camera.getEye().getZ())});
                    properties.setRot(new String[]{"0", "0", "0"});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeThemeMode(ActionEvent event) {
        if (mTheme.isSelected()){
            mTheme.setText("Darkmode");
            setDarkMode();
        }
        else {
            mTheme.setText("Lightmode");
            setLightMode();
        }
    }

    /*************OBJECTS**************/

    /**
     * Creates a GUI representation of an object, adding it to the object list view.
     *
     * @param e          The Entity object representing the object.
     * @param id         The ID of the object.
     * @param objectType The type of the object (LIGHT, OBJECT, CAMERA).
     */
    void createGUIObject(Entity e, int id, Config.type objectType) {
        loadObjectProperties(e, id, objectType);
        setObjectListView();
    }

    /**
     * Loads properties of an object (Object, Light, or Camera) based on its type.
     *
     * @param e            The Entity object representing the object.
     * @param id           The ID of the object.
     * @param categoryType The type of the object (LIGHT, OBJECT, CAMERA).
     */
    private void loadObjectProperties(Entity e, int id, Config.type categoryType) {
        ObjectProperties op;
        LightProperties lp;
        CameraProperties cp;

        if (categoryType == Config.type.LIGHT) {     //load light properties
            lp = new LightProperties(categoryType, Config.lightvariants.POINT, "5", "light", String.valueOf(id), new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, new String[]{"1", "1", "1"});
            loadImage(lp, categoryType);
        } else if (categoryType == Config.type.OBJECT) {          //load object properties
            String objFileName = latestFile.getName().substring(0, latestFile.getName().lastIndexOf('.'));
            e.setObjName(objFileName);

            op = new ObjectProperties(categoryType, e, String.valueOf(id), e.getObjName(), Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()), new String[]{Double.toString(e.getPivot().getX()), Double.toString(e.getPivot().getY()), Double.toString(e.getPivot().getZ())}, new String[]{Double.toString(e.getOrient().getX()), Double.toString(e.getOrient().getY()), Double.toString(e.getOrient().getZ())});
            loadImage(op, categoryType);
        } else {          //load camera properties
            cp = new CameraProperties(categoryType, Config.cameravariants.HD, "camera", String.valueOf(id), new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, 1280, 720);
            loadImage(cp, categoryType);
        }
    }

    /**
     * Adds the button representing the object to the object list view.
     */
    private void setObjectListView() {
        // ObjectList Region
        objectListView.setPadding(new Insets(10, 5, 10, 5));
        objectListView.getItems().add(propertiesList.getLast().getButton()); // davor propertiesList.get(propertiesList.size() - 1).getButton()
    }

    /**
     * Sets the button with an image and event handler based on the provided category type.
     *
     * @param iv           The ImageView representing the object.
     * @param categoryType The type of the object (LIGHT, OBJECT, CAMERA).
     */
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

    /**
     * Loads an image based on the provided properties and category type, then adds it to the properties list.
     *
     * @param op           The Properties object representing the object's properties.
     * @param categoryType The type of the object (LIGHT, OBJECT, CAMERA).
     */
    private void loadImage(Properties op, Config.type categoryType) {
        Image image;
        if (categoryType == Config.type.OBJECT) {      //object
            try {
                image = new Image(System.getProperty("java.home")+"/../app/Obj_img.png");
            } catch (Exception unused) {
                try {
                    image = new Image(objectIMG.toURI().getPath().substring(1));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (categoryType == Config.type.LIGHT) {       //light
            try {
                image = new Image(System.getProperty("java.home")+"/../app/light_Img.png");
            } catch (Exception unused) {
                try {
                    image = new Image(lightObjImg.toURI().getPath().substring(1));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {    // camera
            try {
                image = new Image(System.getProperty("java.home")+"/../app/Camera_img.png");
            } catch (Exception unused) {
                try {
                    image = new Image(cameraObjImg.toURI().getPath().substring(1));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        propertiesList.add(op);
        setButton(new ImageView(image), categoryType); // setButton and call function
    }


    /*************OBJECT COORDINATES**************/

    /**
     * Updates the properties grid with the details of the selected object based on the provided content type.
     *
     * @param contentType The type of the content (LIGHT, OBJECT, CAMERA).
     */
    public void updateProperties(Config.type contentType) {
        GridPane gp = new GridPane();

        gp.setVisible(true);
        gp.addColumn(2);
        gp.addRow(13);

        gp.setPadding(new Insets(5, 10, 5, 10));
        gp.setHgap(3);
        gp.setVgap(3);

        int id = 0;

        for (int i = 0; i < propertiesList.size(); i++) {
            if (propertiesList.get(i).getId().equals(String.valueOf(lastClickedID))) {
                id = i;
            }
        }


        TextField idTF = new TextField(propertiesList.get(id).getId());
        idTF.setEditable(false);
        TextField nameTF = new TextField(propertiesList.get(id).getName());
        // textFieldVALUES[0] = nameTF.getText();
        nameTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                textFocus = false;
            } else {
                textFocus = true;
            }
        });

        gp.add(new Text("ID:"), 0, 0);
        gp.add(idTF, 1, 0);

        gp.add(new Text("Name:"), 0, 1);
        gp.add(nameTF, 1, 1);


        gp.add(new Text("Position:"), 0, 2);

        TextField xPos = new TextField(propertiesList.get(id).getPos()[0]);
        TextField yPos = new TextField(propertiesList.get(id).getPos()[1]);
        TextField zPos = new TextField(propertiesList.get(id).getPos()[2]);

        TextField xRot = new TextField(propertiesList.get(id).getRot()[0]);
        TextField yRot = new TextField(propertiesList.get(id).getRot()[1]);
        TextField zRot = new TextField(propertiesList.get(id).getRot()[2]);

        TextField scaleTF = null;
        int finalId = id;

        if (contentType == Config.type.OBJECT) {
            System.out.println("Object");
            ObjectProperties op = (ObjectProperties) propertiesList.get(id);

            scaleTF = new TextField(op.getScale()==null? "1":op.getScale());
            TextField finalScaleTF = scaleTF;
            scaleTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) { // if focus lost
                    System.out.println("Focus lost on the TextField");
                    updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, finalScaleTF);
                    textFocus = false;
                } else {
                    textFocus = true;
                }
            });
            gp.add(new Text("Scale:"), 0, 10);
            gp.add(scaleTF, 1, 10);
            // textFieldVALUES[7] = scaleTF.getText();

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

            LightProperties opTest = null;


            //workaround
            for (Properties properties : propertiesList) {
                if (properties instanceof LightProperties lightProperties) {
                    if (Objects.equals(lightProperties.getId(), lastClickedID)) opTest = lightProperties;
                }
            }

            LightProperties finalopTest = opTest;

            System.out.println("Light");
            LightProperties lp = (LightProperties) propertiesList.get(id);
            gp.addRow(2);

            gp.add(new Text("Settings"), 0, 11);
            gp.add(new Text("Color:"), 0, 12);

            ColorPicker colorPicker = new ColorPicker();
            colorPicker.setOnAction(actionEvent -> {
                Color selectedColor = colorPicker.getValue();
                double red = selectedColor.getRed();
                double green = selectedColor.getGreen();
                double blue = selectedColor.getBlue();
                System.out.println("Red: " + (int) red + ", Green: " + (int) green + ", Blue: " + (int) blue);
                if (finalopTest != null) {
                    finalopTest.setRgbR(String.valueOf(red));
                    finalopTest.setRgbG(String.valueOf(green));
                    finalopTest.setRgbB(String.valueOf(blue));
                }
            });
            gp.add(colorPicker, 1,12);

            gp.add(new Text("Intensity :"), 0, 13);
            TextField intensity = new TextField(String.valueOf(lp.getIntensity()));
            intensity.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                if (finalopTest != null)
                    finalopTest.setIntensity(newValue);
            });
            intensity.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue) { // if focus lost
                    textFocus = false;
                } else {
                    textFocus = true;
                }
            });
            gp.add(intensity, 1, 13);

            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"POINT"};
            cb.getItems().setAll(choice);

            //Known Bug: nach wechsel auf ein anderes Objekt, light verliert value.
            cb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {               //update value


                if (finalopTest == null)
                    return;

                if(Config.lightvariants.POINT.toString().equals(newValue)){
                    finalopTest.setLightvariants(Config.lightvariants.POINT);
                }
            });
            cb.setValue(lp.getLightvariants().toString());
            gp.add(new Text("Variant:"), 0, 14);
            gp.add(cb, 1, 14);

        } else if (contentType == Config.type.CAMERA) {

            CameraProperties opTest = null;

            //workaround
            for (Properties properties : propertiesList) {
                if (properties instanceof CameraProperties cameraProperties) {
                    if (Objects.equals(cameraProperties.getId(), lastClickedID))
                        opTest = cameraProperties;
                }
            }

            CameraProperties finalopTest = opTest;

            System.out.println("Camera");
            CameraProperties cp = (CameraProperties) propertiesList.get(id);
            gp.addRow(3);

            gp.add(new Text("Width:"), 0, 12);
            TextField width = new TextField(String.valueOf(cp.getWidth()));
            width.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                //CameraProperties opTest = (CameraProperties)propertiesList.get(Integer.parseInt(lastClickedID));

                if (finalopTest != null) {
                    finalopTest.setWidth(Integer.parseInt(newValue));
                    Config.getInstance().setWIDTH(Integer.parseInt(newValue));
                }
            });

            width.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    textFocus = false;
                } else {
                    textFocus = true;
                }
            });

            gp.add(width, 1, 12);

            gp.add(new Text("Height:"), 0, 13);
            TextField height = new TextField(String.valueOf(cp.getHeight()));


            height.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                // CameraProperties opTest = (CameraProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                if (finalopTest != null) {
                    finalopTest.setHeight(Integer.parseInt(newValue));
                    Config.getInstance().setHEIGHT(Integer.parseInt(newValue));
                }

            });

            height.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    textFocus = false;
                } else {
                    textFocus = true;
                }
            });

            gp.add(height, 1, 13);

            width.setText("1280");
            height.setText("720");

            /*
            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"HD"};
            height.setEditable(false);
            width.setEditable(false);
            cb.getItems().setAll(choice);
            cb.setValue(choice[0]);

            cb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {             //update value



                if (finalopTest == null)
                    return;

                if(Config.cameravariants.HD.toString().equals(newValue)){
                    finalopTest.setCameravariants(Config.cameravariants.HD);
                    width.setEditable(false);
                    height.setEditable(false);
                    System.out.println("HD");
                }
            });
            cb.setValue(cp.getCameravariants().toString());
            */

            gp.add(new Text("Camera Resolution"), 0, 11);
            //gp.add(cb, 1, 11);

            // textFieldVALUES[7] = String.valueOf(cp.getHeight());
            // textFieldVALUES[8] = String.valueOf(cp.getWidth());
            // textFieldVALUES[9] = cb.getValue();
        }

        TextField fScale = scaleTF;
        xPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println(xPos.getText());
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, fScale);
                textFocus = false;
            } else {
                textFocus = true;
            }
        });
        yPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, fScale);
                textFocus = false;
            } else {
                textFocus = true;
            }
        });
        zPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, fScale);
                textFocus = false;
            } else {
                textFocus = true;
            }
        });

        xRot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, fScale);
                textFocus = false;
            } else {
                textFocus = true;
            }
        });

        yRot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, fScale);
                textFocus = false;
            } else {
                textFocus = true;
            }
        });

        zRot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, fScale);
                textFocus = false;
            } else {
                textFocus = true;
            }
        });

        numericOnly(xPos);
        numericOnly(yPos);
        numericOnly(zPos);
        // textFieldVALUES[1] = xPos.getText();
        // textFieldVALUES[2] = yPos.getText();
        // textFieldVALUES[3] = zPos.getText();


        gp.add(new Text("X:"), 0, 3);
        gp.add(xPos, 1, 3);
        gp.add(new Text("Y:"), 0, 4);
        gp.add(yPos, 1, 4);
        gp.add(new Text("Z:"), 0, 5);
        gp.add(zPos, 1, 5);

        gp.add(new Text("Rotation:"), 0, 6);

        numericOnly(xRot);
        numericOnly(yRot);
        numericOnly(zRot);
        // textFieldVALUES[4] = xRot.getText();
        // textFieldVALUES[5] = yRot.getText();
        // textFieldVALUES[6] = zRot.getText();


        gp.add(new Text("X:"), 0, 7);
        gp.add(xRot, 1, 7);
        gp.add(new Text("Y:"), 0, 8);
        gp.add(yRot, 1, 8);
        gp.add(new Text("Z:"), 0, 9);
        gp.add(zRot, 1, 9);


        scrollPaneProperties.setContent(gp);
        //updateModelSettings(contentType, id, // textFieldVALUES);
    }

    /**
     * Updates the data of the selected object based on the provided input fields.
     *
     * @param id      The ID of the selected object.
     * @param xPos    The TextField for X-position.
     * @param yPos    The TextField for Y-position.
     * @param zPos    The TextField for Z-position.
     * @param xRot    The TextField for X-rotation.
     * @param yRot    The TextField for Y-rotation.
     * @param zRot    The TextField for Z-rotation.
     * @param scaleTF The TextField for scaling (only for ObjectProperties).
     */
    private void updateData(int id, TextField xPos, TextField yPos, TextField zPos, TextField xRot, TextField yRot, TextField zRot, TextField scaleTF) {
        if (propertiesList.get(id) instanceof ObjectProperties objectProperties && scaleTF != null) {
            objectProperties.setScale(scaleTF.getText());
            previewWindow.updatePosition(
                    Integer.parseInt(lastClickedID),
                    Double.parseDouble(xPos.getText()),
                    Double.parseDouble(yPos.getText()),
                    Double.parseDouble(zPos.getText()),
                    Double.parseDouble(xRot.getText()),
                    Double.parseDouble(yRot.getText()),
                    Double.parseDouble(zRot.getText()),
                    Double.parseDouble(scaleTF.getText())
            );
        } else {
            // Update position without scaling (or for non-ObjectProperties)
            previewWindow.updatePosition(
                    Integer.parseInt(lastClickedID),
                    Double.parseDouble(xPos.getText()),
                    Double.parseDouble(yPos.getText()),
                    Double.parseDouble(zPos.getText()),
                    Double.parseDouble(xRot.getText()),
                    Double.parseDouble(yRot.getText()),
                    Double.parseDouble(zRot.getText()),
                    1
            );
        }
        // Update the position and rotation in the properties list
        propertiesList.get(id).setPos(new String[]{xPos.getText(), yPos.getText(), zPos.getText()});
        propertiesList.get(id).setRot(new String[]{xRot.getText(), yRot.getText(), zRot.getText()});
    }

    /**
     * Restricts the input of the provided TextField to numeric values only.
     *
     * @param field The TextField to restrict to numeric values.
     */
    private void numericOnly(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d*)?")) field.setText(newValue.replaceAll("[^\\d.]", ""));
        });
    }

    /**
     * Sets the application theme to light mode.
     */
    private void setLightMode() {
        removeStylesheet("DarkMode.css");
        addStylesheet("LightMode.css");
    }

    /**
     * Sets the application theme to dark mode.
     */
    private void setDarkMode() {
        removeStylesheet("LightMode.css");
        addStylesheet("DarkMode.css");
    }

    /**
     * Adds a stylesheet to the application's stylesheets list.
     *
     * @param stylesheet The name of the stylesheet to add.
     */
    private void addStylesheet(String stylesheet) {
        String stylesheetPath = Objects.requireNonNull(getClass().getResource("/com/softpro/dnaig/style/" + stylesheet)).toExternalForm();
        parent.getStylesheets().add(stylesheetPath);
    }

    /**
     * Removes a stylesheet from the application's stylesheets list.
     *
     * @param stylesheet The name of the stylesheet to remove.
     */
    private void removeStylesheet(String stylesheet) {
        String stylesheetPath = Objects.requireNonNull(getClass().getResource("/com/softpro/dnaig/style/" + stylesheet)).toExternalForm();
        parent.getStylesheets().remove(stylesheetPath);
    }

    /*************Render Functions**************/
    /**
     * Handles the rendering functionality, including starting or canceling the ray tracer.
     *
     * @param event The ActionEvent triggering the rendering.
     */
    @FXML
    void renderFunc(ActionEvent event) {
        System.out.println(camExist);

        // Check if a camera is available
        if (!camExist) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Camera input detected! \nPlease add a Camera beforehand", ButtonType.CANCEL);
            alert.setTitle("No Camera Detected!");
            alert.showAndWait();
        } else {
            // Switch the style class and execute rendering or cancellation accordingly
            if ((renderButton.getText().equals("Cancel"))) {
                cancelRayTracer();
            } else {
                loadRayTracer();
            }
        }
    }

    /**
     * Switches the style class of the render button based on its current state.
     */
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

    /**
     * Cancels the ongoing ray tracer process.
     */
    void cancelRayTracer() {
        // Code to stop the RayTracer...
        System.out.println("Getting Canceled!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //output.cancelRayTracer(); // Uncomment and replace with actual method call if applicable
        Output.getOutput().cancelRayTracer();
        switchRenderButtonStyleClass();
    }

    /**
     * Sets the stage for the controller.
     *
     * @param stage The JavaFX stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Callback method invoked when the ray tracer finishes rendering.
     *
     * @param unused Unused parameter.
     */
    public void callbackWhenRayTracerFinished(Void unused) {
        switchRenderButtonStyleClass();
    }

    /**
     * Loads the Ray Tracer with the current scene configuration and starts the rendering process.
     */
    @FXML
    void loadRayTracer() {
        Output output = Output.getOutput().recreate();
        output.clear();
        entityList.forEach(System.out::println);
        Camera camera = null;

        // Iterate through the properties list to configure lights, objects, and camera
        for (Properties properties : propertiesList) {
            if (properties instanceof LightProperties lightProperties) {
                // Configure PointLight based on LightProperties

                PointLight pointLight = new PointLight(
                        Integer.parseInt(lightProperties.getId()),
                        new Vector3D(Double.parseDouble(lightProperties.getPos()[0]),
                                Double.parseDouble(lightProperties.getPos()[1]),
                                Double.parseDouble(lightProperties.getPos()[2])),
                        new Vector3D(Double.parseDouble(lightProperties.getRgb()[0]),
                                Double.parseDouble(lightProperties.getRgb()[1]),
                                Double.parseDouble(lightProperties.getRgb()[2])),
                        Double.parseDouble(lightProperties.getIntensity())

                );

                // Remove and update existing light in the lightList
                lightList.removeIf(light -> light.getID() == pointLight.getID());
                lightList.add(pointLight);
            } else if (properties instanceof ObjectProperties objectProperties) {
                // Configure Entity based on ObjectProperties
                Entity entity = null;
                for (Entity entity1 : entityList) {
                    if (entity1.getID() == Integer.parseInt(objectProperties.getId())) {
                        entity = entity1;
                        entity.setPivot(new Vector3D(Double.parseDouble(objectProperties.getPos()[0]),
                                Double.parseDouble(objectProperties.getPos()[1]),
                                Double.parseDouble(objectProperties.getPos()[2])));
                        entity.setOrient(new Vector3D(Double.parseDouble(objectProperties.getRot()[0]),
                                Double.parseDouble(objectProperties.getRot()[1]),
                                Double.parseDouble(objectProperties.getRot()[2])));
                        entity.scale(Double.parseDouble(objectProperties.getScale() == null ? "1" : objectProperties.getScale()));
                        objectProperties.setRot(new String[]{"0", "0", "0"});
                        objectProperties.setScale(objectProperties.getScale() == null ? "1" : objectProperties.getScale());
                        System.out.println("Orientation: " + entity.getOrient());
                        break;
                    }
                }

                // Remove and update existing entity in the entityList
                if (entity != null) {
                    Entity finalEntity = entity;
                    entityList.removeIf(entity1 -> entity1.getID() == finalEntity.getID());
                    entityList.add(entity);
                }
            } else if (properties instanceof CameraProperties cameraProperties) {
                // Configure Camera based on CameraProperties
                Vector3D pos = new Vector3D(Double.parseDouble(cameraProperties.getPos()[0]),
                        Double.parseDouble(cameraProperties.getPos()[1]),
                        Double.parseDouble(cameraProperties.getPos()[2]));
                Vector3D rot = new Vector3D(Double.parseDouble(cameraProperties.getRot()[0]),
                        Double.parseDouble(cameraProperties.getRot()[1]),
                        Double.parseDouble(cameraProperties.getRot()[2]));
                camera = new Camera(pos, rot, cameraProperties.getWidth(), cameraProperties.getHeight());
            }
        }

        System.out.println(entityList.size());
        System.out.println(lightList.size());

        // Set the scene and start the ray tracer
        output.setScene(entityList, lightList, camera);
        output.openRayTracer(propertiesList, stage, this::callbackWhenRayTracerFinished, this);
    }

    /*************One Time METHODS**************/

    /**
     * Deletes an object or light based on its ID.
     *
     * @param objID The ID of the object or light to be deleted.
     */
    public void deleteObject(int objID) {
        System.out.println(objID);
        System.out.println(propertiesList.size());
        scrollPaneProperties.setContent(null);

        // Iterate through propertiesList to find and delete the object or light
        for (Properties properties : propertiesList) {
            System.out.println("test");
            if (properties instanceof LightProperties lightProperties) {
                // Delete light if found
                if (properties.getId().equals(String.valueOf(objID))) {
                    System.out.println("Test");
                    objectListView.getItems().remove(properties.getButton());
                    propertiesList.remove(properties);
                    PointLight pointLight = null;

                    // Find the corresponding light in the lightList
                    for (Light light : lightList) {
                        if (light.getID() == objID) {
                            System.out.println("DELETE LIGHT");
                            pointLight = (PointLight) light;
                            break;
                        }
                    }

                    // Remove the light from the lightList
                    if (pointLight != null) lightList.remove(pointLight);
                    return;
                }
            } else if (properties instanceof ObjectProperties objectProperties) {
                // Delete object if found
                System.out.println("DELETE OBJECT");
                if (properties.getId().equals(String.valueOf(objID))) {
                    objectListView.getItems().remove(properties.getButton());
                    propertiesList.remove(properties);
                    Entity entity = null;

                    // Find the corresponding entity in the entityList
                    for (Entity entity1 : entityList) {
                        if (entity1.getID() == Integer.parseInt(objectProperties.getId())) {
                            entity = entity1;
                            break;
                        }
                    }

                    // Remove the entity from the entityList
                    if (entity != null) entityList.remove(entity);
                    return;
                }
            } else if (properties instanceof CameraProperties cameraProperties) {
                camExist = false;

                // Delete object if found
                System.out.println("DELETE OBJECT");
                if (properties.getId().equals(String.valueOf(objID))) {
                    objectListView.getItems().remove(properties.getButton());
                    propertiesList.remove(properties);
                    Entity entity = null;

                    // Find the corresponding entity in the entityList
                    for (Entity entity1 : entityList) {
                        if (entity1.getID() == Integer.parseInt(cameraProperties.getId())) {
                            entity = entity1;
                            break;
                        }
                    }

                    // Remove the entity from the entityList
                    if (entity != null) entityList.remove(entity);
                    return;
                }
            } else {
                System.out.println(properties.getName());
            }
        }
    }

    /**
     * Opens a FileChooser to select an OBJ file and returns the selected file.
     *
     * @return The selected OBJ file.
     */
    File fileChooser() {
        fileChooser.setTitle("Open .obj File");
        String fileString;

        latestFile = fileChooser.showOpenDialog(new Stage());

        if (latestFile != null) {
            fileString = String.valueOf(latestFile.getParentFile());
            fileChooser.setInitialDirectory(new File(fileString));
        }

        return latestFile;
    }

    /**
     * Sets the ID of the last clicked object or light.
     *
     * @param s The ID to be set.
     */
    public void setLastClickedID(String s) {
        this.lastClickedID = s;
    }

    /**
     * Initializes the controller. Called after the FXML file has been loaded.
     */
    public void initialize() {
        previewWindow = new PreviewWindow(previewPane, this);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ Files", "*.obj"));
    }

    /**
     * Handles key events.
     *
     * @param event The key event to be handled.
     */
    public void handleKey(KeyEvent event) {
        if (previewWindow != null && !textFocus) previewWindow.handleKey(event);
    }

    /**
     * Displays an about section with information about the application.
     *
     * @param event The action event that triggers the about section.
     */
    @FXML
    void aboutSection(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE, "A Java-Based Raytracer with an In-Build 3D-Model viewer and editor, easy to use for beginner and experienced " +
                "or even the visibly impaired (coming soon!).", ButtonType.CANCEL);
        alert.setTitle("About");
        alert.showAndWait();

        System.out.println("3D-Modelling Raytracer, created as a school project");
    }

}
