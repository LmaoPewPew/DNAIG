package com.softpro.dnaig.simpleRayTracer;


import javafx.scene.paint.Color;

import java.io.IOException;

public class Sphere implements Object3D{
    double radius;
    Vector3D_RT center;
    Material_RT m = new Material_RT(new Vector3D_RT(0, 1, 0));
    {
        m.setReference(this);
    }


    public Sphere(double radius, Vector3D_RT center){
        this.radius=radius;
        this.center=center;
    }

    public Sphere(int radius, Vector3D_RT center, Color c) {
        this.radius=radius;
        this.center=center;
        this.m = new Material_RT(new Vector3D_RT(c.getRed(), c.getGreen(), c.getBlue()), this);
        m.setReference(this);
    }

    @Override
    public double intersect(Ray ray) {
        Vector3D_RT dir = ray.direction;
        Vector3D_RT origin = ray.position;

        // a = dir * dir
        double a = dir.prod(dir);
        // b = 2 * dir * (origin - center)
        Vector3D_RT ec = origin.subtract(center);
        // c = (origin - center) * (origin - center) - radius * radius
        double b = 2*dir.prod(ec);
        // c = (origin - center) * (origin - center) - radius * radius
        double c = ec.prod(ec)-radius*radius;

        // Calculate the discriminant
        Vector3D_RT result = Util.mitternachtsformel(a, b, c);

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
    public int getColor(Vector3D_RT position, int depth) throws IOException {
        return m.getRGB(position, depth);
    }

    @Override
    public Vector3D_RT getNormal(Vector3D_RT position) {
        return position.subtract(center).normalize();
    }
}
