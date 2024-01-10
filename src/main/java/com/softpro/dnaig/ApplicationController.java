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
    private MenuItem mSaveAs;
    @FXML
    private RadioMenuItem mTheme;
    @FXML
    private MenuItem mAbout;
    @FXML
    private RadioMenuItem mVoice;

    //Rest
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

    public ApplicationController() {
    }


    /* *****************************************METHODS***************************************** */


    /*************BUTTON CLICK ACTION METHODS**************/

    @FXML
    void importLightObject(MouseEvent event) throws IOException {
        int id = objectID;
        createGUIObject(null, id, Config.type.LIGHT);
        previewWindow.addObject("src/main/java/com/softpro/dnaig/assets/objFile/lightbulb/lightbulb.obj", id);

        objectID++;
    }

    @FXML
    void importCameraObject(MouseEvent event) throws IOException {
        camExist = true;
        System.out.println(camExist);
        int id = objectID++;
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

    private final FileChooser directoryChooser = new FileChooser();

    @FXML
    void saveFile(ActionEvent event) {
        // Automatically Save .YAML file in Downloads folder
        //FileChooser directoryChooser = new FileChooser();

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

    @FXML
    void saveFileAs(ActionEvent event) {
        // Save .YAML file in specific folder (Downloads will open first tho)
    }

    @FXML
    void exportYaml(ActionEvent event) {
        // Save Image of the Rendered image in specific Folder (like SaveFileAs)
        //FileChooser directoryChooser = new FileChooser();

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

        directoryChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("YAML", "*.yaml"));
        directoryChooser.setTitle("Save Scene");

        File file = directoryChooser.showOpenDialog(new Stage());

        try {
            System.out.println(file.getPath());
            //CustomScene.getScene().yamlImport(file);
            YAMLexporter.importScene(file, entityList, lightList, null);
            for (Entity entity : entityList) {
                int id = objectID++;
                latestFile = new File(entity.getObjPath());
                entity.setId(id);
                createGUIObject(entity, id, Config.type.OBJECT);
            }
            for (Light light : lightList) {
                int id = objectID++;
                light.setID(id);
                System.out.println(light.toYaml());
                LightProperties lp = new LightProperties(Config.type.LIGHT, Config.lightvariants.POINT, String.format("%f, %f, %f", light.getIntensity().getX(), light.getIntensity().getY(), light.getIntensity().getZ()), "light", String.valueOf(id), new String[]{String.valueOf(light.getPosition().getX()), String.valueOf(light.getPosition().getY()), String.valueOf(light.getPosition().getZ())}, new String[]{"0","0","0"}, new String[]{"0","0","0"});
                loadImage(lp, Config.type.LIGHT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EDIT
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

    //HELP

    /*************OBJECTS**************/



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
            lp = new LightProperties(categoryType, Config.lightvariants.POINT, "1", "light", String.valueOf(id), new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, new String[]{"1", "1", "1"});
            loadImage(lp, categoryType);
        } else if (categoryType == Config.type.OBJECT) {          //load object properties
            String objFileName = latestFile.getName().substring(0, latestFile.getName().lastIndexOf('.'));
            e.setObjName(objFileName);

            op = new ObjectProperties(categoryType, e, String.valueOf(id), e.getObjName(), Integer.toString(e.getFaces().size()), Integer.toString(e.getVertexCount()), new String[]{Double.toString(e.getPivot().getX()), Double.toString(e.getPivot().getY()), Double.toString(e.getPivot().getZ())}, new String[]{Double.toString(e.getOrient().getX()), Double.toString(e.getOrient().getY()), Double.toString(e.getOrient().getZ())});
            loadImage(op, categoryType);
        } else {          //load camera properties
            cp = new CameraProperties(categoryType, Config.cameravariants.HD, "camera", String.valueOf(id), new String[]{"0", "0", "0"}, new String[]{"0", "0", "0"}, 1920, 1080);
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

        TextField xRot = new TextField(propertiesList.get(id).getRot()[0]);
        TextField yRot = new TextField(propertiesList.get(id).getRot()[1]);
        TextField zRot = new TextField(propertiesList.get(id).getRot()[2]);

        int finalId = id;
        xPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println(xPos.getText());
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, null);
            }
        });
        yPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, null);
            }
        });
        zPos.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost

                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, null);
            }
        });

        xRot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, null);
            }
        });

        yRot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, null);
            }
        });

        zRot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // if focus lost
                System.out.println("Focus lost on the TextField");
                updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, null);
            }
        });

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
            scaleTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) { // if focus lost
                    System.out.println("Focus lost on the TextField");
                    updateData(finalId, xPos, yPos, zPos, xRot, yRot, zRot, scaleTF);
                }
            });
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

            gp.add(new Text("R:"), 0, 12);
            TextField r = new TextField(String.valueOf(lp.getRgb()[0]));
            r.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                LightProperties opTest = (LightProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                opTest.setRgbR(newValue);
            });
            gp.add(r, 1, 12);

            gp.add(new Text("G:"), 0, 13);
            TextField g = new TextField(String.valueOf(lp.getRgb()[1]));
            g.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                LightProperties opTest = (LightProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                opTest.setRgbG(newValue);
            });
            gp.add(g, 1, 13);

            gp.add(new Text("B:"), 0, 14);
            TextField b = new TextField(String.valueOf(lp.getRgb()[2]));
            b.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                LightProperties opTest = (LightProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                opTest.setRgbB(newValue);
            });
            gp.add(b, 1, 14);

            gp.add(new Text("Intensity :"), 0, 15);
            TextField intensity = new TextField(String.valueOf(lp.getIntensity()));
            intensity.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                LightProperties opTest = (LightProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                opTest.setIntensity(newValue);
            });
            gp.add(intensity, 1, 15);

            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"POINT", "SPOT", "SUN", "AREA"};
            cb.getItems().setAll(choice);

            //Known Bug: nach wechsel auf ein anderes Objekt, light verliert value.
            cb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {               //update value
                LightProperties opTest = null;

                //workaround
                for (Properties properties : propertiesList) {
                    if (properties instanceof LightProperties lightProperties) {
                        if (Objects.equals(lightProperties.getId(), lastClickedID))
                            opTest = lightProperties;
                    }
                }

                if (opTest == null)
                    return;

                if(Config.lightvariants.POINT.toString().equals(newValue)){
                    opTest.setLightvariants(Config.lightvariants.POINT);
                }
                if(Config.lightvariants.SPOT.toString().equals(newValue)){
                    opTest.setLightvariants(Config.lightvariants.SPOT);
                }
                if(Config.lightvariants.SUN.toString().equals(newValue)){
                    opTest.setLightvariants(Config.lightvariants.SUN);
                }
                if(Config.lightvariants.AREA.toString().equals(newValue)){
                    opTest.setLightvariants(Config.lightvariants.AREA);
                }
            });
            cb.setValue(lp.getLightvariants().toString());
            gp.add(new Text("Variant:"), 0, 16);
            gp.add(cb, 1, 16);

            textFieldVALUES[7] = String.valueOf(lp.getIntensity());
            textFieldVALUES[8] = cb.getValue();

            //TODO COLOR PICKER:
            //https://docs.oracle.com/javafx/2/ui_controls/color-picker.htm#:~:text=Use%20the%20ColorPicker%20class%20of,to%20declare%20a%20ColorPicker%20object.

        } else if (contentType == Config.type.CAMERA) {
            System.out.println("Camera");
            CameraProperties cp = (CameraProperties) propertiesList.get(id);
            gp.addRow(3);

            gp.add(new Text("Length:"), 0, 11);
            TextField length = new TextField(String.valueOf(cp.getHeight()));
            length.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                CameraProperties opTest = (CameraProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                opTest.setHeight(Integer.parseInt(newValue));
            });
            gp.add(length, 1, 12);

            gp.add(new Text("Width:"), 0, 12);
            TextField width = new TextField(String.valueOf(cp.getWidth()));
            width.textProperty().addListener((observable, oldValue, newValue) -> {               //update value
                CameraProperties opTest = (CameraProperties)propertiesList.get(Integer.parseInt(lastClickedID));
                opTest.setWidth(Integer.parseInt(newValue));
            });
            gp.add(width, 1, 13);

            ChoiceBox<String> cb = new ChoiceBox<>();
            String[] choice = {"HD", "FullHD", "Custom"};

            cb.getItems().setAll(choice);
            cb.setValue(choice[1]);

            cb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {             //update value
                CameraProperties opTest = null;

                //workaround
                for (Properties properties : propertiesList) {
                    if (properties instanceof CameraProperties cameraProperties) {
                        if (Objects.equals(cameraProperties.getId(), lastClickedID))
                            opTest = cameraProperties;
                    }
                }

                if (opTest == null)
                    return;

                if(Config.cameravariants.HD.toString().equals(newValue)){
                    opTest.setCameravariants(Config.cameravariants.HD);
                    length.setEditable(false);
                    width.setEditable(false);
                    length.setText("1080");
                    width.setText("720");
                    System.out.println("HD");
                }
                if(Config.cameravariants.FullHD.toString().equals(newValue)){
                    opTest.setCameravariants(Config.cameravariants.FullHD);
                    length.setEditable(false);
                    width.setEditable(false);
                    length.setText("1920");
                    width.setText("1080");

                    System.out.println("FullHD");
                }
                if(Config.cameravariants.Custom.toString().equals(newValue)){
                    opTest.setCameravariants(Config.cameravariants.Custom);
                    length.setEditable(true);
                    width.setEditable(true);

                    //Textfiller 4k
                    length.setText("3840");
                    width.setText("2160");

                    System.out.println("Custom");
                }
            });

            cb.setValue(cp.getCameravariants().toString());
            gp.add(new Text("Variants:"), 0, 13);
            gp.add(cb, 1, 11);

            textFieldVALUES[7] = String.valueOf(cp.getHeight());
            textFieldVALUES[8] = String.valueOf(cp.getWidth());
            textFieldVALUES[9] = cb.getValue();
        }
        scrollPaneProperties.setContent(gp);
        //updateModelSettings(contentType, id, textFieldVALUES);
    }

    private void updateData(int id, TextField xPos, TextField yPos, TextField zPos, TextField xRot, TextField yRot, TextField zRot, TextField scaleTF) {
        previewWindow.updatePosition(
                Integer.parseInt(lastClickedID),
                Double.parseDouble(xPos.getText()),
                Double.parseDouble(yPos.getText()),
                Double.parseDouble(zPos.getText()),
                Double.parseDouble(xRot.getText()),
                Double.parseDouble(yRot.getText()),
                Double.parseDouble(zRot.getText())
        );
        propertiesList.get(id).setPos(new String[]{xPos.getText(), yPos.getText(), zPos.getText()});
        propertiesList.get(id).setRot(new String[]{xRot.getText(), yRot.getText(), zRot.getText()});
        if(propertiesList.get(id) instanceof ObjectProperties objectProperties && scaleTF != null) {
            objectProperties.setScale(scaleTF.getText());
        }
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

            properties.setIntensity(values[7]);
            properties.setLightvariants(Config.lightvariants.valueOf(values[8]));
        } else if (contentType == Config.type.CAMERA) {
            CameraProperties properties = (CameraProperties) propertiesList.get(id);

            properties.setHeight(Integer.parseInt(values[7]));
            properties.setWidth(Integer.parseInt(values[8]));
            //properties.setLightvariants(Config.cameravariants.valueOf(values[9]));

        }
    }

    private void numericOnly(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d*)?")) field.setText(newValue.replaceAll("[^\\d.]", ""));
        });
    }

    private void numericOnlyRes(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) field.setText(newValue.replaceAll("[^\\d.]", ""));
        });
    }


    // mehrere - überall, egal wo.
/*  private void numericOnly(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*")) field.setText(newValue.replaceAll("[^\\d.-]", ""));
        });
    }
*/

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
        System.out.println(camExist);
        if (!camExist) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Camera input detected! \nPlease add a Camera beforehand", ButtonType.CANCEL);
            alert.setTitle("No Camera Detected!");
            alert.showAndWait();
        } else {
            switchRenderButtonStyleClass();
            if ((renderButton.getText().equals("Cancel"))) {
                loadRayTracer();
            } else cancelRayTracer();
        }
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

    @FXML
    void loadRayTracer() {
        Output output = Output.getOutput();
        output.clear();
        entityList.forEach(System.out::println);
        Camera camera = null;
        //entityList.forEach(entity -> previewWindow.getEntityData(entity));

        for (Properties properties : propertiesList) {
            if (properties instanceof LightProperties lightProperties) {
                PointLight pointLight = null;
                //System.out.println(pointLight.getPosition());
                Vector3D pos = new Vector3D(Double.parseDouble(lightProperties.getPos()[0]), Double.parseDouble(lightProperties.getPos()[1]), Double.parseDouble(lightProperties.getPos()[2]));
                Vector3D brightness = new Vector3D(Double.parseDouble(lightProperties.getIntensity()), Double.parseDouble(lightProperties.getIntensity()), Double.parseDouble(lightProperties.getIntensity()));
                //Vector3D brightness = new Vector3D(3.0,4.0,1.0);
                pointLight = new PointLight(Integer.parseInt(lightProperties.getId()), pos, brightness);
                //pointLight = new PointLight(pointLight.getID(), new Vector3D(Double.parseDouble(lightProperties.getPos()[0]), Double.parseDouble(lightProperties.getPos()[1]), Double.parseDouble(lightProperties.getPos()[2])), new Vector3D(Double.parseDouble(lightProperties.getBrightness().split(", ")[0]), Double.parseDouble(lightProperties.getBrightness().split(", ")[1]), Double.parseDouble(lightProperties.getBrightness().split(", ")[2])));
                PointLight finalPointLight = pointLight;
                lightList.removeIf(light -> light.getID() == finalPointLight.getID());
                lightList.add(pointLight);
            } else if (properties instanceof ObjectProperties objectProperties) {
                Entity entity = null;
                for (Entity entity1 : entityList) {
                    if (entity1.getID() == Integer.parseInt(objectProperties.getId())) {
                        entity = entity1;
                        entity.setPivot(new Vector3D(Double.parseDouble(objectProperties.getPos()[0]), Double.parseDouble(objectProperties.getPos()[1]), Double.parseDouble(objectProperties.getPos()[2])));
                        entity.setOrient(new Vector3D(Double.parseDouble(objectProperties.getRot()[0]), Double.parseDouble(objectProperties.getRot()[1]), Double.parseDouble(objectProperties.getRot()[2])));
                        entity.scale(Double.parseDouble(objectProperties.getScale() == null ? "1" : objectProperties.getScale()));
                        objectProperties.setRot(new String[]{"0","0","0"});
                        objectProperties.setScale("1");
                        System.out.println("Orientation: " + entity.getOrient());
                        break;
                    }
                }
                if (entity != null) {
                    Entity finalEntity = entity;
                    entityList.removeIf(entity1 -> entity1.getID() == finalEntity.getID());
                    entityList.add(entity);
                }
            } else if(properties instanceof CameraProperties cameraProperties) {
                Vector3D pos = new Vector3D(Double.parseDouble(cameraProperties.getPos()[0]), Double.parseDouble(cameraProperties.getPos()[1]), Double.parseDouble(cameraProperties.getPos()[2]));
                Vector3D rot = new Vector3D(Double.parseDouble(cameraProperties.getRot()[0]), Double.parseDouble(cameraProperties.getRot()[1]), Double.parseDouble(cameraProperties.getRot()[2]));
                camera = new Camera(pos, rot, cameraProperties.getWidth(), cameraProperties.getHeight());

            }
        }


        // previewWindow.getEntityData(entityList.get(0));
        System.out.println(entityList.size());
        System.out.println(lightList.size());
        output.setScene(entityList, lightList, camera);
        output.openRayTracer(propertiesList, stage, this::callbackWhenRayTracerFinished);
    }


    /*************Voice Assistant**************/

    @FXML
    void voiceAssistant(ActionEvent event) {
       boolean isSelected = mVoice.isSelected();
        System.out.println(isSelected);

        //So bald Feature geht, muss alles weg damit.
        Alert alert = new Alert(Alert.AlertType.ERROR, "THIS FEATURE IS STILL WORK IN PROGRESS, FUTURE UPDATES WILL BE RELEASED SOON!", ButtonType.CANCEL);
        alert.setTitle("WIP");
        alert.showAndWait();
        mVoice.setSelected(false);
        ////////////////////////////////////////
    }

    /*************One Time METHODS**************/

    // Location might change once finished (?)
    public void deleteObject(int objID) {
        System.out.println(objID);
        System.out.println(propertiesList.size());
        for (Properties properties : propertiesList) {
            System.out.println("test");
            if(properties instanceof LightProperties lightProperties) {
                if (properties.getId().equals(String.valueOf(objID))) {
                    System.out.println("Test");
                    objectListView.getItems().remove(properties.getButton());
                    propertiesList.remove(properties);
                    PointLight pointLight = null;
                    for (Light light : lightList) {
                        if (light.getID() == objID) {
                            System.out.println("DELETE LIGHT");
                            pointLight = (PointLight) light;
                            break;
                        }
                    }
                    if (pointLight != null) lightList.remove(pointLight);
                    return;
                }
            }else if(properties instanceof ObjectProperties objectProperties) {
                System.out.println("DELETE OBJECT");
                if (properties.getId().equals(String.valueOf(objID))) {
                    objectListView.getItems().remove(properties.getButton());
                    propertiesList.remove(properties);
                    Entity entity = null;
                    for (Entity entity1 : entityList) {
                        if (entity1.getID() == Integer.parseInt(objectProperties.getId())) {
                            entity = entity1;
                            break;
                        }
                    }
                    if (entity != null) entityList.remove(entity);
                    return;
                }
            }
            else System.out.println(properties.getName());
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

    @FXML
    void aboutSection(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE, "A Java-Based Raytracer with an In-Build 3D-Model viewer and editor, easy to use for beginner and experienced " + "or even the visibly impaired (coming soon!).", ButtonType.CANCEL);
        alert.setTitle("About");
        alert.showAndWait();

        System.out.println("3D-Modelling Raytracer, created as a school project");
    }
}
