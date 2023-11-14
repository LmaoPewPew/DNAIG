package simpleRayTracer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Output2 extends Application {/*

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private BufferedImage canvas;
    Image image = new Image("Helo there");
    private static Output2 instance;

    public Output2(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i< canvas.getHeight(); i++){
            for(int j = 0; j<canvas.getWidth(); j++){
                canvas.setRGB(j, i, Color.BLUE.getRGB());
            }
        }
        this.setPreferredSize(new Dimension(width, height));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(canvas, null, null);
    }

    public static Output2 getOutput2(){
        if(instance==null){
            instance = new Output2(WIDTH, HEIGHT);
        }
        return instance;
    }

    public static void setPixel(int x, int y, int rgb){
        Output2 inst = getOutput2();
        if(x>WIDTH-1 || y>HEIGHT-1 || x<0 || y<0){
            return;
        }
        inst.canvas.setRGB(x, y, rgb);
        inst.repaint();
    }

*/
    public static void main(String[] args) {
        /*
        RayTracer r = new RayTracer();
        r.trace();
        Output2 panel = getOutput2();

        JFrame frame = new JFrame("Simple Raytracer");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         */
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Simple Raytracer");
        stage.setResizable(false);
        stage.show();
    }
}
