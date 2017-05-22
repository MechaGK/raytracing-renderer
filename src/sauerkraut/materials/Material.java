package sauerkraut.materials;

import sauerkraut.coordinates.Coordinate;
import java.awt.*;

/**
 * Class to hold information for rendering materials
 */
public abstract class Material {
    public Material(float albedo, float specularExponent, float specularStrength) {
        this.albedo = albedo;
        this.specularExponent = specularExponent;
        this.specularStrength = specularStrength;
    }
    public Material(float albedo, float specularExponent) {
        this.albedo = albedo;
        this.specularExponent = specularExponent;
    }
    public Material(float albedo) {
        this.albedo = albedo;
    }

    /**
     * Albedo is a measure for reflectance or optical brightness.
     * src: https://en.wikipedia.org/wiki/Albedo
     */
    public float albedo = 0.18f;
    public float specularExponent = 50;
    public float specularStrength = 0.2f;

    public abstract Color getColor(Coordinate coord);
}
