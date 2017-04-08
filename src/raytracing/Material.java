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

    public Material(Color color) {
        this.color = color;
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
