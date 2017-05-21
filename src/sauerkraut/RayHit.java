package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.shapes.Shape;

/**
 * Class for containing information about a hit by ray.
 */
public class RayHit {
    public final Vector3D point;
    public final Shape shape;
    public final Vector3D normal;

    public RayHit(Vector3D point, Shape shape, Vector3D normal) {
        this.point = point;
        this.shape = shape;
        this.normal = normal;
    }
}
