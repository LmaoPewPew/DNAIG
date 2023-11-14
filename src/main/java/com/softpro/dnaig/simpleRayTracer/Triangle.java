package simpleRayTracer;

import dnaig.objData.Face;
import dnaig.objData.Vertex;

import java.awt.*;
import java.io.IOException;

public class Triangle implements Object3D{

    Vector3D p1, p2, p3;
    Material m = new Material(new Vector3D(1, 0, 0));;
    {
        m.setReference(this);
    }

    public Triangle(Vector3D p1, Vector3D p2, Vector3D p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle(Vector3D p1, Vector3D p2, Vector3D p3, Color color){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        m = new Material(new Vector3D(color.getRed()/255, color.getGreen()/255, color.getBlue()/255));
        m.setReference(this);
    }

    public Triangle(Face face, Color color){
        for (int i = 0; i<3; i++) {
            Vertex vertex = face.getVertices()[i];
            switch (i){
                case 0:
                    this.p1 = new Vector3D(vertex.getCoordinates().getX(), vertex.getCoordinates().getY(), vertex.getCoordinates().getZ());
                    break;
                case 1:
                    this.p2 = new Vector3D(vertex.getCoordinates().getX(), vertex.getCoordinates().getY(), vertex.getCoordinates().getZ());
                    break;
                case 2:
                    this.p3 = new Vector3D(vertex.getCoordinates().getX(), vertex.getCoordinates().getY(), vertex.getCoordinates().getZ());
                    break;
                default: break;
            }
        }
        m = new Material(new Vector3D(color.getRed()/255, color.getGreen()/255, color.getBlue()/255));
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
        if(direction.skalarproduct(normal) == 0 || v1.cross(v2).length() == 0){
            return Double.MAX_VALUE;
        }
        double res = normal.prod(o);
        double r = (res-normal.prod(origin))/normal.prod(direction);
        Vector3D intersection = Util.add(origin, direction.skalarmultiplication(r));
        double v2v , v1v;
        v2v = v1v = Double.MAX_VALUE;
        int[] lines = {-1, -1, -1};
        for (int i = 0; i<3; i++){
            if(v1.getValue(i) == 0){
                if(v2.getValue(i) != 0){
                    lines[i] = 2;
                }
            } else {
                if(v2.getValue(i) == 0){
                    lines[i] = 1;
                } else {
                    lines[i] = 0;
                }
            }
        }
        for (int i = 0; i<3; i++){
            if(lines[i] == 1){
                v1v = (intersection.getValue(i)-o.getValue(i))/v1.getValue(i);
            }else if(lines[i] == 2){
                v2v = (intersection.getValue(i)-o.getValue(i))/v2.getValue(i);
            }
        }
        for (int i = 0; i<3; i++){
            if(lines[i] == 0){
                if(v1v == Double.MAX_VALUE){
                    if(v2v == Double.MAX_VALUE){
                        double v1vn = (intersection.getValue(i)-o.getValue(i))/v1.getValue(i);
                        double v1vv = -v2.getValue(i)/v1.getValue(i);
                        for (int n = i+1; n<3; n++){
                            if(lines[n] == 0){
                                double div = v1.getValue(n)*v1vv + v2.getValue(n);
                                v2v = (intersection.getValue(i) - (v1.getValue(i) * v1vn + o.getValue(i))) / div;
                                v1v = v1vn + v1vv * v2v;
                                break;
                            }
                        }
                    } else {
                        v1v = ((intersection.getValue(i)-o.getValue(i))-v2.getValue(i)*v2v)/v1.getValue(i);
                        break;
                    }
                } else {
                    if(v2v == Double.MAX_VALUE){
                        v2v = ((intersection.getValue(i)-o.getValue(i))-v1.getValue(i)*v1v)/v2.getValue(i);
                        break;
                    }
                }
            }
        }
        if(v1v== Double.MAX_VALUE || v2v == Double.MAX_VALUE || v1v < 0 || v2v <0) {
            return Double.MAX_VALUE;
        }
        if (v1v+v2v <= 1 && r > 0) {
            return r;
        }
        return Double.MAX_VALUE;
    }

    @Override
    public int getColor(Vector3D position, int depth) throws IOException {
        return m.getRGB(position, depth);
    }

    @Override
    public Vector3D getNormal(Vector3D position) {
        Vector3D tmp1 = p3.subtract(p1).normalize();
        Vector3D tmp2 = p2.subtract(p1).normalize();
        return tmp1.cross(tmp2).normalize();
    }
}
