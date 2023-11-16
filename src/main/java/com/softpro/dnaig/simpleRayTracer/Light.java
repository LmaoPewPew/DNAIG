package com.softpro.dnaig.simpleRayTracer;

import com.softpro.dnaig.utils.Vector3D;

public interface Light {

    Vector3D getPosition();

    Vector3D getIntensity(Vector3D fromPosition);

}
