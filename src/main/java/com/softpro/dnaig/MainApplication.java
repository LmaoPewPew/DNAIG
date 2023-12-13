package com.softpro.dnaig;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    final double screenRes = 1331.2;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DNAIG.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenRes, screenRes / 16 * 9);
        stage.setTitle("DNAIG-RayTracer");
        URL logoIGM = getClass().getResource("sprites/LOGO.png");
        stage.getIcons().add(new Image(String.valueOf(logoIGM)));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        ApplicationController controller = fxmlLoader.getController();
        scene.setOnKeyReleased(controller::handleKey);

        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}