package com.softpro.dnaig.softpro;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

//https://github.com/LmaoPewPew/SoftProject.git
public class MainApplication extends Application {
    final double screenRes = 1331.2;
    boolean isDarkMode = true;


    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        /*BACKBONE OF THE CODE*/
        VBox root = new VBox();
        Scene scene = new Scene(root, screenRes, screenRes / 16 * 9);

        /*MENUBAR SETTING*/
        MenuBar menuBar = new MenuBar();
        menuBarMethode(scene, menuBar);

        /*Starting-THEMES*/
        //setLightTheme(scene);

        Button btnTest = new Button("click Me");
        btnTest.setOnAction(actionEvent -> btnTest.setText("i've been Clicked"));


        /* ******************************************/
        root.getChildren().addAll(menuBar, btnTest);
        //Filepath: src/main/resources/com/softpro/dnaig/assets/icon.png
        stage.getIcons().add(new Image("https://i.imgur.com/tkYVF4s.jpg"));
        stage.setTitle("DNAIG-RayTracer!");
        stage.setScene(scene);
        stage.show();
    }
    /* **********************METHODE_REGION***********************/

    /**************MENUBAR**************/
    public void menuBarMethode(Scene scene, MenuBar menuBar) {
        //Menu File
        Menu mFile = new Menu("File");
        MenuItem itemFile1 = new MenuItem("File 1");
        MenuItem itemFile2 = new MenuItem("File 2");
        MenuItem itemFile3 = new MenuItem("File 3");

        SeparatorMenuItem fileGroupSeparator = new SeparatorMenuItem();

        mFile.getItems().addAll(itemFile1, itemFile2, fileGroupSeparator, itemFile3);

        //Menu Edit
        Menu mEdit = new Menu("Edit");
        MenuItem theme = new MenuItem("Theme");
        MenuItem itemEdit2 = new MenuItem("Edit 2");

        theme.setOnAction(event -> {
            isDarkMode = !isDarkMode;
            /**
            try {
                if (isDarkMode) {
                    setDarkTheme(scene);
                } else {
                    setLightTheme(scene);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            //*/
        });
        mEdit.getItems().addAll(theme, itemEdit2);

        //Menu Help
        Menu mHelp = new Menu("Help");
        MenuItem itemHelp1 = new MenuItem("Help 1");
        MenuItem itemHelp2 = new MenuItem("Help 2");
        mHelp.getItems().addAll(itemHelp1, itemHelp2);

        //MenuBar Children
        menuBar.getMenus().addAll(mFile, mEdit, mHelp);
    }

    /**************THEME**************/
    private void setDarkTheme(Scene scene) throws MalformedURLException {
        File style = new File("src/main/java/com/softpro/dnaig/softpro/DarkMode.css");
        scene.getStylesheets().add(style.toURI().toURL().toExternalForm());
        /*
            String darkTheme = "-fx-background-color: #333333; -fx-text-fill: white;";
            stage.getScene().getRoot().setStyle(darkTheme);
        */
    }

    private void setLightTheme(Scene scene) throws MalformedURLException {
        File style = new File("src/main/java/com/softpro/dnaig/softpro/LightMode.css");
        scene.getStylesheets().add(style.toURI().toURL().toExternalForm());
        /*
        /*  String lightTheme = "-fx-background-color: white; -fx-text-fill: black;";
            stage.getScene().getRoot().setStyle(lightTheme);
        */
    }

    /**************MAIN**************/
    public static void main(String[] args) {
        launch();
    }
}