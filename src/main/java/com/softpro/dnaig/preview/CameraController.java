package com.softpro.dnaig.preview;

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

    private PreviewWindow previewWindow;
    private Point2D oldPos;
    private HashMap<Model3D, CameraControlWrapper> modelCameraMap;
    private Model3D selected = null;
    private Rotate cameraRX;
    private Rotate cameraRY;
    private Translate cameraT;

    public CameraController(PreviewWindow previewWindow, SubScene subScene, /*Stage stage,*/ View view, Overlay overlay) {

        this.previewWindow = previewWindow;

        cameraRX = new Rotate(0, Rotate.X_AXIS);
        cameraRY = new Rotate(0, Rotate.Y_AXIS);
        cameraT = new Translate();

        modelCameraMap = new HashMap<>();

        //view.getModel().getRoot().getTransforms().addAll(rX, rY);
        view.getCamera().getTransforms().addAll(cameraRX, cameraRY, cameraT);

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
                if (selected != null) {
                    CameraControlWrapper wrapper = modelCameraMap.get(selected);
                    wrapper.getT().setX(wrapper.getT().getX() + (oldPos.getX() - event.getSceneX()) * 0.2);
                    wrapper.getT().setY(wrapper.getT().getY() + (oldPos.getY() - event.getSceneY()) * 0.2);
                    wrapper.updatePivotAfterMove();
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                } else {
                    cameraT.setX(cameraT.getX() + (oldPos.getX() - event.getSceneX()) * 0.2);
                    cameraT.setY(cameraT.getY() + (oldPos.getY() - event.getSceneY()) * 0.2);
                    oldPos = new Point2D(event.getSceneX(), event.getSceneY());
                }
            }
        });

        previewWindow.root.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            Camera camera = view.getCamera();
            camera.setTranslateZ(camera.getTranslateZ() + delta * 2);
        });

        /*stage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            PickResult n = event.getPickResult();
            final int testn = n.getIntersectedFace();
            System.out.println("test " + testn);
            System.out.println("test " + n.getIntersectedNode());
        });*/

        /*overlay.getLblRotX().textProperty().bind(Bindings.concat("Rotation X: ", modelCameraMap.entrySet().stream().findFirst().get().getValue().getrX().angleProperty().asString()));
        overlay.getLblRotY().textProperty().bind(Bindings.concat("Rotation Y: ", modelCameraMap.entrySet().stream().findFirst().get().getValue().getrY().angleProperty().asString()));
        overlay.getLblZoom().textProperty().bind(Bindings.concat("Z: ", view.getCamera().translateZProperty().asString()));*/
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
}