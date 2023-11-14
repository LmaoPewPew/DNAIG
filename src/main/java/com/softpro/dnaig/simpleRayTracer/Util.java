package simpleRayTracer;

public class Util {
    public static final double EPSILON = 0.00004;
    public static final int maxRecursionDepth = 0;

    public static Vector3D add(Vector3D a, Vector3D b){
        return new Vector3D(a.getX()+b.getX(), a.getY()+b.getY(), a.getZ()+b.getZ());
    }

    public static Vector3D add(Vector3D a, Vector3D b, Vector3D c){
        return add(a, add(b, c));
    }

    public static Vector3D mitternachtsformel(double a, double b, double c){
        double discriminant = b*b-4*a*c;
        if(discriminant<0 || a==0){
            return new Vector3D();
        } else if(discriminant == 0){
            return new Vector3D((-b)/(2*a), 0, 1);
        } else {
            return new Vector3D((-b+Math.sqrt(discriminant))/(2*a), (-b-Math.sqrt(discriminant))/(2*a), 2);
        }
    }
}
