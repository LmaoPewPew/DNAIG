package com.softpro.dnaig.preview;

import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.input.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz3d.importers.Model3D;

import java.util.HashMap;

/*
    Adapted from https://projects.isp.uni-luebeck.de/isp/tutorial/-/blob/ab58f3d450d5e888ea48c540d3bc9cf435f69cd8/javafx/3d/src/main/java/main/Controller3D.java
 */

public class CameraController {

    private PreviewWindow previewWindow;
    private Point2D oldPos;
    private HashMap<Model3D, CameraControlWrapper> modelCameraMap;
    private Model3D selected;

    public CameraController(PreviewWindow previewWindow, Stage stage, View view, Overlay overlay) {

        this.previewWindow = previewWindow;

        /*Rotate rX = new Rotate(0, Rotate.X_AXIS);
        Rotate rY = new Rotate(0, Rotate.Y_AXIS);*/
        Translate t = new Translate();

        modelCameraMap = new HashMap<>();

        //view.getModel().getRoot().getTransforms().addAll(rX, rY);
        view.getCamera().getTransforms().add(t);

        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            oldPos = new Point2D(event.getSceneX(), event.getSceneY());
        });

        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                CameraControlWrapper wrapper = modelCameraMap.get(selected);
                double newAngleX = wrapper.getrX().getAngle() + oldPos.getY() - event.getSceneY();
                wrapper.getrX().setAngle(newAngleX);
                wrapper.getrY().setAngle(wrapper.getrY().getAngle() - oldPos.getX() + event.getSceneX());
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                t.setX(t.getX() + (oldPos.getX() - event.getSceneX())*0.2);
                t.setY(t.getY() + (oldPos.getY() - event.getSceneY())*0.2);
                oldPos = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            Camera camera = view.getCamera();
            camera.setTranslateZ(camera.getTranslateZ() + delta * 2);
        });

        stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode().equals(KeyCode.F3)) {
                previewWindow.addObject("ObjFiles/porsche/porsche.obj");
            }
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