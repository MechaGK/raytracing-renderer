package saurkraut.lights;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;
import saurkraut.Ray;
import saurkraut.Scene;

/**
 * Light which lights the whole scene evenly from a specific direction
 */
public class DistantLight extends Light {
    public Vector3D direction;

    /**
     * Creates a new distant light
     * @param direction The direction of the new point light. Is normalized internally.
     * @param intensity Intensity of the new point light
     * @param color Color of the new point light
     */
    public DistantLight(Vector3D direction, float intensity, Color color) {
        super(color, intensity);
        this.direction = direction.normalize();
    }
    
    @Override
    public float getContribution(Scene scene, Vector3D worldPoint) {
        if (scene.rayFree(new Ray(worldPoint, directionFromPoint(worldPoint)))) {
            return getIntensity(worldPoint);
        }
        return 0;
    }

    @Override
    public Vector3D directionFromPoint(Vector3D worldPoint) {
        return direction.negate();
    }
}
