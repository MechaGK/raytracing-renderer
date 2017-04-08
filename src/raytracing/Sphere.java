package raytracing;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class used to represent a sphere
 * Math is based the analytic solution shown at
 * https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
 */
public class Sphere extends Shape {
    protected float radius;

    public Sphere(Material material, Vector3D position, float radius) {
        this.material = material;
        this.position = position;
        this.radius = radius;
    }

    @Override
    public Vector3D intersect(Ray ray) {
        double b = 2 * ray.direction.dotProduct(ray.origin.subtract(position));
        double c = Math.pow(ray.origin.subtract(position).getNorm(), 2) - radius * radius;

        double discriminant = b * b - 4 * c;

        if (discriminant > 0) {
            double t0 = (-b + Math.sqrt(discriminant)) / 2;
            double t1 = (-b - Math.sqrt(discriminant)) / 2;

            if (t0 < 0) { // Point given by t0 is behind the ray
                return ray.getPoint(t1);
            } else if (t1 < 0) { // Point given by t1 is behind the ray
                return ray.getPoint(t0);
            } else {
                double t = Math.min(t0, t1);
                return ray.getPoint(t);
            }
        } else if (discriminant == 0) {
            double t = -(b / 2);

            return ray.getPoint(t);
        }

        return null;
    }

    @Override
    public Vector3D getNormalAtPoint(Vector3D point) {
        return point.subtract(position).normalize();
    }
}
