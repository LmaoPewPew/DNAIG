package com.softpro.dnaig;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/*
    Adapted from https://projects.isp.uni-luebeck.de/isp/tutorial/-/blob/ab58f3d450d5e888ea48c540d3bc9cf435f69cd8/javafx/3d/src/main/java/main/Controller3D.java
 */

public class CameraController {

    private Point2D oldPos;

    public CameraController(Stage stage, View view, Overlay overlay) {

        Rotate rX = new Rotate(0, Rotate.X_AXIS);
        Rotate rY = new Rotate(0, Rotate.Y_AXIS);
        Translate t = new Translate();

        view.getModel().getRoot().getTransforms().addAll(rX, rY);
        view.getCamera().getTransforms().add(t);

        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            oldPos = new Point2D(event.getSceneX(), event.getSceneY());
        });

        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double newAngleX = rX.getAngle() + oldPos.getY() - event.getSceneY();
                rX.setAngle(newAngleX);
                rY.setAngle(rY.getAngle() - oldPos.getX() + event.getSceneX());
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

        overlay.getLblRotX().textProperty().bind(Bindings.concat("Rotation X: ", rX.angleProperty().asString()));
        overlay.getLblRotY().textProperty().bind(Bindings.concat("Rotation Y: ", rY.angleProperty().asString()));
        overlay.getLblZoom().textProperty().bind(Bindings.concat("Z: ", view.getCamera().translateZProperty().asString()));
    }
}