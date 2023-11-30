package com.softpro.dnaig.objData.presets;


import com.softpro.dnaig.rayTracer.Material_RT;
import com.softpro.dnaig.objData.mesh.Object3D;
import com.softpro.dnaig.rayTracer.Ray;
import com.softpro.dnaig.rayTracer.Util;
import com.softpro.dnaig.utils.Vector3D;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Sphere implements Object3D {
    double radius;
    Vector3D center;
    Material_RT m = new Material_RT(new Vector3D(0, 1, 0));
    {
        m.setReference(this);
    }


    public Sphere(double radius, Vector3D center){
        this.radius=radius;
        this.center=center;
    }

    public Sphere(int radius, Vector3D center, Color c) {
        this.radius=radius;
        this.center=center;
        this.m = new Material_RT(new Vector3D(c.getRed(), c.getGreen(), c.getBlue()), this);
        m.setReference(this);
    }

    @Override
    public double intersect(Ray ray) {
        Vector3D dir = ray.direction;
        Vector3D origin = ray.position;

        // a = dir * dir
        double a = dir.product(dir);
        // b = 2 * dir * (origin - center)
        Vector3D ec = origin.subtract(center);
        // c = (origin - center) * (origin - center) - radius * radius
        double b = 2*dir.product(ec);
        // c = (origin - center) * (origin - center) - radius * radius
        double c = ec.product(ec)-radius*radius;

        // Calculate the discriminant
        Vector3D result = Util.mitternachtsformel(a, b, c);

        // If there is no solution, return -1
        switch ((int)Math.round(result.getZ())){
            // If there is no solution, return -1
            case 1:
                return result.getX();
            // If there are two solutions, return the smaller one (closer to the camera)
            case 2:
                if(result.getX()<0){
                    if(result.getY()<0){
                        return Double.MAX_VALUE;
                    }else {
                        return result.getY();
                    }
                }else {
                    if(result.getY()<0){
                        return result.getX();
                    }else {
                        return Math.min(result.getX(), result.getY());
                    }
                }
            // If there is one solution, return the solution
            default:
                return Double.MAX_VALUE;
        }
    }

    @Override
    public int getColor(Vector3D position, int depth) throws IOException {
        return m.getRGB(position, depth);
    }

    @Override
    public Vector3D getNormal(Vector3D position) {
        return position.subtract(center).normalize();
    }
}
