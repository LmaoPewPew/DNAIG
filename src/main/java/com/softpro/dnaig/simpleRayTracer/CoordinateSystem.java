package simpleRayTracer;

import java.awt.*;

public class CoordinateSystem implements Object3D{

    private static CoordinateSystem instance;

    public static CoordinateSystem getCoordinateSystem(){
        if(instance == null){
            instance = new CoordinateSystem();
        }
        return instance;
    }

    @Override
    public double intersect(Ray ray) {
        Vector3D origin = ray.position;
        Vector3D direction = ray.direction;
        double r;
        Vector3D intersection;
        if(direction.getX()!= 0) {
            r = -origin.getX() / direction.getX();
            intersection = Util.add(origin, direction.skalarmultiplication(r));
            if (intersection.getY() * intersection.getY() <= 0.05 || intersection.getZ() * intersection.getZ() <= 0.05) {
                return r;
            }
        }
        if(direction.getY()!= 0) {
            r = -origin.getY() / direction.getY();
            intersection = Util.add(origin, direction.skalarmultiplication(r));
            if (intersection.getX() * intersection.getX() <= 0.05 || intersection.getZ() * intersection.getZ() <= 0.05) {
                return r;
            }
        }
        if(direction.getZ()!= 0) {
            r = -origin.getZ() / direction.getZ();
            intersection = Util.add(origin, direction.skalarmultiplication(r));
            if (intersection.getY() * intersection.getY() <= 0.05 || intersection.getX() * intersection.getX() <= 0.05) {
                return r;
            }
        }
        return Double.MAX_VALUE;
    }

    @Override
    public int getColor(Vector3D position, int depth) {
        return Color.RED.getRGB();
    }

    @Override
    public Vector3D getNormal(Vector3D position) {
        return new Vector3D();
    }
}
