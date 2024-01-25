package com.softpro.dnaig;

import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.properties.Properties;
import com.softpro.dnaig.rayTracer.CallbackInterface;
import com.softpro.dnaig.rayTracer.RTRunnable;
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
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.function.Consumer;

public class Output extends Application {

    private volatile boolean threadCancelled = false;

    private Canvas canvas;
    private static Output instance;
    private Stage localStage;

    public Output(){}

    public Output(int width, int height) {
        canvas = new Canvas(width, height);
        drawDefaultBackground();
    }

    private Consumer<Void> callbackWhenRayTracerFinished;
    public void openRayTracer(LinkedList<Properties> propertiesList, Stage primaryStage, Consumer<Void> callbackWhenRayTracerFinished, ApplicationController applicationController) {
        //ToDO:abarbeiten der propertiesListe

        this.callbackWhenRayTracerFinished = callbackWhenRayTracerFinished;

        // check if window is opened for the first time
        if (localStage == null) {
            Output panel = getOutput();
            StackPane root = new StackPane();
            root.getChildren().add(panel.canvas);

            localStage = new Stage();

            localStage.setTitle("DNAIG-RayTracer Output");
            localStage.setScene(new Scene(root));
            localStage.setResizable(false);
        }

        // open/focus RayTracer window
        localStage.show();

        // reset stuff from possible previous runs
        threadCancelled = false;
        resetPixelTest();

        // call RayTracer on new thread
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                RayTracer r = new RayTracer(applicationController);
                try {
                    r.trace();
                } catch (IOException e) {
                    System.out.println("here");
                    throw new RuntimeException(e);
                }
                return null;
            }
        };

        RTRunnable runnable = new RTRunnable(task, this::callbackWhenRTFinished);
        Thread renderThread = new Thread(runnable);
        renderThread.start();
    }

    private void callbackWhenRTFinished(String s) {
        System.out.println("Main finished");
        Platform.runLater(() -> callbackWhenRayTracerFinished.accept(null));
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
        for (int y = 0; y < Config.getInstance().getHEIGHT(); y++) {
            for (int x = 0; x < Config.getInstance().getWIDTH(); x++) {
                pixelWriter.setColor(x, y, Color.BLUE);
            }
        }
    }

    public void clear() {
        drawDefaultBackground();
    }

    public static Output getOutput(){
        if(instance==null){
            instance = new Output(Config.getInstance().getWIDTH(), Config.getInstance().getHEIGHT());
        }
        return instance;
    }

    public Output recreate() {
        instance = new Output(Config.getInstance().getWIDTH(), Config.getInstance().getHEIGHT());
        return instance;
    }

    public boolean isThreadCancelled() {
        return threadCancelled;
    }

    int h = Config.getInstance().getHEIGHT() / Config.getInstance().getTILES();
    int w = Config.getInstance().getWIDTH() / Config.getInstance().getTILES();
    int buffer_test_size = w*h;
    int[][] buffer_test = new int[Config.getInstance().getTHREADS()][buffer_test_size];
    int[] buffer_ptr_test = new int[Config.getInstance().getTHREADS()];
    public synchronized void setPixelTest(int tid, int x, int y, int c, int work) {
        if (x > Config.getInstance().getWIDTH() - 1 || y > Config.getInstance().getHEIGHT() - 1 || x < 0 || y < 0) {
            return;
        }

        int index = x % w + (y % h) * w;

        // add alpha value to color (opacity is always 100%)
        buffer_test[tid][index] = (0xFF << 24) | c;
        buffer_ptr_test[tid]++;

        // if buffer is full, write data out to screen
        if (buffer_ptr_test[tid] >= buffer_test_size) {
            // draw output
            canvas.getGraphicsContext2D().getPixelWriter().setPixels(x - w + 1, y - h + 1, w, h, PixelFormat.getIntArgbInstance(), buffer_test[tid], 0, w);

            // clear buffer
            buffer_test[tid] = new int[buffer_test_size];
            buffer_ptr_test[tid] = 0;

            //System.out.printf("Writing task %d\n", work);
        }
    }

    public void resetPixelTest() {
        buffer_test = new int[Config.getInstance().getTHREADS()][buffer_test_size];
        buffer_ptr_test = new int[Config.getInstance().getTHREADS()];
        canvas.getGraphicsContext2D().clearRect(0, 0, Config.getInstance().getWIDTH(), Config.getInstance().getHEIGHT());
    }

    public void clearPixelTest() {
        buffer_test = null;
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

        RayTracer r = new RayTracer(null);
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
