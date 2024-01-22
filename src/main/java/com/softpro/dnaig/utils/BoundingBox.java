package com.softpro.dnaig.utils;

import com.softpro.dnaig.objData.presets.Plane;
import com.softpro.dnaig.rayTracer.Ray;
import com.softpro.dnaig.rayTracer.Util;

import java.util.ArrayList;

public class BoundingBox {

    private Vector3D maxVec;
    private Vector3D minVec;

    public BoundingBox(Vector3D maxVec, Vector3D minVec){
        this.maxVec = maxVec;
        this.minVec = minVec;
    }

    public void setMaxVec(Vector3D maxVec) {
        this.maxVec = maxVec;
    }

    public void setMinVec(Vector3D minVec) {
        this.minVec = minVec;
    }

    public Vector3D getMaxVec() {
        return maxVec;
    }

    public Vector3D getMinVec() {
        return minVec;
    }

    public boolean intersects(BoundingBox b){
        Boolean test1 = b.maxVec.getX()+Util.EPSILON<minVec.getX()||b.maxVec.getY()+Util.EPSILON<minVec.getY()||b.maxVec.getZ()+Util.EPSILON<minVec.getZ();
        Boolean test2 = b.minVec.getX()-Util.EPSILON > maxVec.getX()||b.minVec.getY()-Util.EPSILON > maxVec.getY()||b.minVec.getZ()-Util.EPSILON > maxVec.getZ();
        if(test1||test2){
            return false;
        } else {
            return true;
        }
    }

    public boolean contains(Vector3D point){
        boolean test1 = point.getX()<= maxVec.getX()+Util.EPSILON &&point.getY()<= maxVec.getY()+Util.EPSILON&&point.getZ()<= maxVec.getZ()+Util.EPSILON;
        boolean test2 = point.getX()+Util.EPSILON>= minVec.getX()&&point.getY()+Util.EPSILON>= minVec.getY()&&point.getZ()+Util.EPSILON>= minVec.getZ();
        return test1 && test2;
    }

    public double intersect(Vector3D origin, Vector3D direction){
        direction = direction.normalize();
        Ray ray = new Ray(origin, direction);
        Plane plane1 = new Plane(maxVec, new Vector3D(1, 0, 0));
        Plane plane2 = new Plane(maxVec, new Vector3D(0, 1, 0));
        Plane plane3 = new Plane(maxVec, new Vector3D(0, 0, 1));
        Plane plane4 = new Plane(minVec, new Vector3D(1, 0, 0));
        Plane plane5 = new Plane(minVec, new Vector3D(0, 1, 0));
        Plane plane6 = new Plane(minVec, new Vector3D(0, 0, 1));
        Plane[] planes = {plane1, plane2, plane3, plane4, plane5, plane6};
        double res = Double.MAX_VALUE;
        for (int i = 0; i<planes.length; i++){
            double r = planes[i].intersect(ray);
            if(r<Double.MAX_VALUE){
                Vector3D intersection = new Vector3D();
                intersection.add(origin);
                intersection.add(direction.scalarMultiplication(r));
                if(contains(intersection) && r<res){
                    res = r;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        BoundingBox boundingBox = new BoundingBox(new Vector3D(1, 1, 1), new Vector3D(-1, -1, -1));
        BoundingBox boundingBox1 = new BoundingBox(new Vector3D(2, 1.2, 1), new Vector3D(1.1, 0.1, 0.1));
        Vector3D or = new Vector3D(2, 0, 0);
        Vector3D dir = new Vector3D(-1, 0, 0);
        System.out.println(boundingBox.intersects(boundingBox1));
        System.out.println(boundingBox1.intersect(or, dir));
    }

}
