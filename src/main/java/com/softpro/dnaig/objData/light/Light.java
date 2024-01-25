package com.softpro.dnaig.objData.light;

import com.softpro.dnaig.utils.Vector3D;

public interface Light {

    Vector3D getPosition();
    Vector3D getRgb(Vector3D fromPosition);

    Vector3D getRgb();
    double getIntensity();

    /**
     * @return YAML representation of the light
     */
    String toYaml();

    int getID();
    void setID(int id);
}
