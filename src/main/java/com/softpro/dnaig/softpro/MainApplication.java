package com.softpro.dnaig.softpro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    final int screenres = 1024;
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        VBox root = new VBox();

        Button btnTest = new Button("click Me");
        btnTest.setOnAction(actionEvent -> btnTest.setText("i've been Clicked"));
        root.getChildren().addAll(btnTest);

        stage.setTitle("DNAIG-RayTracer!");

        stage.setScene(new Scene(root, screenres, screenres/16*9));
        stage.show();
    }
//https://github.com/LmaoPewPew/SoftProject.git

    public static void main(String[] args) {
        launch();
    }
}