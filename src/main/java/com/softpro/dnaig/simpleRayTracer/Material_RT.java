package com.softpro.dnaig.simpleRayTracer;

import com.softpro.dnaig.utils.ColorConverter;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Material_RT {
    Object3D reference;

    Vector3D_RT ambient;
    Vector3D_RT diffus = new Vector3D_RT(0.7, 0.7, 0.7);
    Vector3D_RT specular = new Vector3D_RT(0.3, 0.3, 0.3);

    double phongExponent = 5;

    double reflectionIdx = 0.5;

    public Material_RT(Vector3D_RT color) {
        this.ambient = color;
    }

    public Material_RT(Vector3D_RT color, Object3D ref) {
        this.ambient = color;
        this.reference = ref;
    }

    public int getRGB(Vector3D_RT position, int depth) throws IOException {
        if(reference==null){
            return 0;
        }

        Vector3D_RT sum = new Vector3D_RT();
        for(Light l: CustomScene.getScene().lights){
            Vector3D_RT positionToLight = l.getPosition().subtract(position).normalize();
            Vector3D_RT moved = position.move(Util.EPSILON, positionToLight);
            Ray shadow = new Ray(moved, positionToLight);
            //boolean shadowed = shadow.castShadow2();
            boolean shadowed = shadow.castShadow();
            Vector3D_RT ret = new Vector3D_RT();
            ret.add(ambient.multiply(l.getIntensity(position)));

            if(!shadowed){
                Vector3D_RT normal = reference.getNormal(position);
                double NL = Math.max(normal.prod(positionToLight), 0);
                ret.add(diffus.multiply(l.getIntensity(position)).skalarmultiplication(NL));

                Vector3D_RT refl = normal.skalarmultiplication(NL*2).subtract(positionToLight).normalize();
                Vector3D_RT v = RayTracer.camera.getEye().subtract(position).normalize();
                double RV = Math.max(refl.prod(v), 0);
                ret.add(specular.multiply(l.getIntensity(position)).skalarmultiplication(Math.pow(RV, phongExponent)));
            }
            double dist = l.getPosition().subtract(position).length();
            sum.add(ret.skalarmultiplication(1/(dist*dist)).skalarmultiplication(255));
        }
        if(reflectionIdx>0){
            Vector3D_RT normal = reference.getNormal(position);
            Vector3D_RT V = RayTracer.camera.getEye().subtract(position).normalize();
            double NV = Math.max(normal.prod(V), 0);
            Vector3D_RT refl = normal.skalarmultiplication(2*NV).subtract(V).normalize();
            Ray reflection = new Ray(position.move(Util.EPSILON, refl), refl);
            int res = reflection.castPrimary2(depth +1);
            Color c = ColorConverter.rgbToColorConverter(res);
            Vector3D_RT v = new Vector3D_RT(c.getRed(), c.getGreen(), c.getBlue());
            sum.add(v.skalarmultiplication(reflectionIdx));
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
