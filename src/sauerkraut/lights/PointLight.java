package sauerkraut.lights;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;
import sauerkraut.Scene;

/**
 *
 */
public class PointLight extends Light {
    public Vector3D position;

    /**
     * Creates a PointLight
     * @param position The position of the new point light
     * @param intensity Intensity of the new point light
     * @param color Color of the new point light
     */
    public PointLight(Vector3D position, float intensity, Color color) {
        super(color, intensity);
        this.position = position;
    }
    
    @Override
    public float getIntensity(Vector3D point) {
        return intensity / (float) position.distanceSq(point);
    }
    
    @Override
    public float getContribution(Scene scene, Vector3D worldPoint) {
        if (scene.lineFree(worldPoint, position)) {
            return getIntensity(worldPoint);
        }
        return 0;
    }

    @Override
    public Vector3D directionFromPoint(Vector3D worldPoint) {
        // Destination - Source
        return position.subtract(worldPoint).normalize();
    }
}
