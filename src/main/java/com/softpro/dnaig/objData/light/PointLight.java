package com.softpro.dnaig.objData.light;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.utils.Vector3D;

public class PointLight implements Light {
    Vector3D position;
    Vector3D intensity;

    /**
     * @param position 3D position of the light
     * @param intensity intensity of the light
     */
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

    @Override
    public String yamlString() {
        return """
                \tLight:
                \t\t- type: PointLight
                \t\t  position: %s
                \t\t  intensity: %s
                """.formatted(position.yamlString(), intensity.yamlString());
    }
}
