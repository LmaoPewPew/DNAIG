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

    public static void importScene(File file, ArrayList<Entity> entities, ArrayList<Light> lights, Camera camera) {
        entities.clear();
        lights.clear();
        // Camera camera = new Camera();

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
                        } else if (line.contains("position:")) {
                            position = extractVector(reader);
                        } else if (line.contains("rotation:")) {
                            rotation = extractVector(reader);
                        } else if (line.contains("scale:")) {
                            scale = Double.parseDouble(line.split(":")[1].trim());
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
                        if (line.contains("position:")) {
                            position = extractVector(reader);
                        } else if (line.contains("lookAt:")) {
                            lookAt = extractVector(reader);
                        } else if (line.contains("upVec:")) {
                            up = extractVector(reader);
                        } else if (line.contains("width:")) {
                            width = Integer.parseInt(line.split(":")[1].trim());
                        } else if (line.contains("height:")) {
                            height = Integer.parseInt(line.split(":")[1].trim());
                        } else if (line.contains("fieldOfView:")) {
                            fov = Double.parseDouble(line.split(":")[1].trim());
                        }
                        if (position != null && lookAt != null && up != null && width != 0 && height != 0 && fov != 0) {
                            // camera = new Camera(position, lookAt, up, fov, width, height);
                        }
                    }
                    // Extract point lights
                    case POINTLIGHTS -> {
                        if (line.contains("position:")) {
                            position = extractVector(reader);
                        } else if (line.contains("Ke:")) {
                            color = extractVector(reader);
                        }
                        if (position != null && color != null) {
                            System.out.println("Position: " + position + " Color: " + color);
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

    private static Vector3D extractVector(BufferedReader reader) throws IOException {
        Vector3D vec;
        String line;
        double x = 0, y = 0, z = 0;
        for (int i = 0; i < 3; i++) {
            line = reader.readLine();
            if (line.contains("x:") || line.contains("r:")) {
                x = Double.parseDouble(line.split(":")[1].trim());
            } else if (line.contains("y:") || line.contains("g:")) {
                y = Double.parseDouble(line.split(":")[1].trim());
            } else if (line.contains("z:") || line.contains("b:")) {
                z = Double.parseDouble(line.split(":")[1].trim());
            }
        }
        vec = new Vector3D(x, y, z);
        return vec;
    }
}
