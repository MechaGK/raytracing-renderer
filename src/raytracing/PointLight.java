package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

/**
 *
 */
public class PointLight extends Light {
    private Vector3D position;

    /**
     * Creates a PointLight
     * @param position The position of the new point light
     * @param intensity Intensity of the new point light
     * @param color Color of the new point light
     */
    public PointLight(Vector3D position, float intensity, Color color) {
        this.position = position;
        this.intensity = intensity;
        this.color = color;
    }
}
