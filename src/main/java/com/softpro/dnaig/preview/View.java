package com.softpro.dnaig.preview;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Importer3D;
import org.fxyz3d.importers.Model3D;
import org.fxyz3d.scene.Axes;
import org.fxyz3d.scene.CubeWorld;

import java.io.File;
import java.io.IOException;

public class View {

    Model3D model;
    Camera camera;
    Group group;
    SubScene subScene;

    /**
     * The View class represents a 3D view of the scene. It contains a camera, a group of objects, and a subscene for rendering the objects in a 3D space.
     */
    public View() {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(50000.0);
        Rotate rz = new Rotate(180.0, Rotate.Z_AXIS);
        camera.getTransforms().add(rz);
        camera.setTranslateX(30);
        camera.setTranslateY(10);
        camera.getTransforms().add(new Translate(0, 0, -150));

        group = new Group();

        Axes axes = new Axes();
        axes.setHeight(1000);
        axes.setRadius(1);
        final Group grid = Grid.createGrid(1000, 25);

        group.getChildren().addAll(axes, grid);

        subScene = new SubScene(group, 1280, 720, true, SceneAntialiasing.BALANCED);

        subScene.setFill(Color.rgb(80, 80, 80));
        subScene.setCamera(camera);
    }

    /**
     * Adds a 3D object to the view's group.
     *
     * @param path The file path of the 3D object.
     * @return The added Model3D object.
     * @throws IOException If there is an error loading the 3D object.
     */
    public Model3D addObject(String path) throws IOException {
        model = Importer3D.load(new File(path).toURI().toURL());
        group.getChildren().add(model.getRoot());

        return model;
    }

    /**
     * Retrieves the 3D model associated with the current View object.
     *
     * @return The 3D model as a Model3D object.
     */
    public Model3D getModel() {
        return model;
    }

    /**
     * Retrieves the Camera object associated with the current View object.
     *
     * @return The Camera object.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Retrieves the SubScene object associated with the current View object.
     *
     * @return The SubScene object.
     */
    public SubScene getSubScene() {
        return subScene;
    }

    /**
     * Removes a Model3D object from the view's group.
     *
     * @param model The Model3D object to remove.
     */
    public void removeObject(Model3D model) {
        group.getChildren().remove(model.getRoot());
    }
}
