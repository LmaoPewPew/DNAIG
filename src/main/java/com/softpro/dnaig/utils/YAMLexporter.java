package com.softpro.dnaig.utils;

import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.light.PointLight;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.objData.mesh.Object3D;
import com.softpro.dnaig.rayTracer.Camera;
import com.softpro.dnaig.rayTracer.CustomScene;

import java.io.*;
import java.util.ArrayList;

public class YAMLexporter {

    private static StringBuilder sb;

    public static void exportScene(
            File file,
            ArrayList<Entity> objects,
            ArrayList<Light> lights,
            Camera camera
    ) {
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

    public static void importScene(File file) {
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<Light> lights = new ArrayList<>();
        Camera camera = new Camera();

        // Defines mode of reading
        enum State {
            MODELS,
            POINTLIGHTS,
            CAMERA,
            NONE
        }

        State state = State.NONE;

        String filePath = "";
        Vector3D position = null;
        Vector3D rotation = null;
        double scale = 0;
        Vector3D color = null;
        Vector3D lookAt = null;
        Vector3D up = null;
        int width = 0, height = 0;
        double fov = 0;

        int id = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                // Change mode depending on type in YAML file
                if (line.contains("models:")) {
                    state = State.MODELS;
                } else if (line.contains("camera:")) {
                    state = State.CAMERA;
                } else if (line.contains("pointLights:")) {
                    state = State.POINTLIGHTS;
                }

                switch (state) {
                    // Extract models
                    case MODELS -> {
                        if (line.contains("filePath:")) {
                            filePath = line.split("filePath:")[1].replaceAll("\"", "").trim();
                            System.out.println(filePath);
                        } else if (line.contains("position:")) {
                            position = extractVector(line, "position:");
                        } else if (line.contains("rotation:")) {
                            rotation = extractVector(line, "rotation:");
                        } else if (line.contains("scale:")) {
                            scale = Double.parseDouble(line.split(":")[1].trim());
                        } else if (line.contains("color:")) {
                            color = extractVector(line, "color:");
                        }
                        if (!filePath.isEmpty() && position != null && rotation != null && scale != 0) {
                            Entity entity = null;
                            try {
                                entity = ObjFileReader.createObject(filePath, id++);
                                entity.setPivot(position);
                                entity.setOrient(rotation);
                                entity.scale(scale);
                                entities.add(entity);
                            } catch (Exception e) {
                                System.out.println("Error while reading object file");
                            }
                            finally {
                                filePath = "";
                                position = null;
                                rotation = null;
                                scale = 0;
                            }
                        }
                    }
                    // Extract camera
                    case CAMERA -> {
                        if (line.contains("lookAt:")) {
                            lookAt = extractVector(line, "lookAt:");
                        } else if (line.contains("up:")) {
                            up = extractVector(line, "up:");
                        } else if (line.contains("width:")) {
                            width = Integer.parseInt(line.split(":")[1].trim());
                        } else if (line.contains("height:")) {
                            height = Integer.parseInt(line.split(":")[1].trim());
                        } else if (line.contains("fov:")) {
                            fov = Double.parseDouble(line.split(":")[1].trim());
                        }
                        if (lookAt != null && up != null && width != 0 && height != 0 && fov != 0) {

                        }
                    }
                    // Extract point lights
                    case POINTLIGHTS -> {
                        if (line.contains("position:")) {
                            position = extractVector(line, "position:");
                        } else if (line.contains("Ke:")) {
                            color = extractVector(line, "Ke:");
                        }
                        if (position != null && color != null) {
                            lights.add(new PointLight(position, color));
                            position = null;
                            color = null;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error while reading YAML file");
        }finally {
            try {
                CustomScene.getScene().setScene(entities, lights, camera);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Extracts a vector from a line in the YAML file
     *
     * @param line   Line to extract vector from
     * @param keyword Keyword to search for
     * @return Vector3D
     */
    private static Vector3D extractVector(String line, String keyword) {
        Vector3D vec;
        String pos = line.split(keyword)[1].replaceAll("[{}]", "").trim();
        String[] posArr = pos.split(",");

        vec = new Vector3D(
                Double.parseDouble(posArr[0].split(":")[1].trim()),
                Double.parseDouble(posArr[1].split(":")[1].trim()),
                Double.parseDouble(posArr[2].split(":")[1].trim())
        );
        return vec;
    }
}
