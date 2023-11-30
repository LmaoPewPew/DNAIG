package com.softpro.dnaig.objData.light;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.utils.Vector3D;

public class PointLight implements Light {
    Vector3D position;
    Vector3D intensity;

    public PointLight(Vector3D position, Vector3D intensity){
        this.position=position;
        this.intensity=intensity;
    }

    @Override
    public Vector3D getPosition() {
        return position;
    }

    @Override
    public Vector3D getIntensity(Vector3D fromPosition) {
        return intensity;
    }
}
