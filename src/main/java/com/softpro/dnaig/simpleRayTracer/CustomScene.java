package com.softpro.dnaig.simpleRayTracer;


import com.softpro.dnaig.objData.Entity;
import com.softpro.dnaig.objData.Face;
import com.softpro.dnaig.objData.Material;
import com.softpro.dnaig.objData.Vertex;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;

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
        objects.add(new Sphere(2, new Vector3D_RT(), Color.WHITE));

        objects.add(new Sphere(3, new Vector3D_RT(10, 0, 0), Color.RED));
        objects.add(new Sphere(4, new Vector3D_RT(-3, 3, 4), Color.BLUE));
        objects.add(new Sphere(3, new Vector3D_RT(2, -4, 7), Color.YELLOW));

/*

        objects.add(CoordinateSystem.getCoordinateSystem());


*/
        objects.add(new Triangle(new Vector3D_RT(-2, -2, 1), new Vector3D_RT(1, 2, 0), new Vector3D_RT(2, -1, 1), Color.BLUE));
        objects.add(new Triangle(new Vector3D_RT(1, -2, 1), new Vector3D_RT(2, 2, 0), new Vector3D_RT(-1, -1, -1), Color.BLUE));
        objects.add(new Triangle(new Vector3D_RT(-1, -1, -2), new Vector3D_RT(2, 1, 0), new Vector3D_RT(1, -1, -1), Color.BLUE));
        objects.add(new Triangle(new Vector3D_RT(1, -3, -2), new Vector3D_RT(2, 1, 0), new Vector3D_RT(1, -1, 1), Color.BLUE));


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
        Entity entity2 = new Entity("Test", faceArrayList, 3);
        Entity entity = ObjFileReader.createObject("C:\\Users\\leonv\\Desktop\\tri.obj");
        //entity.scale(10);
        entities.add(entity);
        //lights.add(new PointLight(new Vector3D_RT(15, 15, 15), new Vector3D_RT(50, 50, 50)));
        lights.add(new PointLight(new Vector3D_RT(0, 2, -2), new Vector3D_RT(2, 1, 5)));
        //lights.add(new PointLight(new Vector3D_RT(-15, 15, -15), new Vector3D_RT(50, 50, 50)));
       // lights.add(new PointLight(new Vector3D_RT(-15, 15, 15), new Vector3D_RT(50, 50, 50)));
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
