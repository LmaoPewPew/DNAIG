package com.softpro.dnaig.simpleRayTracer;

import com.softpro.dnaig.utils.ColorConverter;
import com.softpro.dnaig.utils.Vector3D;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Material_RT {
    Object3D reference;

    Vector3D ambient;
    Vector3D diffus = new Vector3D(0.7f, 0.7f, 0.7f);
    Vector3D specular = new Vector3D(0.3f, 0.3f, 0.3f);

    double phongExponent = 5;

    float reflectionIdx = 0.5f;

    public Material_RT(Vector3D color) {
        this.ambient = color;
    }

    public Material_RT(Vector3D color, Object3D ref) {
        this.ambient = color;
        this.reference = ref;
    }

    public int getRGB(Vector3D position, int depth) throws IOException {
        if(reference==null){
            return 0;
        }

        Vector3D sum = new Vector3D();
        for(Light l: CustomScene.getScene().lights){
            Vector3D positionToLight = l.getPosition().subtract(position).normalize();
            Ray shadow = new Ray(position.move(Util.EPSILON, positionToLight), positionToLight);
            boolean shadowed = shadow.castShadow2();

            Vector3D ret = new Vector3D();
            ret.add(ambient.multiply(l.getIntensity(position)));

            if(!shadowed){
                Vector3D normal = reference.getNormal(position);
                float NL = Math.max(normal.product(positionToLight), 0);
                ret.add(diffus.multiply(l.getIntensity(position)).scalarMultiplication(NL));

                Vector3D refl = normal.scalarMultiplication(NL*2).subtract(positionToLight).normalize();
                Vector3D v = RayTracer.camera.getEye().subtract(position).normalize();
                double RV = Math.max(refl.product(v), 0);
                ret.add(specular.multiply(l.getIntensity(position)).scalarMultiplication((float) Math.pow(RV, phongExponent)));
            }
            double dist = l.getPosition().subtract(position).length();
            sum.add(ret.scalarMultiplication((float)(1/(dist*dist))).scalarMultiplication(255));
        }
        if(reflectionIdx>0){
            Vector3D normal = reference.getNormal(position);
            Vector3D V = RayTracer.camera.getEye().subtract(position).normalize();
            float NV = Math.max(normal.product(V), 0);
            Vector3D refl = normal.scalarMultiplication(2*NV).subtract(V).normalize();
            Ray reflection = new Ray(position.move(Util.EPSILON, refl), refl);
            int res = reflection.castPrimary(depth +1);
            Color c = ColorConverter.rgbToColorConverter(res);
            Vector3D v = new Vector3D((float)c.getRed(), (float)c.getGreen(), (float)c.getBlue());
            sum.add(v.scalarMultiplication(reflectionIdx));
        }
        sum.setX(Math.min(255, sum.getX()));
        sum.setY(Math.min(255, sum.getY()));
        sum.setZ(Math.min(255, sum.getZ()));

        sum.setX(Math.max(0, sum.getX()));
        sum.setY(Math.max(0, sum.getY()));
        sum.setZ(Math.max(0, sum.getZ()));
        Color c = Color.rgb((int)Math.round(sum.getX()), (int)Math.round(sum.getY()), (int)Math.round(sum.getZ()));
        int temp = ColorConverter.colorToRGBConverter(c);
        if(temp > 0)
            System.out.println(temp);
        return ColorConverter.colorToRGBConverter(Color.BLUE);
    }

    public void setReference(Object3D reference){
        this.reference = reference;
    }
}
