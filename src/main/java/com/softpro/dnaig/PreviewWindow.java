package com.softpro.dnaig;

import com.softpro.dnaig.utils.Overlay;
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

        /*Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Import OBJ-File and create an entity object.
        Entity cube = ObjFileReader.createObject("ObjFiles/porsche/porsche.obj");








        // Debug coordinates before scaling.
        cube.forEach(System.out::println);

        // Scaling object.
        cube.scale(5);

        // Rotate object.
        cube.rotateByDegree(10,20,30);

        // Set new pivot point.
        cube.setPivot(new Vector3D(
                0,0,0
        ));

        // Print out new adapted coordinates.
        cube.forEach(System.out::println);

        // Set 3D depth, with this value the depth of the 3D model can be influenced and adjusted.
        double depth = 400;

        gc.setStroke(Color.RED);
        gc.strokeLine(0,yOffset,WIDTH,yOffset);

        // Set stroke color.
        gc.setStroke(Color.BLACK);

        // Iterate through each face.
        for (Face face : cube) {

            // Each face needs at least 3 vertices.
            if (face.getVerticeCount() < 3)
                return;

            // Iterate through each vertex.
            for (int i = 0; i < face.getVerticeCount(); i++) {
                Vertex currentVertex = face.getVertex(i);
                Vertex nextVertex = face.getVertex((i + 1) % face.getVerticeCount());

                // Scaled x and y coordinates to create 3D effect with z and depth.
                double scaledCurrentX = currentVertex.getCoordinates().getX() / (currentVertex.getCoordinates().getZ() + depth);
                double scaledCurrentY = currentVertex.getCoordinates().getY() / (currentVertex.getCoordinates().getZ() + depth);

                // Scaled x and y coordinates to create 3D effect with z and depth.
                double scaledNextX = nextVertex.getCoordinates().getX() / (nextVertex.getCoordinates().getZ() + depth);
                double scaledNextY = nextVertex.getCoordinates().getY() / (nextVertex.getCoordinates().getZ() + depth);

                // Adding offset for the screen canvas.
                double x1 = scaledCurrentX * xOffset + xOffset;
                double y1 = -scaledCurrentY * yOffset + yOffset;

                // Adding offset for the screen canvas.
                double x2 = scaledNextX * xOffset + xOffset;
                double y2 = -scaledNextY * yOffset + yOffset;

                // Draw line.
                gc.strokeLine(x1, y1, x2, y2);
            }
        }

        root.getChildren().add(canvas);*/

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}