package simpleRayTracer;

import dnaig.objData.Entity;
import dnaig.objData.Face;

import java.awt.*;
import java.io.IOException;

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
            return Color.BLACK.getRGB();
        }
        Object3D intersect = null;
        double t = Double.MAX_VALUE-1;
        for(Object3D o: Scene.getScene().objects){
            double t2 = o.intersect(this);
            if(t2 > 0 && t2< t){
                intersect = o;
                t = t2;
            }
        }
        if(intersect!=null){
            return intersect.getColor(this.getPosition(t), depth);
        } else {
            return Color.BLACK.getRGB();
        }
    }
    public int castPrimary2(int depth) throws IOException {
        if(depth > Util.maxRecursionDepth){
            return Color.BLACK.getRGB();
        }
        Triangle intersect = null;
        double t = Double.MAX_VALUE-1;
        for(Entity e: Scene.getScene().entities){
            for (int i = 0; i< e.getFaces().size(); i++) {
                Triangle tri = new Triangle(e.getFaces().get(i), Color.BLUE);
                double t2 = tri.intersect(this);
                if (t2 > 0 && t2 < t) {
                    intersect = tri;
                    t = t2;
                }
            }
        }
        if(intersect!=null){
            return intersect.getColor(this.getPosition(t), depth);
        } else {
            return Color.BLACK.getRGB();
        }
    }

    public boolean castShadow() throws IOException {
        double t = Double.MAX_VALUE-1;
        for(Object3D o: Scene.getScene().objects){
            double t2 = o.intersect(this);
            if(t2 > 0 && t2< t){
                return true;
            }
        }
        return false;
    }
    public boolean castShadow2() throws IOException {
        double t = Double.MAX_VALUE-1;

        for(Entity e: Scene.getScene().entities){
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
        return Util.add(this.position, this.direction.skalarmultiplication(t));
    }
}
