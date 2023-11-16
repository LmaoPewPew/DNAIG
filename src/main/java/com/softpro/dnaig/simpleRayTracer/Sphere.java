package com.softpro.dnaig.simpleRayTracer;


import com.softpro.dnaig.utils.Vector3D;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Sphere implements Object3D{
    float radius;
    Vector3D center;
    Material_RT m = new Material_RT(new Vector3D(0, 1, 0));
    {
        m.setReference(this);
    }


    public Sphere(float radius, Vector3D center){
        this.radius=radius;
        this.center=center;
    }

    public Sphere(int radius, Vector3D center, Color c) {
        this.radius=radius;
        this.center=center;
        this.m = new Material_RT(new Vector3D((float)c.getRed()/255, (float)c.getGreen()/255, (float)c.getBlue()/255), this);
        m.setReference(this);
    }

    @Override
    public float intersect(Ray ray) {
        Vector3D dir = ray.direction;
        Vector3D origin = ray.position;
        float a = dir.product(dir);
        Vector3D ec = origin.subtract(center);
        float b = 2*dir.product(ec);
        float c = ec.product(ec)-radius*radius;
        Vector3D result = Util.mitternachtsformel(a, b, c);
        switch ((int)Math.round(result.getZ())){
            case 1:
                return result.getX();
            case 2:
                if(result.getX()<0){
                    if(result.getY()<0){
                        return Float.MAX_VALUE;
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
            default:
                return Float.MAX_VALUE;
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
