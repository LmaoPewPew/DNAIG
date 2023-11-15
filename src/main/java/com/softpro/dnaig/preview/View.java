package com.softpro.dnaig.preview;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Importer3D;
import org.fxyz3d.importers.Model3D;

import java.io.File;
import java.io.IOException;

public class View {

    Model3D model;
    Camera camera;
    Group group;
    SubScene subScene;

    public View() {
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

        subScene.setFill(Color.rgb(80, 80, 80));
        subScene.setCamera(camera);
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

    public void removeObject(Model3D model) {
        group.getChildren().remove(model.getRoot());
    }
}