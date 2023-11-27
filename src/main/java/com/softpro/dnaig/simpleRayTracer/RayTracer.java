package com.softpro.dnaig.simpleRayTracer;


import com.softpro.dnaig.Output;

import java.io.IOException;

public class RayTracer {

    static Camera camera = new Camera();
    int n = 0;

    public void trace() throws IOException {
        for(int i = 0; i< Output.WIDTH; i++){
            for(int j = 0; j<Output.HEIGHT; j++){
                double u= camera.getL() + i + 0.5;
                u = u/200;
                double v= camera.getT() - (j+0.5);
                v = v/200;
                //Vector3D s = Util.add(camera.getU().skalarmultiplication(u), camera.getV().skalarmultiplication(v), camera.getW_d_negated());
                Vector3D_RT s = Util.add(camera.getU().skalarmultiplication(u), camera.getV().skalarmultiplication(v), camera.getScreen()).subtract(camera.getEye());
                Vector3D_RT dir = s.normalize();

                n++;
                Ray r = new Ray(camera.getEye(), dir);

                int res_color = r.castPrimary2(0);
                Output.setPixel(i, j, res_color);

            }
        }
    }
}