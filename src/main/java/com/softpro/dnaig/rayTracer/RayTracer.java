package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.Output;
import com.softpro.dnaig.utils.Vector3D;

import java.io.IOException;

public class RayTracer {

    static Camera camera = new Camera();
    int n = 0;

    public void trace() throws IOException {
        long time = System.currentTimeMillis();

        for(int i = 0; i< Output.WIDTH; i++){
            for(int j = 0; j<Output.HEIGHT; j++){
                double u= camera.getL() + i + 0.5;
                u = u/200;
                double v= camera.getT() - (j+0.5);
                v = v/200;
                //Vector3D s = Util.add(camera.getU().skalarmultiplication(u), camera.getV().skalarmultiplication(v), camera.getW_d_negated());
                Vector3D s = Util.add(camera.getU().scalarMultiplication(u), camera.getV().scalarMultiplication(v), camera.getScreen()).subtract(camera.getEye());
                Vector3D dir = s.normalize();

                n++;
                Ray r = new Ray(camera.getEye(), dir);

                int res_color = r.castPrimary(0);

                Output.setPixel(i, j, res_color);

            }
        }

        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000.0 + "s");
    }
}