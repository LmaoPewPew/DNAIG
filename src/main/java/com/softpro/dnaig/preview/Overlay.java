package com.softpro.dnaig.preview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Overlay extends BorderPane {

    private final Label lblMode = new Label("Mode: ");
    private final Label lblCameraRx = new Label("Rotation X: ");
    private final Label lblCameraRy = new Label("Rotation Y: ");
    private final Label lblCameraRz = new Label("Rotation Z: ");
    private final Label lblCameraTx = new Label("X: ");
    private final Label lblCameraTy = new Label("Y: ");
    private final Label lblCameraTz = new Label("Z: ");

    public Overlay() {
        VBox left = new VBox();
        left.setAlignment(Pos.TOP_LEFT);
        left.setPadding(new Insets(30));
        left.setSpacing(10);

        VBox.setMargin(lblMode, new Insets(0, 0, 20, 0));

        left.getChildren().addAll(lblMode, new Label("Camera: "), lblCameraRx, lblCameraRy, lblCameraTx, lblCameraTy, lblCameraTz);
        setLeft(left);
    }

    public Label getLblMode() {
        return lblMode;
    }

    public Label getLblCameraRx() {
        return lblCameraRx;
    }

    public Label getLblCameraRy() {
        return lblCameraRy;
    }

    public Label getLblCameraRz() {
        return lblCameraRz;
    }

    public Label getLblCameraTx() {
        return lblCameraTx;
    }

    public Label getLblCameraTy() {
        return lblCameraTy;
    }

    public Label getLblCameraTz() {
        return lblCameraTz;
    }
}
