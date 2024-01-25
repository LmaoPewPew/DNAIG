package com.softpro.dnaig.rayTracer;

import com.softpro.dnaig.objData.mesh.Material;
import com.softpro.dnaig.objData.mesh.Object3D;
import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.utils.ColorConverter;
import com.softpro.dnaig.utils.Vector3D;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Material_RT {
    Object3D reference;

    Vector3D ambient;
    Vector3D diffus = new Vector3D(0.7, 0.7, 0.7);
    Vector3D specular = new Vector3D(0.3, 0.3, 0.3);

    double phongExponent = 5;

    double reflectionIdx = 0.5;

    public Material_RT(Vector3D color) {
        this.ambient = color;
    }

    public Material_RT(Material material){
        if(material==null){
            this.ambient = new Vector3D(0.5, 0.5, 0.5);
            this.diffus = new Vector3D(0.7, 0.7, 0.7);
            this.specular = new Vector3D(0.3, 0.3, 0.3);
            return;
        }
        this.ambient = new Vector3D(material.getKa()[0], material.getKa()[1], material.getKa()[2]);
        this.diffus = new Vector3D(material.getKd()[0], material.getKd()[1], material.getKd()[2]);
        this.specular = new Vector3D(material.getKs()[0], material.getKs()[1], material.getKs()[2]);
        //this.specular = new Vector3D(1, 1, 1);
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
            Vector3D moved = position.move(Util.EPSILON, positionToLight);
            Ray shadow = new Ray(moved, positionToLight);
            boolean shadowed = shadow.castShadow(l.getPosition());

            Vector3D ret = new Vector3D();
            ret.add(ambient.multiply(l.getRgb(position).scalarMultiplication(l.getIntensity()/10)));

            if(!shadowed){
                Vector3D normal = reference.getNormal(position);
                double NL = Math.abs(normal.product(positionToLight));
                ret.add(diffus.multiply(l.getRgb(position).scalarMultiplication(l.getIntensity()/10)).scalarMultiplication(NL));

                Vector3D refl = normal.scalarMultiplication(NL*2).subtract(positionToLight).normalize();
                Vector3D v = RayTracer.camera.getEye().subtract(position).normalize();
                double RV = Math.abs(refl.product(v));
                ret.add(specular.multiply(l.getRgb(position).scalarMultiplication(l.getIntensity()/10)).scalarMultiplication(Math.pow(RV, phongExponent)));
            }
            double dist = l.getPosition().subtract(position).length();
            sum.add(ret.scalarMultiplication(1/(dist*dist)).scalarMultiplication(255));
        }
        if(reflectionIdx>0){
            Vector3D normal = reference.getNormal(position);
            Vector3D V = RayTracer.camera.getEye().subtract(position).normalize();
            double NV = Math.max(normal.product(V), 0);
            Vector3D refl = normal.scalarMultiplication(2*NV).subtract(V).normalize();
            Ray reflection = new Ray(position.move(Util.EPSILON, refl), refl);
            int res = reflection.castPrimary(depth +1);
            Color c = ColorConverter.rgbToColorConverter(res);
            Vector3D v = new Vector3D(c.getRed(), c.getGreen(), c.getBlue());
            sum.add(v.scalarMultiplication(reflectionIdx));
        }
        sum.setX(Math.min(255, sum.getX()));
        sum.setY(Math.min(255, sum.getY()));
        sum.setZ(Math.min(255, sum.getZ()));

        sum.setX(Math.max(0, sum.getX()));
        sum.setY(Math.max(0, sum.getY()));
        sum.setZ(Math.max(0, sum.getZ()));
        Color c = Color.rgb((int)Math.round(sum.getX()), (int)Math.round(sum.getY()), (int)Math.round(sum.getZ()));
        return ColorConverter.colorToRGBConverter(c);
    }

    public void setReference(Object3D reference){
        this.reference = reference;
    }
}