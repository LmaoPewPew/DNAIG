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
            currentMode.set(Mode.MOVE_OBJECT_XY);

            model.getRoot().getTransforms().addAll(rX, rY, rZ, t, s);
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

    private void setCurrentModifier(KeyCode keyCode) {
        currentModifier.set(keyCode);
    }

    public void updateSelected(int id) {
        if (id < 0) {
            cameraController.setSelected(null);
            currentMode.set(Mode.MOVE_CAMERA_XY);
        } else {
            cameraController.setSelected(tempModelList.get(id));
            currentMode.set(Mode.MOVE_OBJECT_XY);
        }
    }

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

    public ObjectProperty<Mode> getCurrentMode() {
        return currentMode;
    }

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
    }
}