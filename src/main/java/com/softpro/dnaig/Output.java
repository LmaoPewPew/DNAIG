package com.softpro.dnaig;

import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.rayTracer.Camera;
import com.softpro.dnaig.rayTracer.CustomScene;
import com.softpro.dnaig.rayTracer.RayTracer;
import com.softpro.dnaig.utils.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Output extends Application {

    public static final int WIDTH = Config.WIDTH;
    public static final int HEIGHT = Config.HEIGHT;
    private static final int BUFFER_SIZE = Config.HEIGHT / 5;

    private int[] buffer = new int[BUFFER_SIZE];
    private int buffer_ptr = 0;
    private volatile boolean threadCancelled = false;

    private Canvas canvas;
    private static Output instance;
    private Stage localStage;

    public Output(){}

    public Output(int width, int height) {
        canvas = new Canvas(width, height);
        drawDefaultBackground();
    }

    public void openRayTracer(LinkedList<Properties> propertiesList, Stage primaryStage, Consumer<Void> callbackWhenRayTracerFinished) {
        //ToDO:abarbeiten der propertiesListe

        // check if window is opened for the first time
        if (localStage == null) {
            Output panel = getOutput();
            StackPane root = new StackPane();
            root.getChildren().add(panel.canvas);

            localStage = new Stage();

            localStage.setTitle("Simple Raytracer");
            localStage.setScene(new Scene(root));
            localStage.setResizable(false);
        }

        // open/focus RayTracer window
        localStage.show();

        // reset stuff from possible previous runs
        threadCancelled = false;
        buffer = new int[BUFFER_SIZE];

        // call RayTracer on new thread
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                RayTracer r = new RayTracer();
                try {
                    r.trace();

                    // callback to ApplicationController when tracing is finished or cancelled
                    Platform.runLater(() -> callbackWhenRayTracerFinished.accept(null));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        Thread renderThread = new Thread(task);
        renderThread.setPriority(2);
        renderThread.start();
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
        // set shared variable to tell RT to cancel tracing
        threadCancelled = true;
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

    public boolean isThreadCancelled() {
        return threadCancelled;
    }


    public void setPixel(int x, int y, int c){
        if (x > WIDTH - 1 || y > HEIGHT - 1 || x < 0 || y < 0) {
            return;
        }

        // add alpha value to color (opacity is always 100%)
        buffer[buffer_ptr++] = (0xFF<<24) | c;

        // if buffer is full, write data out to screen
        if (buffer_ptr >= BUFFER_SIZE) {
            canvas.getGraphicsContext2D().getPixelWriter().setPixels(x, y-BUFFER_SIZE+1, 1, BUFFER_SIZE, PixelFormat.getIntArgbInstance(), buffer, 0, 1);

            // reset buffer
            buffer = new int[BUFFER_SIZE];
            buffer_ptr = 0;
        }

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

    public void setScene(ArrayList<Entity> entityList, ArrayList<Light> lightList, Camera camera) {
        CustomScene.getScene().setScene(entityList, lightList, camera);
    }
}
