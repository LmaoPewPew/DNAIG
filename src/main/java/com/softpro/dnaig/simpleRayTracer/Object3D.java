package simpleRayTracer;

import java.io.IOException;

public interface Object3D {
    double intersect(Ray ray);

    int getColor(Vector3D position, int depth) throws IOException;

    public Vector3D getNormal(Vector3D position);
}
