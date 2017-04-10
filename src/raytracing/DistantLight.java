package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

/**
 * Light which lights the whole scene evenly from a specific direction
 */
public class DistantLight extends Light {
    private Vector3D direction;

    public DistantLight(Vector3D direction, float intensity, Color color) {
        this.direction = direction;
        this.intensity = intensity;
        this.color = color;
    }
}
