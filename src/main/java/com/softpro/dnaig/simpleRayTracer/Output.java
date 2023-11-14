package simpleRayTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Output extends JPanel {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private BufferedImage canvas;
    private static Output instance;

    public Output(int width, int height) {
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

    public static Output getOutput(){
        if(instance==null){
            instance = new Output(WIDTH, HEIGHT);
        }
        return instance;
    }

    public static void setPixel(int x, int y, int rgb){
        Output inst = getOutput();
        if(x>WIDTH-1 || y>HEIGHT-1 || x<0 || y<0){
            return;
        }
        inst.canvas.setRGB(x, y, rgb);
        inst.repaint();
    }

    public static void main(String[] args) throws IOException {
        RayTracer r = new RayTracer();
        r.trace();
        Output panel = getOutput();

        JFrame frame = new JFrame("Simple Raytracer");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
