package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Ray {
    public final Vector3D origin;
    public final Vector3D direction;

    /**
     * Creates a ray from a given origin and direction.
     *
     * @param origin    The origin of the ray.
     * @param direction The direction of the ray. Is normalized internally.
     */
    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    /**
     * Returns a point on the ray given a constant
     * @param t the constant
     * @return point on the ray
     */
    public Vector3D getPoint(double t) {
        return origin.add(direction.scalarMultiply(t));
    }

    @Override
    public String toString() {
        return String.format("%s, dir: %s", origin, direction);
    }
}
