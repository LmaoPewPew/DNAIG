package com.softpro.dnaig.simpleRayTracer;

public interface Light {

    Vector3D_RT getPosition();

    Vector3D_RT getIntensity(Vector3D_RT fromPosition);

}
