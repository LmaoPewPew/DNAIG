package com.dnaig.dnaig.utils;

import com.dnaig.dnaig.objData.Entity;
import com.dnaig.dnaig.objData.Face;
import com.dnaig.dnaig.objData.Material;
import com.dnaig.dnaig.objData.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObjFileReader {

    public static Entity createObject(String path) throws IOException {
        ArrayList<Vector3D> vertexList = new ArrayList<>();
        ArrayList<Vector3D> vertexNormalList = new ArrayList<>();
        ArrayList<Vector3D> vertexTextureList = new ArrayList<>();

        ArrayList<Face> faces = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(path));

        String line, name = "";
        while((line = br.readLine()) != null){

            line = line.trim();
            // removes multiple whitespaces
            line = line.replaceAll("^ +| +$|( )+", "$1");

            String[] tokens = line.split(" ");

            /*
             * Vertex
             * Parses all 3 coordinates into floats and stores in 3D Vector
             */
            switch (tokens[0]) {
                case "v" -> {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    float z = Float.parseFloat(tokens[3]);

                    Vector3D vector3d = new Vector3D(x, y, z);
                    vertexList.add(vector3d);

                    //System.out.println("x: " + x + " y: " + y + " z: " + z);
                }


                /*
                 * Vertex normal
                 * Parses all 3 coordinates into floats and stores in 3D Vector
                 */
                case "vn" -> {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    float z = Float.parseFloat(tokens[3]);

                    Vector3D vector3d = new Vector3D(x, y, z);
                    vertexNormalList.add(vector3d);
                }


                /*
                 * Vertex texture coordinates
                 * Parses both u and v coordinates into floats and stores in 3D Vector
                 * -> (u, v, 0)
                 */
                case "vt" -> {
                    float u = Float.parseFloat(tokens[1]);
                    float v = Float.parseFloat(tokens[2]);

                    Vector3D vector3d = new Vector3D(u, v, 0f);
                    vertexTextureList.add(vector3d);
                }


                /*
                 * Face
                 * Reads arbitrary amount of vertex-indexes
                 * structure: f v/vt/vn v/vt/vn v/vt/vn v/vt/vn
                 */
                case "f" -> {

                    // int[numberOfVerticesPerFace][indexOfVertexType]
                    String[][] indexSeparation = new String[tokens.length - 1][];
                    int[][] indexes = new int[tokens.length - 1][];

                    // for each vertex per face the indexes for the vertices are split
                    // additionally for each iteration the array for the indexes is created with the corresponding size
                    for (int i = 1; i < tokens.length; i++) {
                        indexSeparation[i - 1] = tokens[i].split("/");
                        indexes[i - 1] = new int[indexSeparation[i - 1].length];
                    }

                    // each index is stored per face
                    for (int i = 0; i < indexSeparation.length; i++) {
                        for (int j = 0; j < indexSeparation[i].length; j++) {
                            indexes[i][j] = Integer.parseInt(indexSeparation[i][j]);
                        }
                    }

                    // amount of vertices
                    Vertex[] vertices = new Vertex[indexes.length];
                    int i = 0;
                    for (int[] idx : indexes) {
                        // first index is for the regular vertex
                        Vector3D v = vertexList.get(idx[0] - 1);

                        /*
                         * the index 0 can be used due to no definition within the obj-file
                         * -> 0 doesn't exist as index access
                         * second index is for the texture vertex
                         * third index is for the normal vertex
                         */
                        Vector3D vt = idx[1] == 0 ? new Vector3D() : vertexTextureList.get(idx[1] - 1);
                        Vector3D vn = idx[2] == 0 ? new Vector3D() : vertexNormalList.get(idx[2] - 1);

                        vertices[i++] = new Vertex(
                                v,
                                vt,
                                vn
                        );
                    }
                    Face face = new Face(vertices);
                    faces.add(face);
                }

                /*
                 * object name
                 * Reads the object name if existing
                 */
                case "o" -> name = tokens[1];
            }
        }
        br.close();

        Entity entity = new Entity(name, faces, null, vertexList.size(), false);
        System.out.println(entity);

        return entity;
    }

    public static ArrayList<Material> createMaterial(String path) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        ArrayList<Material> materials = new ArrayList<>();

        Material material = null;

        String line;
        while((line = bufferedReader.readLine()) != null){

            line = line.trim();
            line = line.replaceAll("^ +| +$|( )+", "$1");

            String[] tokens = line.split(" ");
            if(tokens.length > 1){
                String command = tokens[0];

                if(command.equals("newmtl")){
                    material = new Material();
                    material.setName(tokens[1]);
                }
                else if(material != null){
                    switch (tokens[0]){
                        case "Ka" ->{
                            material.setKa(parseToRGB(tokens));
                        }
                        case "Kd" ->{
                            material.setKd(parseToRGB(tokens));
                        }
                        case "Ks" ->{
                            material.setKs(parseToRGB(tokens));
                        }
                        case "Ke" ->{
                            material.setKe(parseToRGB(tokens));
                        }
                        case "Ns" ->{
                            material.setNs(Float.parseFloat(tokens[1]));
                        }
                        case "Ni" ->{
                            material.setNi(Float.parseFloat(tokens[1]));
                        }
                        case "d" ->{
                            material.setD(Float.parseFloat(tokens[1]));
                        }
                        case "illum" ->{
                            material.setIllum(Integer.parseInt(tokens[1]));
                        }

                    }
                }
            }
            else{
                if(material != null) {
                    materials.add(material);
                    material = null;
                }
            }
        }
        materials.add(material);
        return materials;
    }

    private static float[] parseToRGB(String[] token){
        float[] rgb = new float[token.length - 1];
        for (int i = 1; i < token.length; i++) {
            rgb[i - 1] = Float.parseFloat(token[i]);
        }
        return rgb;
    }
    public static void main(String[] args) {
        Entity entity = null;
        ArrayList<Material> materials;
        try {
            entity = createObject("C:\\Users\\Leon\\Documents\\ObjFileImporter\\src\\obj\\star-destroyer\\stardestroyer.obj");
            materials = createMaterial("C:\\Users\\Leon\\Documents\\ObjFileImporter\\src\\obj\\star-destroyer\\stardestroyer.mtl");
            materials.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
