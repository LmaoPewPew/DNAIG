package com.softpro.dnaig.simpleRayTracer;

import java.io.IOException;

public interface Object3D {
    double intersect(Ray ray);

    int getColor(Vector3D_RT position, int depth) throws IOException;

    Vector3D_RT getNormal(Vector3D_RT position);
}