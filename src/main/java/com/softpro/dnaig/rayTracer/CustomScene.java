package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.light.PointLight;
import com.softpro.dnaig.objData.mesh.*;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;
import com.softpro.dnaig.utils.YAMLexporter;
import javafx.scene.paint.Color;

import java.io.File;
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
 */

        //Entity entity2 = new Entity("Test", faceArrayList, 3);

     //   entities.add(ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\quad.obj", 0));
     //   lights.add(new PointLight(new Vector3D(0, 3, -3), new Vector3D(3, 5, 1)));
/*
        Entity entity1 = ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\ObjDateien\\ico.obj", 0);
        Entity entity2 = ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\ObjDateien\\ico.obj", 1);
        Entity entity3 = ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\ObjDateien\\ico.obj", 2);
        Entity entity4  = ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\ObjDateien\\ico.obj", 3);

       // Entity audi = ObjFileReader.createObject("C:\\THU\\src\\main\\java\\com\\softpro\\dnaig\\assets\\objFile\\astonMartin\\astonMartin.obj", 4);
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);







        //lights.add(new PointLight(new Vector3D_RT(15, 15, 15), new Vector3D_RT(50, 50, 50)));
      //  lights.add(new PointLight(new Vector3D(0, 2, -2), new Vector3D(5, 5, 5)));
        //lights.add(new PointLight(new Vector3D_RT(-15, 15, -15), new Vector3D_RT(50, 50, 50)));
        // lights.add(new PointLight(new Vector3D_RT(-15, 15, 15), new Vector3D_RT(50, 50, 50)));


        entity2.scale(0.5);
        entity4.scale(0.25);

        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        for (Entity entity : entities){
            for (Face face1: entity.getFaces()) {
                for (int i = 0; i<face1.getVerticeCount(); i++) {
                    maxX = Math.max(maxX, Math.abs(face1.getVertex(i).getCoordinates().getX()));
                    maxY = Math.max(maxY, Math.abs(face1.getVertex(i).getCoordinates().getY()));
                    maxZ = Math.max(maxZ, Math.abs(face1.getVertex(i).getCoordinates().getZ()));
                }
            }
        }

        double factor = Math.max(Math.max(maxX, maxY), maxZ);




      //  entity.setColor(Color.RED);
     //   entity2.setColor(Color.GREEN);
     //   entity3.setColor(Color.BLUE);
     //   entity4.setColor(Color.YELLOW);
        entity1.setPivot(new Vector3D(.5, 0, .1));
           entity2.setPivot(new Vector3D(0.25, 0.25, 0));
       entity3.setPivot(new Vector3D(-0.25, -0.25, 0));
        entity4.setPivot(new Vector3D(-0.5, 0, -0.1));

        entities.add(ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\ObjDateien\\cone.obj", 0));

        for (Entity entity : entities){
            objects.addAll(entity.getTriangles(1));
        }
*/
        /*
        //  objects.addAll(entity.getTriangles(factor));
     //   objects.addAll(entity2.getTriangles(factor));
      //  objects.addAll(entity3.getTriangles(factor));
      //  objects.addAll(entity4.getTriangles(factor));

        System.out.println(objects.size());


        lights.add(new PointLight(new Vector3D(0, 3, -3), new Vector3D(8, 5, 10)));
        lights.add(new PointLight(new Vector3D(4, 2, -1.5), new Vector3D(6, 2, 1)));

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
    public static CustomScene getScene() throws IOException {
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
            objects.addAll(entity.getTriangles(1));
        }

       // lights.add(new PointLight(new Vector3D(0, 3, -3), new Vector3D(8, 5, 10)));
       // lights.add(new PointLight(new Vector3D(4, 2, -1.5), new Vector3D(13, 2, 1)));
    }

}