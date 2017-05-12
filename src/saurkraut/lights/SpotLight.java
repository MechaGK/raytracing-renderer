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
public class SpotLight extends Light {
    public Vector3D position;
    public Vector3D direction;
    public float dotCutOff;

    /**
     * Creates a PointLight
     * @param position The position of the new point light
     * @param intensity Intensity of the new point light
     * @param color Color of the new point light
     */
    public SpotLight(Vector3D position, Vector3D direction, float dotCutOff, float intensity, Color color) {
        super(color, intensity);
        this.position = position;
        this.direction = direction.normalize();
        this.dotCutOff = dotCutOff;
    }
    
    @Override
    public float getIntensity(Vector3D worldPoint) {
        Vector3D directionToPoint = position.subtract(worldPoint).normalize();
        float dotted = (float) directionToPoint.dotProduct(direction);
        if (dotted < dotCutOff) {
            dotted = 0;
        }
        else if (dotted < dotCutOff + 0.1) {
            dotted -= dotCutOff;
            dotted *= 10;
        }
        else {
            dotted = 1;
        }
        //dotted = (dotted < dotCutOff) ? 0 : 1;
            //dotted = (dotted < dotCutOff - 0.1f) ? 0 : (dotted - 0.1f) / (dotCutOff - 0.1f);
        return dotted * intensity / (float) position.distanceSq(worldPoint);
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
