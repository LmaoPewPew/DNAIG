package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.light.PointLight;
import com.softpro.dnaig.objData.mesh.*;
import com.softpro.dnaig.objData.presets.Plane;
import com.softpro.dnaig.utils.Config;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;
import com.softpro.dnaig.utils.YAMLexporter;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CustomScene {
    public static CustomScene scene;
    public ArrayList<Object3D> objects = new ArrayList<Object3D>();
    public ArrayList<Light> lights = new ArrayList<Light>();
    public ArrayList<Entity> entities = new ArrayList<>();
    public Camera camera = new Camera();
    public CustomScene() {
        lights.add(new PointLight(new Vector3D(2, 3, 1), new Vector3D(8, 8, 8)));
        camera = new Camera(new Vector3D(1, 0.5, 1), new Vector3D(), Config.WIDTH, Config.HEIGHT);

        objects.add(new Plane(new Vector3D(-1, -4, -1), new Vector3D(1, -4, -1), new Vector3D(-1, -4, 1), Color.LIGHTGRAY));
        objects.add(new Plane(new Vector3D(-1, -1, -4), new Vector3D(1, -1, -4), new Vector3D(-1, 1, -4), Color.LIGHTGRAY));

        Entity e1 = null;
        try {
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/cube/cube.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/icosphere/ico.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/quadCube/cube4.obj", 1);
            e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/starDestroyer/stardestroyer.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/astonMartin/astonMartin.obj", 1);
        }catch (java.io.IOException e) {
            System.out.println("ups");
        }
        e1.setOrient(new Vector3D(0, 90, 0));
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        for (Face face1: e1.getFaces()) {
            for (int i = 0; i<face1.getVerticeCount(); i++) {
                maxX = Math.max(maxX, Math.abs(face1.getVertex(i).getCoordinates().getX()));
                maxY = Math.max(maxY, Math.abs(face1.getVertex(i).getCoordinates().getY()));
                maxZ = Math.max(maxZ, Math.abs(face1.getVertex(i).getCoordinates().getZ()));
            }
        }

        double factor = Math.max(Math.max(maxX, maxY), maxZ);

        objects.addAll(e1.getTriangles(factor));

    }

    /**
     * Exports the scene to a specified file.
     * @param file The file to export the scene to.
     */
    public void yamlExport(File file){
        YAMLexporter.exportScene(file, entities, lights, RayTracer.camera);
    }

    /**
     * Imports a scene from a specified file.
     * @param file The file to import the scene from.
     */
    public void yamlImport(File file){
        YAMLexporter.importScene(file, entities, lights, RayTracer.camera);
    }

    /**
     * Returns the scene.
     * @return The scene.
     * @throws IOException If the scene is null.
     */
    public static CustomScene getScene() {
        if(scene==null){
            scene = new CustomScene();
        }
        return scene;
    }

    public void addEntity(Entity entity, Vector3D position){
        entities.add(entity);
    }

    /**
     * Sets the scene to a specified ArrayList of Entities, ArrayList of Lights and a Camera.
     *
     * @param entities List of Entities in the scene.
     * @param lights List of Lights in the scene.
     * @param camera Camera in the scene.
     */
    public void setScene(ArrayList<Entity> entities, ArrayList<Light> lights, Camera camera) {
        this.entities = entities;
        this.lights = lights;
        this.camera = camera;
        lights.forEach(light -> {
                    System.out.println(light.getPosition());
            System.out.println(light.getIntensity());
                });
       // RayTracer.camera = camera;

        this.objects = new ArrayList<>();

        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        for (Entity entity : this.entities){
            for (Face face1: entity.getFaces()) {
                for (int i = 0; i<face1.getVerticeCount(); i++) {
                    maxX = Math.max(maxX, Math.abs(face1.getVertex(i).getCoordinates().getX()));
                    maxY = Math.max(maxY, Math.abs(face1.getVertex(i).getCoordinates().getY()));
                    maxZ = Math.max(maxZ, Math.abs(face1.getVertex(i).getCoordinates().getZ()));
                }
            }
        }

        double factor = Math.max(Math.max(maxX, maxY), maxZ);

        for (Entity entity : entities){
            objects.addAll(entity.getTriangles(factor));
        }

       // lights.add(new PointLight(new Vector3D(0, 3, -3), new Vector3D(8, 5, 10)));
       // lights.add(new PointLight(new Vector3D(4, 2, -1.5), new Vector3D(13, 2, 1)));
    }

}