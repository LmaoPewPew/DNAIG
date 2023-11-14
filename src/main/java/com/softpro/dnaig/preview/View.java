package com.softpro.dnaig.preview;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz3d.importers.Importer3D;
import org.fxyz3d.importers.Model3D;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class View {

    Model3D model;
    Camera camera;
    Group group;
    SubScene subScene;

    public View(Stage stage) {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(50000.0);
        Rotate rz = new Rotate(180.0, Rotate.Z_AXIS);
        camera.getTransforms().add(rz);
        camera.setTranslateX(600);
        camera.setTranslateY(400);
        camera.getTransforms().add(new Translate(0, 0, -300));

        group = new Group();

        subScene = new SubScene(group, 1280, 720, true, SceneAntialiasing.BALANCED);
        subScene.widthProperty().bind(stage.getScene().widthProperty());
        subScene.heightProperty().bind(stage.getScene().heightProperty());

        subScene.setFill(Color.rgb(80, 80, 80));
        subScene.setCamera(camera);

        //model.getRoot().translateXProperty().bind(subScene.widthProperty().divide(2));
        //model.getRoot().translateYProperty().bind(subScene.heightProperty().divide(2));
    }

    public Model3D addObject(String path) throws IOException {
        model = Importer3D.load(new File(path).toURI().toURL());
        group.getChildren().add(model.getRoot());

        return model;
    }

    public Model3D getModel() {
        return model;
    }

    public Camera getCamera() {
        return camera;
    }

    public SubScene getSubScene() {
        return subScene;
    }
}
