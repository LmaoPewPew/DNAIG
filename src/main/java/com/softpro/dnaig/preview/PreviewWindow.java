package com.softpro.dnaig.preview;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Model3D;

import java.io.IOException;

public class PreviewWindow {
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private View view;
    private Overlay overlay;
    private CameraController cameraController;
    public StackPane root;

    public PreviewWindow(StackPane root) {
        this.root = root;

        view = new View();
        overlay = new Overlay();
        cameraController = new CameraController(this, view.getSubScene(), view, overlay);

        root.getChildren().addAll(view.getSubScene(), overlay);

        //addObject("src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj");
    }

    public void addObject(String path) {
        try {
            Model3D model = view.addObject(path);
            Rotate rX = new Rotate(0, Rotate.X_AXIS);
            Rotate rY = new Rotate(0, Rotate.Y_AXIS);
            Translate t = new Translate();

            cameraController.getModelCameraMap().put(model, new CameraControlWrapper(rX, rY, t));

            cameraController.setSelected(model);

            model.getRoot().getTransforms().addAll(rX, rY);
            model.getRoot().getParent().getTransforms().add(t);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeObject(int id) {
        Model3D model = cameraController.getSelected();
        view.removeObject(model);
        cameraController.getModelCameraMap().remove(model);
        if (!cameraController.getModelCameraMap().isEmpty())
            cameraController.setSelected((Model3D) cameraController.getModelCameraMap().keySet().toArray()[0]);
    }

    public void handleKey(KeyEvent event) {
        switch (event.getCode()) {
            case F3 -> addObject("src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj");
            case DELETE -> removeObject(0);//todo
        }
    }
}