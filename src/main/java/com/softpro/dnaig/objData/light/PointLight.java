package com.softpro.dnaig.objData.light;


import com.softpro.dnaig.utils.Vector3D;

import java.util.Locale;

public class PointLight implements Light {

    int id;

    Vector3D position;
    Vector3D rgb;
    double intensity;


    /**
     * @param position 3D position of the light
     * @param rgb intensity of the light
     */
    public PointLight(Vector3D position, Vector3D rgb){
        this.position = position;
        this.rgb = rgb;
        this.id = 0;
        this.intensity = 1;
    }

    public PointLight(int id, Vector3D position, Vector3D rgb){
        this.position = position;
        this.rgb = rgb;
        this.id = id;
        this.intensity = 1;
    }

    public PointLight(int id, Vector3D position, Vector3D rgb, double intensity){
        this.position = position;
        this.rgb = rgb;
        this.id = id;
        this.intensity = intensity;
    }

    public PointLight(Vector3D position, Vector3D rgb, double intensity){
        this.position = position;
        this.rgb = rgb;
        this.id = 0;
        this.intensity = intensity;
    }

    @Override
    public Vector3D getPosition() {
        return position;
    }

    @Override
    public Vector3D getRgb(){return rgb;}

    @Override
    public Vector3D getRgb(Vector3D fromPosition) {
        return rgb;
    }

    @Override
    public double getIntensity() {
        return intensity;
    }

    @Override
    public String toYaml() {
        return String.format(
                Locale.US,
                """
                -\tposition: %s
                \tKe: %s
                \tintensity: %f
                """, position.toYaml(), rgb.toColorYaml(), intensity
        );
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }
}
