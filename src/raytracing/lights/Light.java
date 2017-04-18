package raytracing.lights;

import java.awt.*;

/**
 * A class for a spherical light.
 */
public abstract class Light {
    private Color color;
    private float intensity;

    public Light(Color color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
