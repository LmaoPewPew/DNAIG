package com.softpro.dnaig.simpleRayTracer;

public class Util {
    public static final double EPSILON = 0.00004;
    public static final int maxRecursionDepth = 0;

    public static Vector3D_RT add(Vector3D_RT a, Vector3D_RT b){
        return new Vector3D_RT(a.getX()+b.getX(), a.getY()+b.getY(), a.getZ()+b.getZ());
    }

    public static Vector3D_RT add(Vector3D_RT a, Vector3D_RT b, Vector3D_RT c){
        return add(a, add(b, c));
    }

    
    public static Vector3D_RT mitternachtsformel(double a, double b, double c){
        // a*x^2 + b*x + c = 0
        double discriminant = b*b-4*a*c;
        // If discriminant is negative, there is no solution
        if(discriminant<0 || a==0){
            // x = 0
            return new Vector3D_RT();
        } 
        // If discriminant is zero, there is one solution
        else if(discriminant == 0){
            // x = -b/(2*a)
            return new Vector3D_RT((-b)/(2*a), 0, 1);
        } 
        // If discriminant is positive, there are two solutions
        else {
            // x1 = (-b+sqrt(b^2-4*a*c))/(2*a)
            double rightPart = Math.sqrt(discriminant);
            //return new Vector3D_RT((-b+Math.sqrt(discriminant))/(2*a), (-b-Math.sqrt(discriminant))/(2*a), 2);
            return new Vector3D_RT((-b+rightPart)/(2*a), (-b-rightPart)/(2*a), 2);
        }
    }
}
