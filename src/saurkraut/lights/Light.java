package saurkraut.lights;

import java.awt.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class Light {
    protected Color color;
    protected float intensity;

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
    
    public abstract Vector3D directionFromPoint(Vector3D worldPoint);
    
    public float getIntensity(Vector3D worldPoint) {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
