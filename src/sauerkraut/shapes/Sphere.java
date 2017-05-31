package sauerkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.RayHit;
import sauerkraut.coordinates.Coordinate;
import sauerkraut.coordinates.SphericalCoordinate;
import sauerkraut.materials.Material;
import sauerkraut.Ray;

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
    protected RayHit intersect(Ray ray) {
        // 1. Transform ray to local space
        Ray locRay = new Ray(pointToLocal(ray.origin), vectorToLocal(ray.direction));
                
        // 2. Now test against unit sphere
        double b = 2 * locRay.direction.dotProduct(locRay.origin);
        double c = Math.pow(locRay.origin.getNorm(), 2) - 1;

        double discriminant = b * b - 4 * c;

        if (discriminant > 0) {
            double t0 = (-b + Math.sqrt(discriminant)) / 2;
            double t1 = (-b - Math.sqrt(discriminant)) / 2;

            if (t0 < 0 && t1 < 0) { // Both points are behind the ray
                return null;
            } else if (t0 < 0) { // Point given by t0 is behind the ray
                Vector3D point = pointToWorld(locRay.getPoint(t1));
                return new RayHit(point, this, locRay.getPoint(t1).normalize());
            } else if (t1 < 0) { // Point given by t1 is behind the ray
                Vector3D point = pointToWorld(locRay.getPoint(t0));
                return new RayHit(point, this, locRay.getPoint(t0).normalize());
            } else {
                double t = Math.min(t0, t1);
                Vector3D point = pointToWorld(locRay.getPoint(t));
                return new RayHit(point, this, locRay.getPoint(t).normalize());
            }
        } else if (discriminant == 0) {
            double t = -(b / 2);

            Vector3D point = pointToWorld(locRay.getPoint(t));
            return new RayHit(point, this, locRay.getPoint(t).normalize());
        }

        return null;
    }

    @Override
    public Color getColor(Vector3D point) {
        Vector3D relativeHit = point.subtract(position);
        relativeHit = relativeHit.normalize();

        double sigma = (1 + Math.atan2(relativeHit.getZ(), relativeHit.getX()) / Math.PI) * 0.5d;
        double theta = Math.acos(relativeHit.getY()) / Math.PI;

        SphericalCoordinate coord = new SphericalCoordinate(sigma, theta);
        return material.getColor(coord);
        //return material.getColor(sigma, theta);
    }
}
