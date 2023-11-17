package com.softpro.dnaig.preview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Model3D;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PreviewWindow {
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private View view;
    private Overlay overlay;
    private CameraController cameraController;
    private Map<Integer, Model3D> tempModelList;
    private int tempModelCount = 0;
    private ObjectProperty<Mode> currentMode = new SimpleObjectProperty<>(Mode.MOVE_CAMERA_FREE);
    private ObjectProperty<KeyCode> currentModifier = new SimpleObjectProperty<>(null);
    public StackPane root;

    public PreviewWindow(StackPane root) {
        this.root = root;

        view = new View();
        overlay = new Overlay();
        cameraController = new CameraController(this, view.getSubScene(), view, overlay);
        tempModelList = new HashMap<>();

        root.getChildren().addAll(view.getSubScene(), overlay);

        overlay.getLblModifier().textProperty().bind(Bindings.concat("Modifier: ", currentModifier.asString()));

        //addObject("src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj");
    }

    public void addObject(String path) {
        try {
            Model3D model = view.addObject(path);
            Rotate rX = new Rotate(0, Rotate.X_AXIS);
            Rotate rY = new Rotate(0, Rotate.Y_AXIS);
            Translate t = new Translate();

            tempModelList.put(tempModelCount++, model);

            cameraController.getModelCameraMap().put(model, new CameraControlWrapper(rX, rY, t));

            cameraController.setSelected(model);
            currentMode.set(Mode.MOVE_OBJECT_FREE);

            model.getRoot().getTransforms().addAll(rX, rY, t);
            //model.getRoot().getParent().getTransforms().add(t);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeObject(int id) {
        Model3D model = tempModelList.get(id);
        view.removeObject(model);
        cameraController.getModelCameraMap().remove(model);
        if (!cameraController.getModelCameraMap().isEmpty())
            cameraController.setSelected((Model3D) cameraController.getModelCameraMap().keySet().toArray()[0]);
    }

    public void removeSelected() {
        Model3D model = cameraController.getSelected();
        view.removeObject(model);
        cameraController.getModelCameraMap().remove(model);
        updateSelected(-1);
    }

    public void handleKey(KeyEvent event) {
        if (currentModifier.get() == null) {
            switch (event.getCode()) {
                case F3 -> addObject("src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj");
                case DELETE -> removeSelected();
                case ESCAPE -> updateSelected(-1);
                case F9 -> cameraController.resetCamera();
                case LEFT -> cameraController.moveCamera("L");
                case RIGHT -> cameraController.moveCamera("R");
                case UP -> cameraController.moveCamera("U");
                case DOWN -> cameraController.moveCamera("D");
                case M -> setCurrentModifier(event.getCode());
            }
        } else if (currentModifier.get() == KeyCode.M) {
            switch (event.getCode()) {
                case X -> {
                    currentMode.set(currentMode.get().getType() == Mode.ModeType.CAMERA ? Mode.MOVE_CAMERA_X : Mode.MOVE_OBJECT_X);
                    currentModifier.set(null);
                }
                case Y -> {
                    currentMode.set(currentMode.get().getType() == Mode.ModeType.CAMERA ? Mode.MOVE_CAMERA_Y : Mode.MOVE_OBJECT_Y);
                    currentModifier.set(null);
                }
                case Z -> {
                    currentMode.set(currentMode.get().getType() == Mode.ModeType.CAMERA ? Mode.MOVE_CAMERA_Z : Mode.MOVE_OBJECT_Z);
                    currentModifier.set(null);
                }
                default -> currentModifier.set(null);
            }
        }
    }

    private void setCurrentModifier(KeyCode keyCode) {
        currentModifier.set(keyCode);
    }

    public void updateSelected(int id) {
        if (id < 0) {
            cameraController.setSelected(null);
            currentMode.set(Mode.MOVE_CAMERA_FREE);
        } else {
            cameraController.setSelected(tempModelList.get(id));
            currentMode.set(Mode.MOVE_OBJECT_FREE);
        }
    }

    public ObjectProperty<Mode> getCurrentMode() {
        return currentMode;
    }
}