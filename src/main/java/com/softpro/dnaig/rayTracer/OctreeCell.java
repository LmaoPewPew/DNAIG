package com.softpro.dnaig.rayTracer;

import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.objData.mesh.Face;
import com.softpro.dnaig.objData.mesh.Triangle;
import com.softpro.dnaig.utils.BoundingBox;
import com.softpro.dnaig.utils.ObjFileReader;
import com.softpro.dnaig.utils.Vector3D;

import java.nio.file.AccessDeniedException;
import java.util.*;

public class OctreeCell {

    private BoundingBox boundingBox;
    public ArrayList<Triangle> content = new ArrayList<>();
    private int maxDepth;
    private OctreeCell[] children;

    public OctreeCell(BoundingBox boundingBox, ArrayList<Triangle> content, int maxDepth) {
        this.boundingBox = boundingBox;
        this.content = content;
        this.maxDepth = maxDepth;
    }

    public OctreeCell(BoundingBox boundingBox, int maxDepth){
        this.boundingBox = boundingBox;
        this.maxDepth = maxDepth;
    }

    public void createTree(int depth){
        if(depth <= maxDepth){
            createChildren();
            for (int i = 0; i< children.length; i++){
                children[i].createTree(depth+1);
            }
        }
    }

    private void createChildren(){
        children = new OctreeCell[8];
        Vector3D minVec = new Vector3D(boundingBox.getMinVec().getX(), boundingBox.getMinVec().getY(), boundingBox.getMinVec().getZ());
        Vector3D maxVec = new Vector3D(boundingBox.getMaxVec().getX(), boundingBox.getMaxVec().getY(), boundingBox.getMaxVec().getZ());

        Vector3D dir = maxVec.subtract(minVec);

        Vector3D miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        Vector3D maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0, 0, 0.5)));
        maV.add(dir.multiply(new Vector3D(0.5, 0.5, 1)));
        BoundingBox bb0 = new BoundingBox(maV, miV);
        children[0] = new OctreeCell(bb0, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0.5, 0, 0.5)));
        maV.add(dir.multiply(new Vector3D(1, 0.5, 1)));
        BoundingBox bb1 = new BoundingBox(maV, miV);
        children[1] = new OctreeCell(bb1, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0, 0.5, 0.5)));
        maV.add(dir.multiply(new Vector3D(0.5, 1, 1)));
        BoundingBox bb2 = new BoundingBox(maV, miV);
        children[2] = new OctreeCell(bb2, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0.5, 0.5, 0.5)));
        maV.add(dir);
        BoundingBox bb3 = new BoundingBox(maV, miV);
        children[3] = new OctreeCell(bb3, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV.add(dir.multiply(new Vector3D(0.5, 0.5, 0.5)));
        BoundingBox bb4 = new BoundingBox(maV, miV);
        children[4] = new OctreeCell(bb4, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0.5, 0, 0)));
        maV.add(dir.multiply(new Vector3D(1, 0.5, 0.5)));
        BoundingBox bb5 = new BoundingBox(maV, miV);
        children[5] = new OctreeCell(bb5, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0, 0.5, 0)));
        maV.add(dir.multiply(new Vector3D(0.5, 1, 0.5)));
        BoundingBox bb6 = new BoundingBox(maV, miV);
        children[6] = new OctreeCell(bb6, maxDepth);

        miV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        maV = new Vector3D(minVec.getX(), minVec.getY(), minVec.getZ());
        miV.add(dir.multiply(new Vector3D(0.5, 0.5, 0)));
        maV.add(dir.multiply(new Vector3D(1, 1, 0.5)));
        BoundingBox bb7 = new BoundingBox(maV, miV);
        children[7] = new OctreeCell(bb7, maxDepth);

        for (Triangle t:content) {
            for (int i = 0; i< children.length; i++){
                if(children[i].boundingBox.intersects(t.getBoundingBox())){
                    children[i].content.add(t);
                }
            }
        }

    }


    public ArrayList<OctreeCell> getSortedIntersects(Vector3D origin, Vector3D direction){
        ArrayList<OctreeCell> intersections = intersect(origin, direction);
        Map<Double, OctreeCell> lengthCell = new HashMap<>();
        for (OctreeCell octreeCell:intersections) {
            lengthCell.put(octreeCell.boundingBox.intersect(origin, direction), octreeCell);
        }
        ArrayList<Double> n = new ArrayList<>(lengthCell.keySet());
        n.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if(o1<o2){return -1;}
                else if(o1>o2){return 1;}
                else {return 0;}
            }
        });
        ArrayList<OctreeCell> result = new ArrayList<>();
        for(double num: n){
            result.add(lengthCell.get(num));
        }
        return result;
    }

    public ArrayList<OctreeCell> intersect(Vector3D origin, Vector3D direction){
        ArrayList<OctreeCell> result = new ArrayList<>();
        if(boundingBox.intersect(origin, direction)< Double.MAX_VALUE){
            if(children!=null){
                for(int i = 0; i<children.length; i++){
                    if(children[i].content.size()>0) {
                        result.addAll(children[i].intersect(origin, direction));
                    }
                }
            } else {
                result.add(this);
            }
        }
        return result;
    }

}
