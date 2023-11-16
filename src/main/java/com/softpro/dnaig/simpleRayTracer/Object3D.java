package com.softpro.dnaig.simpleRayTracer;

import com.softpro.dnaig.utils.Vector3D;

import java.io.IOException;

public interface Object3D {
    float intersect(Ray ray);

    int getColor(Vector3D position, int depth) throws IOException;

    public Vector3D getNormal(Vector3D position);
}
