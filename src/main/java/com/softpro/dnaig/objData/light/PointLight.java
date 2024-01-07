package com.softpro.dnaig.objData.light;


import com.softpro.dnaig.objData.light.Light;
import com.softpro.dnaig.utils.Vector3D;

public class PointLight implements Light {

    int id;

    Vector3D position;
    Vector3D intensity;


    /**
     * @param position 3D position of the light
     * @param intensity intensity of the light
     */
    public PointLight(Vector3D position, Vector3D intensity){
        this.position=position;
        this.intensity=intensity;
        this.id = 0;
    }

    public PointLight(int id, Vector3D position, Vector3D intensity){
        this.position=position;
        this.intensity=intensity;
        this.id = id;
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
    public String toYaml() {
        return String.format(
                """
                -\tposition: %s
                \tKe: { r: %f, g: %f, b: %f }      \s
                """, position.toYaml(), intensity.getX(), intensity.getY(), intensity.getZ()
        );
    }

    @Override
    public int getID() {
        return id;
    }
}
