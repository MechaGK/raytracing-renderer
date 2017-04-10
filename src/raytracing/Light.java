package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

/**
 * A class for a spherical light.
 */
public class Light {
    private Vector3D position;
    private float intensity;
    private Color color;

    public Light(Vector3D position, float intensity, Color color) {
        this.position = position;
        this.intensity = intensity;
        this.color = color;
    }
}
