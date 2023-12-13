package com.softpro.dnaig.utils;

import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.objData.mesh.Object3D;
import com.softpro.dnaig.rayTracer.Camera;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class YAMLexporter {

    private static StringBuilder sb;

    public static void exportScene(
            File file,
            ArrayList<Entity> objects,
            ArrayList<Light> lights,
            Camera camera
    ){
        sb = new StringBuilder();

        sb.append("title: DNAIG Raytracer\n");

        sb.append("models:\n");
        objects.forEach(object -> sb.append(object.toYaml()));

        sb.append("pointLights:\n");
        lights.forEach(light -> sb.append(light.toYaml()));

        sb.append("camera:\n");
        sb.append(camera.toYaml());

        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while writing YAML file");
        }
    }
}
