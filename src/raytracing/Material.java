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
