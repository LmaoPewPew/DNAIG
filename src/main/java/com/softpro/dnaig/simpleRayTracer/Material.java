package simpleRayTracer;

import java.awt.*;
import java.io.IOException;

public class Material {
    Object3D reference;

    Vector3D ambient;
    Vector3D diffus = new Vector3D(0.7, 0.7, 0.7);
    Vector3D specular = new Vector3D(0.3, 0.3, 0.3);

    double phongExponent = 5;

    double reflectionIdx = 0.5;

    public Material(Vector3D color) {
        this.ambient = color;
    }

    public Material(Vector3D color, Object3D ref) {
        this.ambient = color;
        this.reference = ref;
    }

    public int getRGB(Vector3D position, int depth) throws IOException {
        if(reference==null){
            return 0;
        }

        Vector3D sum = new Vector3D();
        for(Light l: Scene.getScene().lights){
            Vector3D positionToLight = l.getPosition().subtract(position).normalize();
            Ray shadow = new Ray(position.move(Util.EPSILON, positionToLight), positionToLight);
            boolean shadowed = shadow.castShadow2();

            Vector3D ret = new Vector3D();
            ret.add(ambient.multiply(l.getIntensity(position)));

            if(!shadowed){
                Vector3D normal = reference.getNormal(position);
                double NL = Math.max(normal.prod(positionToLight), 0);
                ret.add(diffus.multiply(l.getIntensity(position)).skalarmultiplication(NL));

                Vector3D refl = normal.skalarmultiplication(NL*2).subtract(positionToLight).normalize();
                Vector3D v = RayTracer.camera.getEye().subtract(position).normalize();
                double RV = Math.max(refl.prod(v), 0);
                ret.add(specular.multiply(l.getIntensity(position)).skalarmultiplication(Math.pow(RV, phongExponent)));
            }
            double dist = l.getPosition().subtract(position).length();
            sum.add(ret.skalarmultiplication(1/(dist*dist)).skalarmultiplication(255));
        }
        if(reflectionIdx>0){
            Vector3D normal = reference.getNormal(position);
            Vector3D V = RayTracer.camera.getEye().subtract(position).normalize();
            double NV = Math.max(normal.prod(V), 0);
            Vector3D refl = normal.skalarmultiplication(2*NV).subtract(V).normalize();
            Ray reflection = new Ray(position.move(Util.EPSILON, refl), refl);
            int res = reflection.castPrimary(depth +1);
            Color c = new Color(res);
            Vector3D v = new Vector3D(c.getRed(), c.getGreen(), c.getBlue());
            sum.add(v.skalarmultiplication(reflectionIdx));
        }
        sum.setX(Math.min(255, sum.getX()));
        sum.setY(Math.min(255, sum.getY()));
        sum.setZ(Math.min(255, sum.getZ()));

        sum.setX(Math.max(0, sum.getX()));
        sum.setY(Math.max(0, sum.getY()));
        sum.setZ(Math.max(0, sum.getZ()));
        Color c = new Color((int)Math.round(sum.getX()), (int)Math.round(sum.getY()), (int)Math.round(sum.getZ()));
        return c.getRGB();
    }

    public void setReference(Object3D reference){
        this.reference = reference;
    }
}
