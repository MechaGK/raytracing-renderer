package sauerkraut.materials;

import sauerkraut.coordinates.Coordinate;

import java.awt.*;

/**
 * Class to hold information for rendering materials
 */
public abstract class Material {
    public Material(float albedo) {
        this.albedo = albedo;
    }

    /**
     * Albedo is a measure for reflectance or optical brightness.
     * src: https://en.wikipedia.org/wiki/Albedo
     */
    public float albedo = 0.18f;

    /**
     * Returns color based on spherical coordinates
     *
     * @param sigma Sigma
     * @param theta Theta
     * @return color at spherical coordinates
     */
    public abstract Color getColor(Coordinate coord);
}
