package saurkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.materials.Material;
import saurkraut.Ray;

import java.awt.*;

public class Plane extends Shape {
    private final Vector3D normal;

    public Plane(Material material, Vector3D position, Vector3D normal) {
        super(material, position);

        this.normal = normal.normalize();
    }

    @Override
    public Vector3D intersect(Ray ray) {
        double test = ray.direction.dotProduct(normal);

        if (test == 0) {
            return null;
        }

        double scalar = position.subtract(ray.origin).dotProduct(normal);
        scalar = scalar / test;

        if (scalar < 0) {
            return null;
        }

        return ray.getPoint(scalar);
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return normal;
    }

    @Override
    public Color getColor(Vector3D point) {
        return material.getColor(0, 0); //TODO: implement spherical coordinates for plane
    }
}
