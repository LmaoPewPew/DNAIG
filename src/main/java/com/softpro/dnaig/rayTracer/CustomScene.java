package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.light.PointLight;
import com.softpro.dnaig.objData.mesh.*;
import com.softpro.dnaig.objData.presets.Plane;
import com.softpro.dnaig.utils.*;
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
    public OctreeCell root;

    public CustomScene() {

/*
        lights.add(new PointLight(new Vector3D(2, 3, 2), new Vector3D(8, 8, 8)));
        camera = new Camera(new Vector3D(2, 2, 2), new Vector3D(), Config.WIDTH, Config.HEIGHT);


        objects.add(new Plane(new Vector3D(-1, -4, -1), new Vector3D(1, -4, -1), new Vector3D(-1, -4, 1), Color.LIGHTGRAY));
        objects.add(new Plane(new Vector3D(-1, -1, -4), new Vector3D(1, -1, -4), new Vector3D(-1, 1, -4), Color.LIGHTGRAY));

        Entity e1 = null;
        try {
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/porsche/porsche.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/cube/cube.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/icosphere/ico.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/quadCube/cube4.obj", 1);
            //e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/starDestroyer/stardestroyer.obj", 1);
            e1 = ObjFileReader.createObject("C:/Users/silas/IdeaProjects/SoftProject/src/main/java/com/softpro/dnaig/assets/objFile/astonMartin/astonMartin.obj", 1);
        }catch (java.io.IOException e) {
            System.out.println("ups");
        }
        //e1.setOrient(new Vector3D(0, 90, 0));
        entities.add(e1);
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        e1.scale(0.0005);
        ArrayList<Triangle> triangles= new ArrayList<>();
        triangles.addAll(e1.getTriangles(1));
        for (Triangle t:triangles) {
            maxX = Math.max(maxX, t.getBoundingBox().getMaxVec().getX());
            maxY = Math.max(maxY, t.getBoundingBox().getMaxVec().getY());
            maxZ = Math.max(maxZ, t.getBoundingBox().getMaxVec().getZ());
            minX = Math.min(minX, t.getBoundingBox().getMinVec().getX());
            minY = Math.min(minY, t.getBoundingBox().getMinVec().getY());
            minZ = Math.min(minZ, t.getBoundingBox().getMinVec().getZ());
        }

        double factor = Math.max(Math.max(maxX, maxY), maxZ);

        //objects.addAll(e1.getTriangles(factor));

        root = new OctreeCell(new BoundingBox(new Vector3D(maxX, maxY, maxZ), new Vector3D(minX, minY, minZ)), triangles, 3);
        root.createTree(0);

 */


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

        ArrayList<Triangle> triangles = new ArrayList<>();
        for (Entity entity : entities){
            //objects.addAll(entity.getTriangles(1));
            triangles.addAll(entity.getTriangles(1));
        }
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        for (Triangle t:triangles) {
            maxX = Math.max(maxX, t.getBoundingBox().getMaxVec().getX());
            maxY = Math.max(maxY, t.getBoundingBox().getMaxVec().getY());
            maxZ = Math.max(maxZ, t.getBoundingBox().getMaxVec().getZ());
            minX = Math.min(minX, t.getBoundingBox().getMinVec().getX());
            minY = Math.min(minY, t.getBoundingBox().getMinVec().getY());
            minZ = Math.min(minZ, t.getBoundingBox().getMinVec().getZ());
        }

        /*
        if(Math.abs(camera.getEye().getY()-minY) < Math.abs(camera.getEye().getY()-maxY)){
            objects.add(new Plane(new Vector3D(-1, maxY+Math.abs(maxY), -1), new Vector3D(1, maxY+Math.abs(maxY), -1), new Vector3D(-1, maxY+Math.abs(maxY), 1),  Color.LIGHTGRAY));
        } else {
            objects.add(new Plane(new Vector3D(-1, minY-Math.abs(minY), -1), new Vector3D(1, minY-Math.abs(minY), -1), new Vector3D(-1, minY-Math.abs(minY), 1),  Color.LIGHTGRAY));
        }

         */
        root = new OctreeCell(new BoundingBox(new Vector3D(maxX, maxY, maxZ), new Vector3D(minX, minY, minZ)), triangles, 3);
        root.createTree(0);


    }

}