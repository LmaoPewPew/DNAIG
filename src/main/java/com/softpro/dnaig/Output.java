package com.softpro.dnaig;

import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.rayTracer.RayTracer;
import com.softpro.dnaig.utils.ColorConverter;
import com.softpro.dnaig.utils.Config;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    Thread renderThread;
    public void openRayTracer(LinkedList<Properties> propertiesList) {
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

    private void repaint() {
        System.out.println("Repaint");
        Canvas newCanvas = new Canvas(canvas.getWidth(), canvas.getHeight());
        Image image = canvas.snapshot(null, null);

        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = newCanvas.getGraphicsContext2D().getPixelWriter();

        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
            }
        }
        getOutput().canvas = newCanvas;
    }

    private void update() {
        System.out.println("Update");
    }

    public void cancelRayTracer(){
        try {
            renderThread.join();
        } catch (InterruptedException e) {
            System.out.println("RenderThread was interrupted");
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

    public void clear() {
        drawDefaultBackground();
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

    public static void dispose(){
        Output inst = getOutput();

    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Output panel = getOutput();

        StackPane root = new StackPane();
        root.getChildren().add(panel.canvas);

        primaryStage.setTitle("Rendered Scene");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        URL logoIGM = getClass().getResource("sprites/LOGO.png");
        primaryStage.getIcons().add(new Image(String.valueOf(logoIGM)));

        primaryStage.show();

        RayTracer r = new RayTracer();
        r.trace();
    }

    public void exportImage(File file) {
        if(file==null){
            return;
        }
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        try {
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null), extension, file);
            System.out.println(extension);
            System.out.println(file.getPath());
        } catch (IOException ignored) {
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
