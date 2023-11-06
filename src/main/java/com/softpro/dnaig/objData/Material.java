package com.softpro.dnaig.objData;


/**
 * Represents a material in a 3D model, including properties like name, color coefficients, and more.
 */
public class Material {

    // Material name.
    private String matName;
    // Ambient color RGB value.
    private float[] ka;
    // Diffuse color RGB value.
    private float[] kd;
    // Specular color RGB value.
    private float[] ks;
    // Emissive coefficient RGB value.
    private float[] ke;
    // Dissolve value.
    private float d;
    // Specular highlights value.
    private float ns;
    // Optical density value.
    private float ni;
    // Illumination model value.
    private int illum;
    // Color texture path.
    private String mapKd;

    /**
     * Creates a default Material with all properties set to default values.
     */
    public Material() {
        matName = "default";
        ka = new float[3];
        kd = new float[3];
        ks = new float[3];
        ke = new float[3];
        d = 0;
        ns = 0;
        ni = 0;
        illum = 0;
        mapKd = "";
    }

    /**
     * Creates a Material with specified properties.
     *
     * @param matName The name of the material.
     * @param ka      The ambient color coefficients.
     * @param kd      The diffuse color coefficients.
     * @param ks      The specular color coefficients.
     * @param ke      The emissive coefficients.
     * @param d       The dissolve (transparency).
     * @param ns      The specular highlights.
     * @param ni      The optical density.
     * @param illum   The illumination model.
     * @param mapKd   The color texture file.
     */
    public Material(String matName, float[] ka, float[] kd, float[] ks, float[] ke, float d, float ns, float ni, int illum, String mapKd) {
        this.matName = matName;
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.ke = ke;
        this.d = d;
        this.ns = ns;
        this.ni = ni;
        this.illum = illum;
        this.mapKd = mapKd;
    }

    /**
     * Gets the name of the material.
     *
     * @return The name of the material.
     */
    public String getMatName() {
        return matName;
    }

    /**
     * Gets the ambient color coefficients as an array of floats.
     *
     * @return The ambient color coefficients.
     */
    public float[] getKa() {
        return ka;
    }

    /**
     * Gets the diffuse color coefficients as an array of floats.
     *
     * @return The diffuse color coefficients.
     */
    public float[] getKd() {
        return kd;
    }

    /**
     * Gets the specular color coefficients as an array of floats.
     *
     * @return The specular color coefficients.
     */
    public float[] getKs() {
        return ks;
    }

    /**
     * Gets the emissive coefficients as an array of floats.
     *
     * @return The emissive coefficients.
     */
    public float[] getKe() {
        return ke;
    }

    /**
     * Gets the dissolve (transparency) value.
     *
     * @return The dissolve value.
     */
    public float getD() {
        return d;
    }

    /**
     * Gets the specular highlights value.
     *
     * @return The specular highlights value.
     */
    public float getNs() {
        return ns;
    }

    /**
     * Gets the optical density value.
     *
     * @return The optical density value.
     */
    public float getNi() {
        return ni;
    }

    /**
     * Gets the illumination model value.
     *
     * @return The illumination model value.
     */
    public int getIllum() {
        return illum;
    }

    /**
     * Gets the color texture file associated with the material.
     *
     * @return The color texture file path.
     */
    public String getMapKd() {
        return mapKd;
    }

    /**
     * Sets the name of the material to a specified string.
     *
     * @param matName The new name for the material.
     */
    public void setMatName(String matName) {
        this.matName = matName;
    }

    /**
     * Sets the ambient color coefficients to a specified array of floats.
     *
     * @param ka The new ambient color coefficients.
     */
    public void setKa(float[] ka) {
        this.ka = ka;
    }

    /**
     * Sets the diffuse color coefficients to a specified array of floats.
     *
     * @param kd The new diffuse color coefficients.
     */
    public void setKd(float[] kd) {
        this.kd = kd;
    }

    /**
     * Sets the specular color coefficients to a specified array of floats.
     *
     * @param ks The new specular color coefficients.
     */
    public void setKs(float[] ks) {
        this.ks = ks;
    }

    /**
     * Sets the emissive coefficients to a specified array of floats.
     *
     * @param ke The new emissive coefficients.
     */
    public void setKe(float[] ke) {
        this.ke = ke;
    }

    /**
     * Sets the dissolve (transparency) value to a specified float.
     *
     * @param d The new dissolve value.
     */
    public void setD(float d) {
        this.d = d;
    }

    /**
     * Sets the specular highlights value to a specified float.
     *
     * @param ns The new specular highlights value.
     */
    public void setNs(float ns) {
        this.ns = ns;
    }

    /**
     * Sets the optical density value to a specified float.
     *
     * @param ni The new optical density value.
     */
    public void setNi(float ni) {
        this.ni = ni;
    }

    /**
     * Sets the illumination model value to a specified integer.
     *
     * @param illum The new illumination model value.
     */
    public void setIllum(int illum) {
        this.illum = illum;
    }

    /**
     * Sets the color texture file path to a specified string.
     *
     * @param mapKd The new color texture file path.
     */
    public void setMapKd(String mapKd) {
        this.mapKd = mapKd;
    }

    /**
     * Provides a formatted string representation of the Material, including its name, color coefficients,
     * transparency, specular highlights, optical density, illumination model, and color texture file path.
     *
     * @return A string representation of the Material.
     */
    @Override
    public String toString() {
        return String.format("""
                material name: %s
                Ka: r(%f) g(%f) b(%f)
                Kd: r(%f) g(%f) b(%f)
                Ks: r(%f) g(%f) b(%f)
                Ke: r(%f) g(%f) b(%f)
                d: %f
                Ns: %f
                Ni: %f
                illum: %d
                map_Kd: %s
                """, matName, ka[0], ka[1], ka[2], kd[0], kd[1], kd[2], ks[0], ks[1], ks[2], ke[0], ke[1], ke[2], d, ns, ni, illum, mapKd);
    }
}
