package saurkraut.lights;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;
import saurkraut.Scene;

/**
 *
 */
public class SpotLight extends Light {
    public Vector3D position;
    public Vector3D direction;
    private float noIntensityT;
    private float fullIntensityT;

    /**
     * Creates a PointLight
     * @param position The position of the new point light
     * @param intensity Intensity of the new point light
     * @param color Color of the new point light
     */
    public SpotLight(Vector3D position, Vector3D direction, float coneAngleMin, float coneAngleMax, Color color, float intensity) {
        super(color, intensity);
        this.position = position;
        this.direction = direction.normalize();
        this.noIntensityT = (float) Math.cos(Math.toRadians(coneAngleMax*0.5));
        this.fullIntensityT = (float) Math.cos(Math.toRadians(coneAngleMin*0.5));
    }
    
    @Override
    public float getIntensity(Vector3D worldPoint) {
        Vector3D directionToPoint = position.subtract(worldPoint).normalize();
        float dotProd = (float) directionToPoint.dotProduct(direction);
        float fallOff;
        
        if (dotProd < noIntensityT) fallOff = 0;
        else if (dotProd >= fullIntensityT) fallOff = 1;
        else fallOff = (dotProd - noIntensityT)/(fullIntensityT - noIntensityT);

        // Magic constants to have an intensity of 100 be bright
        // and lower intensities to gracefully decay
        return fallOff * (100*intensity/(50 + (float)position.distanceSq(worldPoint)));
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
