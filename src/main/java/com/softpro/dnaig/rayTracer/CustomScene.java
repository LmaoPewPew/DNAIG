package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.light.PointLight;
import com.softpro.dnaig.objData.mesh.*;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;
import com.softpro.dnaig.utils.YAMLexporter;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class CustomScene {
    public static CustomScene scene;
    ArrayList<Object3D> objects = new ArrayList<Object3D>();
    public ArrayList<Light> lights = new ArrayList<Light>();
    public ArrayList<Entity> entities = new ArrayList<>();

    public CustomScene() throws IOException {

/*
        objects.add(new Sphere(5, new Vector3D(), Color.GREEN));
        objects.add(new Sphere(3, new Vector3D(10, 0, 0), Color.RED));
        objects.add(new Sphere(4, new Vector3D(-3, 3, 4), Color.BLUE));
        objects.add(new Sphere(3, new Vector3D(2, -4, 7), Color.YELLOW));



 */
        /*
        objects.add(CoordinateSystem.getCoordinateSystem());


        objects.add(new Triangle(new Vector3D(-10, -10, 10), new Vector3D(0, 10, 0), new Vector3D(10, -10, 10), Color.BLUE));
        objects.add(new Triangle(new Vector3D(-10, -10, 10), new Vector3D(0, 10, 0), new Vector3D(-10, -10, -10), Color.BLUE));
        objects.add(new Triangle(new Vector3D(-10, -10, -10), new Vector3D(0, 10, 0), new Vector3D(10, -10, -10), Color.BLUE));
        objects.add(new Triangle(new Vector3D(10, -10, -10), new Vector3D(0, 10, 0), new Vector3D(10, -10, 10), Color.BLUE));




         */
        Vector3D v1 = new Vector3D(-1,-1,1);
        Vector3D v2 = new Vector3D(0,1,0);
        Vector3D v3 = new Vector3D(3,-1,2);
        Vertex vertex1 = new Vertex(v1, v1, v1);
        Vertex vertex2 = new Vertex(v2, v2, v2);
        Vertex vertex3 = new Vertex(v3, v3, v3);
        Vertex[] vertices = {vertex1, vertex2, vertex3};
        Face face = new Face(vertices, new Material(), 0);
        ArrayList<Face> faceArrayList = new ArrayList<>();
        faceArrayList.add(face);

        Entity entity = ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\quad.obj", 0);
        entities.add(entity);
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        for (Face face1: entity.getFaces()) {
            for (int i = 0; i<face1.getVerticeCount(); i++) {
                maxX = Math.max(maxX, Math.abs(face1.getVertex(i).getCoordinates().getX()));
                maxY = Math.max(maxY, Math.abs(face1.getVertex(i).getCoordinates().getY()));
                maxZ = Math.max(maxZ, Math.abs(face1.getVertex(i).getCoordinates().getZ()));
            }
        }
        double factor = Math.max(Math.max(maxX, maxY), maxZ);

        entity.forEach(System.out::println);
        entity.setColor(Color.RED);


        objects.addAll(entity.getTriangles(factor));
        System.out.println(objects.size());


        lights.add(new PointLight(new Vector3D(0, 3, -3), new Vector3D(8, 5, 10)));
        lights.add(new PointLight(new Vector3D(4, 2, -1.5), new Vector3D(13, 2, 1)));


        YAMLexporter.initExporter();
        YAMLexporter.append(RayTracer.camera.yamlString());
        entities.forEach(ent -> YAMLexporter.append("\tObjects:\n" + ent.yamlString()));
        lights.forEach(light -> YAMLexporter.append(light.yamlString()));

        YAMLexporter.export("scene", "C:\\Users\\leonv\\");

    }

    public static CustomScene getScene() throws IOException {
        if(scene==null){
            scene = new CustomScene();
        }
        return scene;
    }

    public void addEntity(Entity entity, Vector3D position){
        entities.add(entity);
    }
}