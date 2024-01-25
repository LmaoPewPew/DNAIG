package com.softpro.dnaig.preview;

import com.softpro.dnaig.ApplicationController;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.utils.Vector3D;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Model3D;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.softpro.dnaig.objData.mesh.Entity;

/**
 * The PreviewWindow class represents a window for previewing and manipulating 3D models.
 */
public class PreviewWindow {
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private View view;
    private Overlay overlay;
    private CameraController cameraController;
    private Map<Integer, Model3D> tempModelList;
    private ObjectProperty<Mode> currentMode = new SimpleObjectProperty<>(Mode.MOVE_CAMERA_XY);
    private ObjectProperty<KeyCode> currentModifier = new SimpleObjectProperty<>(null);
    public StackPane root;
    private ApplicationController applicationController;

    /**
     * Represents a preview window for displaying 3D models.
     *
     * @param root The root container that holds the preview window.
     * @param applicationController The application controller used for managing the application.
     */
    public PreviewWindow(StackPane root, ApplicationController applicationController) {
        this.root = root;
        this.applicationController = applicationController;

        view = new View();
        overlay = new Overlay();
        cameraController = new CameraController(this, view.getSubScene(), view, overlay);
        tempModelList = new HashMap<>();

        root.getChildren().addAll(view.getSubScene(), overlay);

        overlay.getLblModifier().textProperty().bind(Bindings.concat("Modifier: ", currentModifier.asString()));

        //addObject("src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj");
    }

    /**
     * Adds a 3D object to the preview window.
     *
     * @param path The file path of the 3D object.
     * @param id The ID of the 3D object.
     * @throws RuntimeException If there is an error loading the 3D object.
     */
    public void addObject(String path, int id) {
        try {
            Model3D model = view.addObject(path);
            Rotate rX = new Rotate(0, Rotate.X_AXIS);
            Rotate rY = new Rotate(0, Rotate.Y_AXIS);
            Rotate rZ = new Rotate(0, Rotate.Z_AXIS);
            Translate t = new Translate();
            Scale s = new Scale(1, 1, 1);

            tempModelList.put(id, model);

            cameraController.getModelCameraMap().put(model, new CameraControlWrapper(rX, rY, rZ, t, s));

            cameraController.setSelected(model);
            //currentMode.set(Mode.MOVE_OBJECT_XY); //DISABLED OBJECT MOVEMENT VIA PREVIEW//

            model.getRoot().getTransforms().addAll(rX, rY, rZ, t, s);
            //model.getRoot().getParent().getTransforms().add(t);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a 3D object from the preview window.
     *
     * @param id The ID of the 3D object to remove.
     */
    public void removeObject(int id) {
        Model3D model = tempModelList.get(id);
        view.removeObject(model);
        cameraController.getModelCameraMap().remove(model);
        if (!cameraController.getModelCameraMap().isEmpty())
            cameraController.setSelected((Model3D) cameraController.getModelCameraMap().keySet().toArray()[0]);
    }

    /**
     * Removes the currently selected Model3D object from the preview window.
     */
    public void removeSelected() {
        Model3D model = cameraController.getSelected();
        view.removeObject(model);
        cameraController.getModelCameraMap().remove(model);

        int id = -1;
        for (Map.Entry<Integer, Model3D> integerModel3DEntry : tempModelList.entrySet()) {
            if (integerModel3DEntry.getValue() == model) {
                id = integerModel3DEntry.getKey();
                tempModelList.remove(id);
                break;
            }
        }

        if (id > -1) {
            applicationController.deleteObject(id);
        }

        updateSelected(-1);
    }

    /**
     * Handles key events for the PreviewWindow class.
     *
     * @param event The KeyEvent object representing the key event.
     */
    public void handleKey(KeyEvent event) {
        if (currentModifier.get() == null) {
            switch (event.getCode()) {
                case DELETE -> removeSelected();
                case ESCAPE -> updateSelected(-1);
                case F9 -> cameraController.resetCamera();
                case LEFT -> cameraController.moveCamera("L");
                case RIGHT -> cameraController.moveCamera("R");
                case UP -> cameraController.moveCamera("U");
                case DOWN -> cameraController.moveCamera("D");
                case M, R -> setCurrentModifier(event.getCode());
            }
        } else if (currentModifier.get() == KeyCode.M || currentModifier.get() == KeyCode.G) {
            switch (event.getCode()) {
                case X -> {
                    currentMode.set(currentMode.get().getTargetType() == Mode.TargetType.CAMERA ? Mode.MOVE_CAMERA_X : Mode.MOVE_OBJECT_X);
                    currentModifier.set(null);
                }
                case Y -> {
                    currentMode.set(currentMode.get().getTargetType() == Mode.TargetType.CAMERA ? Mode.MOVE_CAMERA_Y : Mode.MOVE_OBJECT_Y);
                    currentModifier.set(null);
                }
                case Z -> {
                    currentMode.set(currentMode.get().getTargetType() == Mode.TargetType.CAMERA ? Mode.MOVE_CAMERA_Z : Mode.MOVE_OBJECT_Z);
                    currentModifier.set(null);
                }
                default -> currentModifier.set(null);
            }
        } else if (currentModifier.get() == KeyCode.R) {
            switch (event.getCode()) {
                case X -> {
                    currentMode.set(currentMode.get().getTargetType() == Mode.TargetType.CAMERA ? Mode.ROTATE_CAMERA_X : Mode.ROTATE_OBJECT_X);
                    currentModifier.set(null);
                }
                case Y -> {
                    currentMode.set(currentMode.get().getTargetType() == Mode.TargetType.CAMERA ? Mode.ROTATE_CAMERA_Y : Mode.ROTATE_OBJECT_Y);
                    currentModifier.set(null);
                }
                case Z -> {
                    currentMode.set(currentMode.get().getTargetType() == Mode.TargetType.CAMERA ? Mode.ROTATE_CAMERA_Z : Mode.ROTATE_OBJECT_Z);
                    currentModifier.set(null);
                }
                default -> currentModifier.set(null);
            }
        }
    }

    /**
     * Sets the current modifier key based on the provided KeyCode.
     *
     * @param keyCode The KeyCode representing the key pressed.
     */
    private void setCurrentModifier(KeyCode keyCode) {
        currentModifier.set(keyCode);
    }

    /**
     * Updates the selected Model3D object based on the provided ID.
     *
     * @param id The ID of the Model3D object.
     */
    public void updateSelected(int id) {
        if (id < 0) {
            cameraController.setSelected(null);
            currentMode.set(Mode.MOVE_CAMERA_XY);
        } else {
            cameraController.setSelected(tempModelList.get(id));
            //currentMode.set(Mode.MOVE_OBJECT_XY); //DISABLED OBJECT MOVEMENT VIA PREVIEW//
        }
    }

    /**
     * Retrieves entity data for a given Entity object.
     *
     * @param entity The Entity object to retrieve data from.
     */
    public void getEntityData(Entity entity){
        int id = entity.getID();
        Model3D model = tempModelList.get(id);

        CameraControlWrapper wrapper = cameraController.getCameraControlWrapper(model);
        Translate translate = wrapper.getT();

        entity.setPivot(new Vector3D(
            translate.getX(),
            translate.getZ(),
                translate.getY())
        );

        System.out.println("Pivot: " + entity.getPivot());

        System.out.println(model);
        System.out.println("ID: " + id);
    }

    /**
     * Retrieves the current mode of the PreviewWindow.
     *
     * @return The current mode represented as an ObjectProperty of type Mode.
     */
    public ObjectProperty<Mode> getCurrentMode() {
        return currentMode;
    }

    /**
     * Updates the position, rotation, and scale of a 3D model.
     *
     * @param id The ID of the 3D model.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @param z The z-coordinate of the position.
     * @param xRot The rotation angle along the X-axis.
     * @param yRot The rotation angle along the Y-axis.
     * @param zRot The rotation angle along the Z-axis.
     * @param scale The scale factor of the model.
     */
    public void updatePosition(int id, double x, double y, double z, double xRot, double yRot, double zRot, double scale) {
        Model3D model = tempModelList.get(id);
        CameraControlWrapper wrapper = cameraController.getCameraControlWrapper(model);
        Translate translate = wrapper.getT();
        Rotate rotateX = wrapper.getrX();
        Rotate rotateY = wrapper.getrY();
        Rotate rotateZ = wrapper.getrZ();
        Scale s = wrapper.getS();
        s.setX(scale);
        s.setY(scale);
        s.setZ(scale);

        translate.setX(x);
        translate.setY(y);
        translate.setZ(z);

        rotateX.setAngle(xRot);
        rotateY.setAngle(yRot);
        rotateZ.setAngle(zRot);

        wrapper.updatePivotAfterMove();
    }
}