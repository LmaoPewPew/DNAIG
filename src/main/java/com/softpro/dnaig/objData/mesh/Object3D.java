package com.softpro.dnaig.objData.mesh;

import com.softpro.dnaig.rayTracer.Ray;
import com.softpro.dnaig.utils.Vector3D;

import java.io.IOException;

public interface Object3D {
    double intersect(Ray ray);

    int getColor(Vector3D position, int depth) throws IOException;

    Vector3D getNormal(Vector3D position);
}