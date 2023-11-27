package com.softpro.dnaig.simpleRayTracer;


public class PointLight implements Light{
    Vector3D_RT position;
    Vector3D_RT intensity;

    public PointLight(Vector3D_RT position, Vector3D_RT intensity){
        this.position=position;
        this.intensity=intensity;
    }

    @Override
    public Vector3D_RT getPosition() {
        return position;
    }

    @Override
    public Vector3D_RT getIntensity(Vector3D_RT fromPosition) {
        return intensity;
    }
}
