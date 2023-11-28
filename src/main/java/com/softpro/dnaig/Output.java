package com.softpro.dnaig;

import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.simpleRayTracer.RayTracer;
import com.softpro.dnaig.utils.ColorConverter;
import com.softpro.dnaig.utils.Config;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class Output extends Application {

    public static final int WIDTH = Config.WIDTH;
    public static final int HEIGHT = Config.HEIGHT;

    private Canvas canvas;
    private static Output instance;

    public Output(){}

    public Output(int width, int height) {
        canvas = new Canvas(width, height);
        drawDefaultBackground();
    }

    public static void openRayTracer(LinkedList<Properties> propertiesList) {
        //ToDO:abarbeiten der propertiesListe

        //toDo: open new window
        Output panel = getOutput();

        StackPane root = new StackPane();
        root.getChildren().add(panel.canvas);
        Stage primaryStage = new Stage();

        primaryStage.setTitle("Simple Raytracer");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        RayTracer r = new RayTracer();
        try {
            r.trace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawDefaultBackground() {
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                pixelWriter.setColor(x, y, Color.BLUE);
            }
        }
    }

    public static Output getOutput(){
        if(instance==null){
            instance = new Output(WIDTH, HEIGHT);
        }
        return instance;
    }

    public static void setPixel(int x, int y, int c){

        Color color = ColorConverter.rgbToColorConverter(c);

        Output inst = getOutput();
        if (x > WIDTH - 1 || y > HEIGHT - 1 || x < 0 || y < 0) {
            return;
        }
        inst.canvas.getGraphicsContext2D().getPixelWriter().setColor(x, y, color);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Output panel = getOutput();

        StackPane root = new StackPane();
        root.getChildren().add(panel.canvas);

        primaryStage.setTitle("Simple Raytracer");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        RayTracer r = new RayTracer();
        r.trace();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
