package com.softpro.dnaig.preview;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
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
    private final View view;

    public CameraController(PreviewWindow previewWindow, SubScene subScene, /*Stage stage,*/ View view, Overlay overlay) {

        this.previewWindow = previewWindow;
        this.view = view;

        cameraRX = new Rotate(0, Rotate.X_AXIS);
        cameraRY = new Rotate(0, Rotate.Y_AXIS);
        cameraT = new Translate(0, 0, 0);

        modelCameraMap = new HashMap<>();

        //view.getModel().getRoot().getTransforms().addAll(rX, rY);
        this.view.getCamera().getTransforms().addAll(cameraRX, cameraRY/*, cameraT*/);

        previewWindow.root.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            oldPos = new Point2D(event.getSceneX(), event.getSceneY());
        });

        previewWindow.root.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (selected != null) {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    double newAngleX = wrapper.getrX().getAngle() + oldPos.getY() - event.getSceneY();
                    wrapper.getrX().setAngle(newAngleX);
                    wrapper.getrY().setAngle(wrapper.getrY().getAngle() - oldPos.getX() + event.getSceneX());
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                } else {
                    // todo
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                switch (previewWindow.getCurrentMode().get()) {
                    case MOVE_CAMERA_FREE -> {
                        this.view.getCamera().setTranslateX(this.view.getCamera().getTranslateX() - (oldPos.getX() - event.getSceneX()) * 0.2);
                        this.view.getCamera().setTranslateY(this.view.getCamera().getTranslateY() - (oldPos.getY() - event.getSceneY()) * 0.2);
                        oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                    }
                    case MOVE_OBJECT_FREE -> {
                        CameraControlWrapper wrapper = modelCameraMap.get(selected);
                        wrapper.getT().setX(wrapper.getT().getX() + (oldPos.getX() - event.getSceneX()) * 0.2);
                        wrapper.getT().setY(wrapper.getT().getY() + (oldPos.getY() - event.getSceneY()) * 0.2);
                        wrapper.updatePivotAfterMove();
                        oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                    }
                    case ERROR -> {}
                }
            }
        });

        previewWindow.root.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            Camera camera = this.view.getCamera();
            camera.setTranslateZ(camera.getTranslateZ() + delta * 2);
        });

        /*stage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            PickResult n = event.getPickResult();
            final int testn = n.getIntersectedFace();
            System.out.println("test " + testn);
            System.out.println("test " + n.getIntersectedNode());
        });*/

        /*overlay.getLblRotX().textProperty().bind(Bindings.concat("Rotation X: ", modelCameraMap.entrySet().stream().findFirst().get().getValue().getrX().angleProperty().asString()));
        overlay.getLblRotY().textProperty().bind(Bindings.concat("Rotation Y: ", modelCameraMap.entrySet().stream().findFirst().get().getValue().getrY().angleProperty().asString()));*/
        overlay.getLblMode().textProperty().bind(Bindings.concat("Mode: ", previewWindow.getCurrentMode().asString()));
        overlay.getLblCameraTx().textProperty().bind(Bindings.concat("X: ", this.view.getCamera().translateXProperty().asString()));
        overlay.getLblCameraTy().textProperty().bind(Bindings.concat("Y: ", this.view.getCamera().translateYProperty().asString()));
        overlay.getLblCameraTz().textProperty().bind(Bindings.concat("Z: ", this.view.getCamera().translateZProperty().asString()));
    }

    public HashMap<Model3D, CameraControlWrapper> getModelCameraMap() {
        return modelCameraMap;
    }

    public Model3D getSelected() {
        return selected;
    }

    protected void setSelected(Model3D selected) {
        this.selected = selected;
    }

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

    public void moveCamera(String direction) {
        switch (direction) {
            case "R" -> cameraRY.setAngle(cameraRY.getAngle()+5);
            case "L" -> cameraRY.setAngle(cameraRY.getAngle()-5);
            case "D" -> cameraRX.setPivotX(cameraRX.getAngle()-5);
            case "U" -> cameraRX.setAngle(cameraRX.getAngle()+5);
        }
    }
}