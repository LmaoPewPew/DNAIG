package com.softpro.dnaig;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    final double screenRes = 1331.2;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DNAIG.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenRes, screenRes/16*9);
        stage.setTitle("DNAIG-RayTracer");
        stage.getIcons().add(new Image("https://i.imgur.com/tkYVF4s.jpg"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}