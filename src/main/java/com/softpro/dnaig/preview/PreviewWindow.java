package com.softpro.dnaig.preview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz3d.importers.Model3D;

import java.io.IOException;

public class PreviewWindow extends Application {

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private View view;
    private Overlay overlay;
    private CameraController cameraController;

    @Override
    public void start(Stage stage) {
        stage.setTitle("3D Cube Projection in JavaFX");
        StackPane root = new StackPane();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.rgb(30,30,30));

        stage.setScene(scene);

        view = new View(stage);
        overlay = new Overlay();

        root.getChildren().addAll(view.getSubScene(), overlay);

        cameraController = new CameraController(this, stage, view, overlay);

        addObject("ObjFiles/porsche/porsche.obj");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void addObject(String path) {
        try {
            Model3D model = view.addObject(path);
            Rotate rX = new Rotate(0, Rotate.X_AXIS);
            Rotate rY = new Rotate(0, Rotate.Y_AXIS);
            Translate t = new Translate();

            cameraController.getModelCameraMap().put(
                    model,
                    new CameraControlWrapper(
                        rX,
                        rY,
                        t
            ));

            cameraController.setSelected(model);

            model.getRoot().getTransforms().addAll(rX, rY);
            model.getRoot().getParent().getTransforms().add(t);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}