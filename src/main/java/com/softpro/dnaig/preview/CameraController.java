package com.softpro.dnaig.preview;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Model3D;

import java.util.HashMap;

/*
    Adapted from https://projects.isp.uni-luebeck.de/isp/tutorial/-/blob/ab58f3d450d5e888ea48c540d3bc9cf435f69cd8/javafx/3d/src/main/java/main/Controller3D.java
 */

public class CameraController {

    private final PreviewWindow previewWindow;
    private Point2D oldPos;
    private final HashMap<Model3D, CameraControlWrapper> modelCameraMap;
    private Model3D selected = null;
    private final Rotate cameraRX;
    private final Rotate cameraRY;
    private final Translate cameraT;
    private final Scale scale;
    private final View view;

    /**
     * Represents a CameraController object that controls the camera in a 3D scene.
     *
     * @param previewWindow The PreviewWindow object to display the preview.
     * @param subScene The SubScene object to set the camera for.
     * @param view The View object representing the scene view.
     * @param overlay The Overlay object to display overlays on the scene.
     */
    public CameraController(PreviewWindow previewWindow, SubScene subScene, /*Stage stage,*/ View view, Overlay overlay) {

        this.previewWindow = previewWindow;
        this.view = view;

        cameraRX = new Rotate(0, Rotate.X_AXIS);
        cameraRY = new Rotate(0, Rotate.Y_AXIS);
        cameraT = new Translate(0, 0, 0);
        scale = new Scale(1, 1, 1);

        modelCameraMap = new HashMap<>();

        //view.getModel().getRoot().getTransforms().addAll(rX, rY);
        this.view.getCamera().getTransforms().addAll(cameraRX, cameraRY/*, cameraT*/);

        previewWindow.root.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMouseClick);
        previewWindow.root.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseRelease);
        previewWindow.root.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, this::handleMouseExit);
        previewWindow.root.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleMouseMove);
        previewWindow.root.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDrag);
        previewWindow.root.addEventHandler(ScrollEvent.SCROLL, this::handleScroll);

        /*overlay.getLblRotX().textProperty().bind(Bindings.concat("Rotation X: ", modelCameraMap.entrySet().stream().findFirst().get().getValue().getrX().angleProperty().asString()));
        overlay.getLblRotY().textProperty().bind(Bindings.concat("Rotation Y: ", modelCameraMap.entrySet().stream().findFirst().get().getValue().getrY().angleProperty().asString()));*/
        overlay.getLblMode().textProperty().bind(Bindings.concat("Mode: ", previewWindow.getCurrentMode().asString()));
        overlay.getLblCameraTx().textProperty().bind(Bindings.concat("X: ", this.view.getCamera().translateXProperty().asString()));
        overlay.getLblCameraTy().textProperty().bind(Bindings.concat("Y: ", this.view.getCamera().translateYProperty().asString()));
        overlay.getLblCameraTz().textProperty().bind(Bindings.concat("Z: ", this.view.getCamera().translateZProperty().asString()));
    }

    /**
     * Retrieves the mapping between the Model3D objects and their corresponding CameraControlWrapper objects.
     *
     * @return The HashMap representing the mapping between Model3D and CameraControlWrapper.
     */
    public HashMap<Model3D, CameraControlWrapper> getModelCameraMap() {
        return modelCameraMap;
    }

    /**
     * Retrieves the currently selected Model3D object.
     *
     * @return The selected Model3D object, or null if no object is currently selected.
     */
    public Model3D getSelected() {
        return selected;
    }

    /**
     * Sets the currently selected Model3D object.
     *
     * @param selected The Model3D object to set as the selected object.
     */
    protected void setSelected(Model3D selected) {
        this.selected = selected;
    }

    /**
     * Resets the camera to its default position and orientation.
     */
    public void resetCamera() {
        cameraRX.setAngle(0.0);
        cameraRY.setAngle(0.0);
        //cameraT.setX(0.0);
        //cameraT.setY(0.0);
        view.getCamera().setTranslateX(0);
        view.getCamera().setTranslateY(0);
        view.getCamera().setTranslateZ(0);
        System.out.println("reset");
    }

    /**
     * Moves the camera in the specified direction.
     *
     * @param direction The direction to move the camera. Can be one of the following:
     *                  - "R" for moving the camera to the right
     *                  - "L" for moving the camera to the left
     *                  - "D" for moving the camera down
     *                  - "U" for moving the camera up
     */
    public void moveCamera(String direction) {
        switch (direction) {
            case "R" -> cameraRY.setAngle(cameraRY.getAngle()+5);
            case "L" -> cameraRY.setAngle(cameraRY.getAngle()-5);
            case "D" -> cameraRX.setAngle(cameraRX.getAngle()-5);
            case "U" -> cameraRX.setAngle(cameraRX.getAngle()+5);
        }
    }

    /**
     * Handles the scroll event by adjusting the translation of the camera along the z-axis.
     *
     * @param event The ScrollEvent object representing the scroll event.
     */
    private void handleScroll(ScrollEvent event) {
        double delta = event.getDeltaY();
        Camera camera = this.view.getCamera();
        camera.setTranslateZ(camera.getTranslateZ() + delta * 2);
    }

    /**
     * Handles the mouse click event by setting the old position to the coordinates
     * of the clicked point.
     *
     * @param event The MouseEvent object representing the mouse click event.
     */
    private void handleMouseClick(MouseEvent event) {
        oldPos = new Point2D(event.getSceneX(), event.getSceneY());
    }

    /**
     * Handles the mouse release event by changing the current mode based on the target type and the current mode.
     * If the target type is CAMERA and the current mode is not MOVE_CAMERA_XY, sets the current mode to MOVE_CAMERA_XY.
     * If the target type is OBJECT and the current mode is not ROTATE_OBJECT_XY, sets the current mode to MOVE_OBJECT_XY.
     *
     * @param event The MouseEvent object representing the mouse release event.
     */
    private void handleMouseRelease(MouseEvent event) {
        Mode currentMode = previewWindow.getCurrentMode().get();
        if (currentMode.getTargetType() == Mode.TargetType.CAMERA && currentMode != Mode.MOVE_CAMERA_XY) {
            previewWindow.getCurrentMode().set(Mode.MOVE_CAMERA_XY);
        } else if (currentMode.getTargetType() == Mode.TargetType.OBJECT && currentMode != Mode.ROTATE_OBJECT_XY) {
            previewWindow.getCurrentMode().set(Mode.MOVE_OBJECT_XY);
        }
        //TODO: previous mode?
    }

    /**
     * Handles the mouse move event by performing different actions based on the current mode.
     *
     * @param event The MouseEvent object representing the mouse move event.
     */
    private void handleMouseMove(MouseEvent event) {
        /*switch (previewWindow.getCurrentMode().get()) {
            case ROTATE_CAMERA_XY -> {}
            case ROTATE_CAMERA_X -> {}
            case ROTATE_CAMERA_Y -> {}
            case ROTATE_CAMERA_Z -> {}
            case ROTATE_OBJECT_XY -> {}
            case ROTATE_OBJECT_X -> {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                wrapper.getrX().setAngle(wrapper.getrX().getAngle() + oldPos.getY() - event.getSceneY());
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case ROTATE_OBJECT_Y -> {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                wrapper.getrY().setAngle(wrapper.getrY().getAngle() - oldPos.getX() + event.getSceneX());
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case ROTATE_OBJECT_Z -> {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                wrapper.getrZ().setAngle(wrapper.getrZ().getAngle() - oldPos.getX() + event.getSceneX());
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case MOVE_CAMERA_X -> {
                this.view.getCamera().setTranslateX(this.view.getCamera().getTranslateX() - (oldPos.getX() - event.getSceneX()) * 0.2);
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case MOVE_CAMERA_Y -> {
                this.view.getCamera().setTranslateY(this.view.getCamera().getTranslateY() - (oldPos.getY() - event.getSceneY()) * 0.2);
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case MOVE_CAMERA_Z -> {
                this.view.getCamera().setTranslateZ(this.view.getCamera().getTranslateZ() - (oldPos.getX() - event.getSceneX()) * 0.2);
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case MOVE_OBJECT_X -> {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                wrapper.getT().setX(wrapper.getT().getX() + (oldPos.getX() - event.getSceneX()) * 0.2);
                wrapper.updatePivotAfterMove();
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case MOVE_OBJECT_Y -> {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                wrapper.getT().setY(wrapper.getT().getY() + (oldPos.getY() - event.getSceneY()) * 0.2);
                wrapper.updatePivotAfterMove();
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case MOVE_OBJECT_Z -> {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                wrapper.getT().setZ(wrapper.getT().getZ() - (oldPos.getX() - event.getSceneX()) * 0.2);
                wrapper.updatePivotAfterMove();
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
            case ERROR -> {
                System.out.println("01100101 01110010 01110010 01101111 01110010");
            }
        }*/
    }

    /**
     * Handles the mouse drag event by performing different actions based on the current mode and mouse button.
     *
     * @param event The MouseEvent object representing the mouse drag event.
     */
    private void handleMouseDrag(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            switch (previewWindow.getCurrentMode().get()) {
                case ROTATE_CAMERA_XY -> {}
                case ROTATE_CAMERA_X -> {}
                case ROTATE_CAMERA_Y -> {}
                case ROTATE_CAMERA_Z -> {}
                case ROTATE_OBJECT_XY -> {}
                case ROTATE_OBJECT_X -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getrX().setAngle(wrapper.getrX().getAngle() + oldPos.getY() - event.getSceneY());
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case ROTATE_OBJECT_Y -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getrY().setAngle(wrapper.getrY().getAngle() - oldPos.getX() + event.getSceneX());
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case ROTATE_OBJECT_Z -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getrZ().setAngle(wrapper.getrZ().getAngle() - oldPos.getX() + event.getSceneX());
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case ERROR -> System.out.println("01100101 01110010 01110010 01101111 01110010");
            }
        } else {
            if (event.getButton() != MouseButton.SECONDARY) {
                return;
            }
            switch (previewWindow.getCurrentMode().get()) {
                case MOVE_CAMERA_XY -> {
                    this.view.getCamera().setTranslateX(this.view.getCamera().getTranslateX() - (oldPos.getX() - event.getSceneX()) * 0.2);
                    this.view.getCamera().setTranslateY(this.view.getCamera().getTranslateY() - (oldPos.getY() - event.getSceneY()) * 0.2);
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_CAMERA_X -> {
                    this.view.getCamera().setTranslateX(this.view.getCamera().getTranslateX() - (oldPos.getX() - event.getSceneX()) * 0.2);
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_CAMERA_Y -> {
                    this.view.getCamera().setTranslateY(this.view.getCamera().getTranslateY() - (oldPos.getY() - event.getSceneY()) * 0.2);
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_CAMERA_Z -> {
                    this.view.getCamera().setTranslateZ(this.view.getCamera().getTranslateZ() - (oldPos.getX() - event.getSceneX()) * 0.2);
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_OBJECT_XY -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getT().setX(wrapper.getT().getX() + (oldPos.getX() - event.getSceneX()) * 0.2);
                    wrapper.getT().setY(wrapper.getT().getY() + (oldPos.getY() - event.getSceneY()) * 0.2);
                    wrapper.updatePivotAfterMove();
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_OBJECT_X -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getT().setX(wrapper.getT().getX() + (oldPos.getX() - event.getSceneX()) * 0.2);
                    wrapper.updatePivotAfterMove();
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_OBJECT_Y -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getT().setY(wrapper.getT().getY() + (oldPos.getY() - event.getSceneY()) * 0.2);
                    wrapper.updatePivotAfterMove();
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case MOVE_OBJECT_Z -> {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getT().setZ(wrapper.getT().getZ() - (oldPos.getX() - event.getSceneX()) * 0.2);
                    wrapper.updatePivotAfterMove();
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
                case ERROR -> System.out.println("01100101 01110010 01110010 01101111 01110010");
            }
        }
    }

    /**
     * Handles the mouse exit event.
     *
     * @param event The MouseEvent object representing the mouse exit event.
     */
    private void handleMouseExit(MouseEvent event) {
        //System.out.printf("%f %f %f %f %f %f\n", event.getSceneX(), event.getSceneY(), event.getX(), event.getY(), event.getScreenX(), event.getScreenY());
        //mouseEvent.get
    }

    /**
     * Retrieves the CameraControlWrapper object associated with the specified Model3D.
     *
     * @param model The Model3D object for which to retrieve the CameraControlWrapper.
     * @return The CameraControlWrapper associated with the specified Model3D.
     */
    public CameraControlWrapper getCameraControlWrapper(Model3D model) {
        return modelCameraMap.get(model);
    }

}