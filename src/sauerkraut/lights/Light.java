package sauerkraut.lights;

import java.awt.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.Scene;

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
    
    public abstract float getContribution(Scene scene, Vector3D worldPoint);
    
    public float getIntensity(Vector3D worldPoint) {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    
}
