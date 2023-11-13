package com.softpro.dnaig;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PreviewWindow extends Application {

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private final int yOffset = HEIGHT / 2;
    private final int xOffset = WIDTH / 2;

    @Override
    public void start(Stage stage) {
        stage.setTitle("3D Cube Projection in JavaFX");
        StackPane root = new StackPane();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.rgb(30,30,30));

        stage.setScene(scene);

        View view = new View(stage);
        Overlay overlay = new Overlay();

        root.getChildren().addAll(view.getSubScene(), overlay);

        new CameraController(stage, view, overlay);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}