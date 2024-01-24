package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.objData.mesh.Entity;
import com.softpro.dnaig.objData.mesh.Face;
import com.softpro.dnaig.objData.mesh.Object3D;
import com.softpro.dnaig.objData.mesh.Triangle;
import com.softpro.dnaig.utils.ColorConverter;
import com.softpro.dnaig.utils.Vector3D;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class Ray {
    public Vector3D position;
    public Vector3D direction;

    public Ray(Vector3D pos, Vector3D dir) {
        this.direction = dir.normalize();
        this.position = pos;
    }

    public Ray(Vector3D from_point, Vector3D to_point, boolean dummy) {
        this.position = from_point;
        this.direction = from_point.subtract(from_point).normalize();
    }

    public int castPrimary(int depth) throws IOException {
        if(depth > Util.maxRecursionDepth){
            return ColorConverter.colorToRGBConverter(Color.BLACK);
        }
        Object3D intersectP = null;
        double t2 = -1;
        double tp = Double.MAX_VALUE-1;

        for (Object3D o : CustomScene.getScene().objects) {
            t2 = o.intersect(this);
            if (t2 > 0 && t2 < tp) {
                intersectP = o;
                tp = t2;
            }
        }
        double t = Double.MAX_VALUE;
        Object3D intersect = null;

        ArrayList<OctreeCell> cells = CustomScene.getScene().root.getSortedIntersects(position, direction);
        for (OctreeCell cell:cells) {
            for (Triangle o: cell.content) {
                t2 = o.intersect(this);
                if(t2 > 0 && t2< t){
                    intersect = o;
                    t = t2;
                }
            }
            if(t<tp){
                return intersect.getColor(this.getPosition(t), depth);
            }
        }
        if(intersectP!=null){
            return intersectP.getColor(this.getPosition(tp), depth);
        } else {
            return ColorConverter.colorToRGBConverter(Color.BLACK);
        }
    }

    public boolean castShadow(Vector3D lightPosition) {
        double t = position.subtract(lightPosition).length();
        double t2 = -1;
        for(Object3D o: CustomScene.getScene().objects){
            t2 = o.intersect(this);
            if(t2 > 0 && t2< t){
                return true;
            }
        }
        ArrayList<OctreeCell> cells = CustomScene.getScene().root.getSortedIntersects(position, direction);
        for (OctreeCell cell:cells) {
            for (Triangle o : cell.content) {
                t2 = o.intersect(this);
                if (t2 > 0 && t2 < t) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean castShadow2() throws IOException {
        double t = Double.MAX_VALUE-1;

        for(Entity e: CustomScene.getScene().entities){
            for (Face face: e.getFaces()) {
                Triangle tri = new Triangle(face, Color.BLUE);
                double t2 = tri.intersect(this);
                if (t2 > 0 && t2 < t) {
                    return true;
                }
            }
        }
        return false;
    }

    private Vector3D getPosition(double t) {
        return Util.add(this.position, this.direction.scalarMultiplication(t));
    }
}