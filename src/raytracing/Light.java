package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

/**
 * A class for a spherical light.
 */
public abstract class Light {
    protected Color color;
    protected float intensity;

    public Light(Color color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }
}
