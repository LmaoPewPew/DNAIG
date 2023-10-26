package com.dnaig.dnaig.utils;

import com.dnaig.dnaig.objData.Entity;
import com.dnaig.dnaig.objData.Face;
import com.dnaig.dnaig.objData.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ObjFileReader {

    public static void createObject(String path) throws IOException {
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

            String[] parts = line.split(" ");

            /*
             * Vertex
             * Parses all 3 coordinates into floats and stores in 3D Vector
             */
            if(line.startsWith("v ")){
                float x = Float.parseFloat(parts[1]);
                float y = Float.parseFloat(parts[2]);
                float z = Float.parseFloat(parts[3]);

                Vector3D vector3d = new Vector3D(x, y, z);
                vertexList.add(vector3d);

                //System.out.println("x: " + x + " y: " + y + " z: " + z);
            }

            /*
             * Vertex normal
             * Parses all 3 coordinates into floats and stores in 3D Vector
             */
            else if(line.startsWith("vn ")){
                float x = Float.parseFloat(parts[1]);
                float y = Float.parseFloat(parts[2]);
                float z = Float.parseFloat(parts[3]);

                Vector3D vector3d = new Vector3D(x, y, z);
                vertexNormalList.add(vector3d);
            }

            /*
             * Vertex texture coordinates
             * Parses both u and v coordinates into floats and stores in 3D Vector
             * -> (u, v, 0)
             */
            else if(line.startsWith("vt ")){
                float u = Float.parseFloat(parts[1]);
                float v = Float.parseFloat(parts[2]);

                Vector3D vector3d = new Vector3D(u, v, 0f);
                vertexTextureList.add(vector3d);
            }

            /*
             * Face
             * Reads arbitrary amount of vertex-indexes
             * structure: f v/vt/vn v/vt/vn v/vt/vn v/vt/vn
             */
            else if(line.startsWith("f ")){

                // int[numberOfVerticesPerFace][indexOfVertexType]
                String[][] indexSeparation = new String[parts.length - 1][];
                int[][] indexes = new int[parts.length - 1][];

                // for each vertex per face the indexes for the vertices are split
                // additionally for each iteration the array for the indexes is created with the corresponding size
                for (int i = 1; i < parts.length; i++) {
                    indexSeparation[i - 1] = parts[i].split("/");
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
            else if(line.startsWith("o ")){
                String[] part = line.split(" ");
                name = part[1];
            }
        }
        br.close();

        Entity entity = new Entity(name, faces, vertexList.size(), false);
        System.out.println(entity);
    }

    public static void main(String[] args) {
        try {
            createObject("C:\\Users\\Leon\\Documents\\ObjFileImporter\\src\\obj\\star-destroyer\\stardestroyer.obj");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
