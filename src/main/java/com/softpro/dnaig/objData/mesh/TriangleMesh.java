package com.softpro.dnaig.objData.mesh;

import java.util.ArrayList;

public interface TriangleMesh {

    ArrayList<Triangle> getTriangles(double factor);

    String toYaml();
}
