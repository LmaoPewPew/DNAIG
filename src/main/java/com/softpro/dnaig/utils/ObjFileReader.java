package com.softpro.dnaig.utils;

import com.softpro.dnaig.objData.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The ObjFileReader class is responsible for parsing a Wavefront OBJ file
 * and converting its data into an Entity object, which can be used for 3D rendering.
 */
public class ObjFileReader {

    /**
     * Creates an Entity object from a Wavefront OBJ file.
     *
     * @param path The file path to the Wavefront OBJ file.
     * @return An Entity object representing the 3D model defined in the OBJ file.
     * @throws IOException If there is an error reading the file
     */
    public static Entity createObject(String path) throws IOException {

        // ArrayLists containing all vertex data.
        ArrayList<Vector3D> vertexList = new ArrayList<>();
        ArrayList<Vector3D> vertexNormalList = new ArrayList<>();
        ArrayList<Vector3D> vertexTextureList = new ArrayList<>();

        // ArrayList containing all face data.
        ArrayList<Face> faceList = new ArrayList<>();


        // HashMap containing all Materials accessed via the material name.
        HashMap<String, Material> materialsMap = new HashMap<>();

        // String containing the path to the Material.
        String mtlPath;

        // String containing the object name.
        String objectName = "";

        // Material containing the currently chosen material of the OBJ file.
        Material material = null;

        // Integer containing the smoothing group setting.
        int smoothingGroup = 0;

        // BufferedReader to read in the lines of the OBJ file.
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;

        while ((line = br.readLine()) != null) {
            // Remove unnecessary whitespaces.
            line = line.trim();
            // Removes multiple whitespaces.
            line = line.replaceAll("^ +| +$|( )+", "$1");

            /*
            Tokenizing the current line.
            tokens[0] always contains the current token, unless the line is empty.
            e.g.:
            line = v 2/3/4 1/2/3 3/2/1
            -> tokens = {v, 2/3/4, 1/2/3, 3/2/1}
             */
            String[] tokens = line.split(" ");

            // tokens[0] is used to process the line
            switch (tokens[0]) {
                /*
                 Vertex:
                 Parses all 3 coordinates into floats and stores in 3D Vector.
                 */
                case "v" -> {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    float z = Float.parseFloat(tokens[3]);

                    vertexList.add(new Vector3D(x, y, z));
                }

                /*
                 Vertex normal:
                 Parses all 3 coordinates into floats and stores in 3D Vector.
                 */
                case "vn" -> {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    float z = Float.parseFloat(tokens[3]);

                    vertexNormalList.add(new Vector3D(x, y, z));
                }

                /*
                 Vertex texture:
                 Parses both u and v coordinates into floats and stores in 3D Vector.

                 The vertex texture is only providing information for coordinates u and v,
                 which are stored in a 3D Vector, without using the last coordinate.

                 Vector3D(u, v, 0)
                 */
                case "vt" -> {
                    float u = Float.parseFloat(tokens[1]);
                    float v = Float.parseFloat(tokens[2]);

                    vertexTextureList.add(new Vector3D(u, v, 0.0f));
                }

                /*
                 Face:
                 Parses the amount of vertices into an array of vertices.

                 The face definition vary a lot between different applications and versions for the Wavefront OBJ file.
                 Often times the faces contain 3+ vertices, each vertex contains the index for the normal and texture information.

                 e.g.:
                 f 1/2/3 2/3/4 5/3/1 5/3/2

                 The face in the example contains 4 vertices.
                 Each vertex composites out of the 3 index values.
                     - The first index stands for the vertex (v) in the Wavefront OBJ file.
                     - The second index stands for the vertex texture (vt) in the Wavefront OBJ file.
                     - The third index stands for the vertex normal (vn) in the Wavefront OBJ file.

                 So the structure basically builds up like: f v/vt/vn v/vt/vn v/vt/vn v/vt/vn ...

                 In older files the face has different definitions.
                 Some examples:
                     - f v//vn v//vn v//vn
                     - f v/vt v/vt

                 These variations are respected and covered in this parser.
                 */
                case "f" -> {
                    /*
                    The 2D arrays store the arbitrary amount of vertices per line.
                    The outer array stores the information for each vertex independently.
                    The inner array stores 3 values:
                        - index 0: contains the index for the vertex.
                        - index 1: contains the index for the vertex texture.
                        - index 2: contains the index for the vertex normal.

                    Structure: [Vertices][v/vt/vn]
                     */
                    String[][] indexSeparation = new String[tokens.length - 1][];
                    int[][] indexes = new int[tokens.length - 1][];

                    /*
                    For each vertex per face the indexes for the vertices are split.
                    Also for each iteration the array for the indexes creates a new inner array with the corresponding size.
                     */
                    for (int i = 1; i < tokens.length; i++) {
                        indexSeparation[i - 1] = tokens[i].split("/");
                        indexes[i - 1] = new int[indexSeparation[i - 1].length];
                    }


                    // For each vertex the values per index are parsed into integer values.
                    for (int i = 0; i < indexSeparation.length; i++) {
                        for (int j = 0; j < indexSeparation[i].length; j++) {
                            // For the case of different definitions like: f v//vn, empty indexes are replaced by an ASCII-String 0.
                            String temp = Objects.equals(indexSeparation[i][j], "") ? "0" : indexSeparation[i][j];
                            indexes[i][j] = Integer.parseInt(temp);
                        }
                    }

                    // Array of Vertices is created with the length of the amount of vertices per face.
                    Vertex[] vertices = new Vertex[indexes.length];
                    int i = 0;
                    for (int[] idx : indexes) {

                        /*
                        The first indexed vertex is the regular vertex from the list.
                        The index value needs to be subtracted by 1, because Lists begin with value 0, although the Wavefront OBJ file definition starts with 1.
                         */
                        Vector3D v = vertexList.get(idx[0] - 1);

                        /*
                        The value 0 can be used to create a Null-vector, in the Wavefront OBJ file is no definition of 0 as an index value,
                        so it can be used as an indicator.
                            - The second index is for the vertex texture.
                            - The third index is for the vertex normal.
                         */
                        Vector3D vt = idx[1] == 0 ? new Vector3D() : vertexTextureList.get(idx[1] - 1);
                        Vector3D vn = idx[2] == 0 ? new Vector3D() : vertexNormalList.get(idx[2] - 1);

                        // All vertices are stored in an array.
                        vertices[i++] = new Vertex(v, vt, vn);
                    }

                    // Material, smoothing group mode and array with vertices are passed to the Face constructor.
                    Face face = new Face(vertices, material, smoothingGroup);

                    // Created face is added to the
                    faceList.add(face);
                }

                /*
                 Object name:
                 Reads the object name. If no name is defined, the application will generate a unique name for the entity.
                 */
                case "o" -> objectName = tokens[1];

                /*
                 Material path:
                 Reads the material file location and concatenates it to the existing OBJ file path.
                 */
                case "mtllib" -> {
                    mtlPath = tokens[1];

                    // Backslashes are replaced for compatibility and easier concatenation.
                    path = path.replace("\\", "/");
                    String[] temp = mtlPath.split("/");

                    // MTL file name is stored in a temporary attribute.
                    String mtlTemp = temp[temp.length - 1];

                    // temp array is reassigned with a new array containing the split OBJ file path.
                    temp = path.split("/");
                    // OBJ file name is replaced with the MTL file name.
                    temp[temp.length - 1] = mtlTemp;

                    // StringBuilder is used to concatenate the MTL file path.
                    StringBuilder str = new StringBuilder();
                    for (String t : temp) {
                        str.append(t).append("/");
                    }

                    // Material mapping is created with the MTL file path.
                    materialsMap = createMaterial(str.toString());
                }

                /*
                Smoothing group:
                Reads the smoothing group mode for all following faces.
                 */
                case "s" -> {
                    // Older files containing "off" as an alias for 0.
                    if (tokens[1].equals("off")) smoothingGroup = 0;
                    else smoothingGroup = Integer.parseInt(tokens[1]);
                }

                /*
                Material to use:
                Reads the name of the material applied for alle following faces.
                 */
                case "usemtl" -> material = materialsMap.get(tokens[1]);
            }
        }
        // BufferedReader is closed after finishing parsing the file.
        br.close();

        //An Entity object representing the 3D model defined in the OBJ file.
        return new Entity(objectName, faceList, vertexList.size());
    }

    /**
     * Creates a mapping of material names to Material objects from a MTL file.
     *
     * @param path The file path to the MTL file.
     * @return A HashMap containing material names as keys and Material objects as a values.
     * @throws IOException If there is an error reading the MTL file.
     */
    public static HashMap<String, Material> createMaterial(String path) throws IOException {

        // HashMap containing all Materials accessed via the material name.
        HashMap<String, Material> materials = new HashMap<>();

        // Material containing the currently chosen material of the OBJ file.
        Material material = null;

        // BufferedReader to read in the lines of the OBJ file.
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;

        while ((line = br.readLine()) != null) {

            // Remove unnecessary whitespaces.
            line = line.trim();
            // Removes multiple whitespaces.
            line = line.replaceAll("^ +| +$|( )+", "$1");


            /*
            Tokenizing the current line.
            tokens[0] always contains the current token, unless the line is empty.
            e.g.:
            line = ka 3.0 4.34 5.423
            -> tokens = {ka, 3.0, 4.34, 5.423}
             */
            String[] tokens = line.split(" ");

            // Check if the current line is not empty.
            if (tokens.length > 1) {

                // If current line contains token "newmtl" a new Material object is created.
                if (tokens[0].equals("newmtl")) {
                    if (material != null) {
                        materials.put(material.getMatName(), material);
                    }
                    material = new Material();
                    material.setMatName(tokens[1]);
                }
                // Else the material object gets its properties set.
                else if (material != null) {
                    switch (tokens[0]) {
                        case "Ka" -> material.setKa(parseToRGB(tokens));
                        case "Kd" -> material.setKd(parseToRGB(tokens));
                        case "Ks" -> material.setKs(parseToRGB(tokens));
                        case "Ke" -> material.setKe(parseToRGB(tokens));
                        case "Ns" -> material.setNs(Float.parseFloat(tokens[1]));
                        case "Ni" -> material.setNi(Float.parseFloat(tokens[1]));
                        case "d" -> material.setD(Float.parseFloat(tokens[1]));
                        case "illum" -> material.setIllum(Integer.parseInt(tokens[1]));
                        case "map_Kd" -> material.setMapKd(tokens[1]);
                    }
                }
            }
        }
        // BufferedReader is closed after finishing parsing the file.
        br.close();

        // After the loop terminated, the last material created is stored in the list.
        if (material != null) materials.put(material.getMatName(), material);

        // A HashMap containing material names as keys and Material objects as a values.
        return materials;
    }

    /**
     * Parses an array of tokens containing RGB values and returns them as an array of floats.
     *
     * @param token An array of tokens containing RGB values.
     * @return An array of floats representing RBG values.
     */
    private static float[] parseToRGB(String[] token) {
        float[] rgb = new float[token.length - 1];
        for (int i = 1; i < token.length; i++) {
            rgb[i - 1] = Float.parseFloat(token[i]);
        }
        return rgb;
    }

    public static void main(String[] args) {
        Entity entity;
        HashMap<String, Material> materials;
        try {
            entity = createObject("C:\\Users\\Leon\\Downloads\\astonMartin\\astonMartin.obj");
            System.out.println(entity);
            materials = createMaterial("C:\\Users\\Leon\\Downloads\\astonMartin\\astonMartin.mtl");

            materials.forEach((key, value) -> System.out.println(value));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
