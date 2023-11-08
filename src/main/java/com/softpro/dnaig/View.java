package com.softpro.dnaig;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.fxyz3d.importers.Importer3D;
import org.fxyz3d.importers.Model3D;

import java.io.File;
import java.io.IOException;

public class View {

    Model3D model;
    Camera camera;
    SubScene subScene;

    View(Stage stage) {

        try {
            model = Importer3D.load(new File("C:/Users/Dominik/OneDrive/Studium/23-24/Software Projekt/Beispielmodelle/cars/porsche/porsche.obj").toURI().toURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(50000.0);
        Rotate rz = new Rotate(180.0, Rotate.Z_AXIS);
        camera.getTransforms().add(rz);
        camera.setTranslateX(600);
        camera.setTranslateY(400);
        camera.translateZProperty().set(-300.0);
        //camera.setRotate(0);
        //camera.setFieldOfView(20);

        /*CameraTransformer cameraTransformer = new CameraTransformer();
        cameraTransformer.getChildren().add(camera);
        cameraTransformer.ry.setAngle(-30.0);
        cameraTransformer.rx.setAngle(-15.0);*/

        /*SpringMesh spring = new SpringMesh(10, 2, 2, 8 * 2 * Math.PI, 200, 100, 0, 0);
        spring.setCullFace(CullFace.NONE);
        spring.setTextureModeVertices3D(1530, p -> p.f);*/

        Group group = new Group(/*cameraTransformer, */model.getRoot()/*, spring*/);

        subScene = new SubScene(group, 1280, 720, true, SceneAntialiasing.BALANCED);
        subScene.widthProperty().bind(stage.getScene().widthProperty());
        subScene.heightProperty().bind(stage.getScene().heightProperty());

        subScene.setFill(Color.rgb(80, 80, 80));
        subScene.setCamera(camera);

        model.getRoot().translateXProperty().bind(subScene.widthProperty().divide(2));
        model.getRoot().translateYProperty().bind(subScene.heightProperty().divide(2));
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
