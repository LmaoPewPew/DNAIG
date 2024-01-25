package com.softpro.dnaig.preview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Overlay class provides a (debug) user interface overlay for the PreviewWindow class.
 */
public class Overlay extends BorderPane {

    private final Label lblMode = new Label("Mode: ");
    private final Label lblModifier = new Label("Modifier: ");
    private final Label lblCameraRx = new Label("Rotation X: ");
    private final Label lblCameraRy = new Label("Rotation Y: ");
    private final Label lblCameraRz = new Label("Rotation Z: ");
    private final Label lblCameraTx = new Label("X: ");
    private final Label lblCameraTy = new Label("Y: ");
    private final Label lblCameraTz = new Label("Z: ");

    /**
     * Constructs a new Overlay object.
     *
     * The Overlay class provides a (debug) user interface overlay for the PreviewWindow class.
     *
     * The constructor initializes the UI elements for mode, modifier, and camera coordinates.
     * It sets up a VBox layout with the labels and adds it to the left side of the Overlay's BorderPane.
     */
    public Overlay() {
        VBox left = new VBox();
        left.setAlignment(Pos.TOP_LEFT);
        left.setPadding(new Insets(30));
        left.setSpacing(10);

        VBox.setMargin(lblMode, new Insets(0, 0, 20, 0));
        VBox.setMargin(lblModifier, new Insets(0, 0, 20, 0));

        left.getChildren().addAll(lblMode, lblModifier, new Label("Camera: "), lblCameraRx, lblCameraRy, lblCameraTx, lblCameraTy, lblCameraTz);
        setLeft(left);
    }

    /**
     * Retrieves the lblMode label.
     *
     * @return The lblMode label.
     */
    public Label getLblMode() {
        return lblMode;
    }

    /**
     * Retrieves the lblModifier label from the Overlay class.
     *
     * @return The lblModifier label.
     */
    public Label getLblModifier() {
        return lblModifier;
    }

    /**
     * Retrieves the lblCameraRx label from the Overlay class.
     *
     * @return The lblCameraRx label.
     */
    public Label getLblCameraRx() {
        return lblCameraRx;
    }

    /**
     * Retrieves the lblCameraRy label from the Overlay class.
     *
     * @return The lblCameraRy label.
     */
    public Label getLblCameraRy() {
        return lblCameraRy;
    }

    /**
     * Retrieves the lblCameraRz label from the Overlay class.
     *
     * @return The lblCameraRz label.
     */
    public Label getLblCameraRz() {
        return lblCameraRz;
    }

    /**
     * Retrieves the lblCameraTx label from the Overlay class.
     *
     * @return The lblCameraTx label that represents the X coordinate of the camera.
     */
    public Label getLblCameraTx() {
        return lblCameraTx;
    }

    /**
     * Retrieves the lblCameraTy label from the Overlay class.
     *
     * @return The lblCameraTy label that represents the Y coordinate of the camera.
     */
    public Label getLblCameraTy() {
        return lblCameraTy;
    }

    /**
     * Retrieves the lblCameraTz label from the Overlay class.
     *
     * @return The lblCameraTz label that represents the Z coordinate of the camera.
     */
    public Label getLblCameraTz() {
        return lblCameraTz;
    }
}
