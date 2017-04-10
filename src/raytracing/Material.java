package raytracing;

import java.awt.*;

/**
 * Class to hold information for rendering materials
 */
public class Material {
    /**
     * Color of the material
     */
    public Color color;

    /**
     * Albedo is a measure for reflectance or optical brightness.
     * src: https://en.wikipedia.org/wiki/Albedo
     */
    public float albedo = 0.18f;

    public Material(Color color, float albedo) {
        this.color = color;
        this.albedo = albedo;
    }

    /**
     * Returns color based on spherical coordinates
     * @param sigma Sigma
     * @param theta Theta
     * @return color at spherical coordinates
     */
    public Color getColor(double sigma, double theta) {
        return color;
    }
}
