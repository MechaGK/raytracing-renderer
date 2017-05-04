package saurkraut.lights;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;
import saurkraut.Ray;
import saurkraut.RayHit;
import saurkraut.Raytracer;
import saurkraut.Scene;

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
        super(color, intensity);
        this.position = position;
    }
    
    @Override
    public float getIntensity(Scene scene, Vector3D point) {
        return intensity / (float) position.distanceSq(point);
    }

    @Override
    public Vector3D getDirectionFromPoint(Vector3D worldPoint) {
        // Destination - Source
        return position.subtract(worldPoint).normalize();
    }
}
