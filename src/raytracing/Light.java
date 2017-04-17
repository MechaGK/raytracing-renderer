package raytracing;

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
