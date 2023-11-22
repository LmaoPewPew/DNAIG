package com.softpro.dnaig.simpleRayTracer;


import com.softpro.dnaig.Output;

import java.io.IOException;

public class RayTracer {

    static Camera camera = new Camera();
    int n = 0;

    /**
     * Trace the scene
     * 
     * @throws IOException
     */
    public void trace() throws IOException {
        for(int i = 0; i < Output.WIDTH; i++){
            for(int j = 0; j < Output.HEIGHT; j++){
                // Calculate the direction of the ray
                double u= camera.getScreenLeft() + i + 0.5;
                //double u = camera.getScreenLeft() + (camera.getScreenRight() - camera.getScreenLeft()) * (i + 0.5) / Output.WIDTH;
                u = u/200;
                double v= camera.getScreenTop() - (j+0.5);
                //double v = camera.getScreenTop() - (camera.getScreenTop() - camera.getScreenBottom()) * (j + 0.5) / Output.HEIGHT;
                v = v/200;
                //Vector3D s = Util.add(camera.getU().skalarmultiplication(u), camera.getV().skalarmultiplication(v), camera.getW_d_negated());

                Vector3D_RT s = Util.add(camera.getU().skalarmultiplication(u), camera.getV().skalarmultiplication(v), camera.getScreen()).subtract(camera.getEye());
                Vector3D_RT dir = s.normalize();

                n++;
                Ray r = new Ray(camera.getEye(), dir);

                int res_color = r.castPrimary(0);
                Output.setPixel(i, j, res_color);

            }
        }
    }
}
