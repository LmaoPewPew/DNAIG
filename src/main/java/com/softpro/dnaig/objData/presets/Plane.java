package com.softpro.dnaig.objData.presets;

import com.softpro.dnaig.objData.mesh.Object3D;
import com.softpro.dnaig.rayTracer.Material_RT;
import com.softpro.dnaig.rayTracer.Ray;
import com.softpro.dnaig.utils.Vector3D;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Plane implements Object3D {

    private Vector3D p1;
    private Vector3D p2;
    private Vector3D p3;

    Material_RT m;


    public Plane(Vector3D p1, Vector3D p2, Vector3D p3, Color color){
        this.p1=p1;
        this.p2=p2;
        this.p3=p3;
        m = new Material_RT(new Vector3D(color.getRed(), color.getGreen(), color.getBlue()));
        m.setReference(this);
    }

    @Override
    public double intersect(Ray ray) {
        Vector3D origin = ray.position;
        Vector3D direction = ray.direction;
        Vector3D o = p1;
        Vector3D v1 = p3.subtract(p1);
        Vector3D v2 = p2.subtract(p1);
        Vector3D normal = getNormal(new Vector3D());
        if(direction.scalarProduct(normal) == 0 || v1.crossProduct(v2).length() == 0){
            return Double.MAX_VALUE;
        }
        double res = normal.product(o);
        return (res-normal.product(origin))/normal.product(direction);
    }

    @Override
    public int getColor(Vector3D position, int depth) throws IOException {
        return m.getRGB(position, depth);
    }

    @Override
    public Vector3D getNormal(Vector3D position) {
        Vector3D tmp1 = p3.subtract(p1).normalize();
        Vector3D tmp2 = p2.subtract(p1).normalize();
        return tmp1.crossProduct(tmp2).normalize();
    }
}
