package com.softpro.dnaig.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Overlay extends BorderPane {

    private final Label lblRotX = new Label("Rotation X: ");
    private final Label lblRotY = new Label("Rotation Y: ");
    private final Label lblZoom = new Label("Z: ");

    public Overlay() {
        VBox right = new VBox();
        right.setAlignment(Pos.CENTER_LEFT);
        right.setPadding(new Insets(10));
        right.setSpacing(10);

        Label lblCamera = new Label("Camera: ");
        VBox.setMargin(lblCamera, new Insets(50, 0, 0, 0));
        right.getChildren().addAll(new Label("Model: "), lblRotX, lblRotY, lblCamera, lblZoom);
        setRight(right);
    }

    public Label getLblRotX() {
        return lblRotX;
    }

    public Label getLblRotY() {
        return lblRotY;
    }

    public Label getLblZoom() {
        return lblZoom;
    }
}
