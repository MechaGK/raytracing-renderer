package saurkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.Material;
import saurkraut.Ray;

import java.awt.*;

/**
 * Class used to represent a sphere
 * Math is based the analytic solution shown at
 * https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
 */
public class Sphere extends Shape {

    public Sphere(Material material, Vector3D position, double radius) {
        super(material, position, new Vector3D(radius, radius, radius));
    }
    
    public Sphere(Material material, Vector3D position, Vector3D scale, Vector3D rotation) {
        super(material, position, scale, rotation);
    }

    @Override
    public Vector3D intersect(Ray ray) {
        // 1. Transform ray to local space
        Vector3D locRayDir = ray.direction;
        Vector3D locRayOrigin = worldToLocal(ray.origin);
        
        Ray locRay = new Ray(locRayOrigin, locRayDir);
        
        // 2. Now test against unit sphere
        double b = 2 * locRayDir.dotProduct(locRayOrigin);
        double c = Math.pow(locRayOrigin.getNorm(), 2) - 1
                ;

        double discriminant = b * b - 4 * c;

        if (discriminant > 0) {
            double t0 = (-b + Math.sqrt(discriminant)) / 2;
            double t1 = (-b - Math.sqrt(discriminant)) / 2;

            if (t0 < 0) { // Point given by t0 is behind the ray
                return localToWorld(locRay.getPoint(t1));
            } else if (t1 < 0) { // Point given by t1 is behind the ray
                return localToWorld(locRay.getPoint(t0));
            } else {
                double t = Math.min(t0, t1);
                return localToWorld(locRay.getPoint(t));
            }
        } else if (discriminant == 0) {
            double t = -(b / 2);

            return localToWorld(locRay.getPoint(t));
        }

        return null;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return point.subtract(position).normalize();
    }

    @Override
    public Color getColor(Vector3D point) {
        Vector3D relativeHit = point.subtract(position);
        relativeHit = relativeHit.normalize();

        double sigma = (1 + Math.atan2(relativeHit.getZ(), relativeHit.getX()) / Math.PI) * 0.5d;
        double theta = Math.acos(relativeHit.getY()) / Math.PI;

        return material.getColor(sigma, theta);
    }
}
