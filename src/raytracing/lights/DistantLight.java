package raytracing.lights;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import raytracing.lights.Light;

import java.awt.*;

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
}
