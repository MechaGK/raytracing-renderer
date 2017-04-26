package sauerkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.coordinates.Coordinate;
import sauerkraut.coordinates.GridCoordinate;
import sauerkraut.materials.Material;
import sauerkraut.Ray;

import java.awt.*;

public class Plane extends Shape {
    private final Vector3D normal;
    private final Vector3D XAxis;
    private final Vector3D YAxis;

    public Plane(Material material, Vector3D position, Vector3D normal) {
        super(material, position);

        this.normal = normal.normalize();

        this.YAxis = new Vector3D(this.normal.getY(), this.normal.getZ(), -this.normal.getX()).normalize();
        this.XAxis = Vector3D.crossProduct(this.normal, this.YAxis).normalize();
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

        //THE ABS PART IS A GROSS HACK THAT CAUSES MIRRORED TILING. PLEASE HELP FIX. :(
        int x = Math.abs((int) (Vector3D.dotProduct(point, XAxis)));
        int y = Math.abs((int) (Vector3D.dotProduct(point, YAxis)));

        Coordinate coord = new GridCoordinate(x, y); //Infinitely large, no width or height.
        return material.getColor(coord);

        //DEPRECATED! >:D
        //return material.getColor(0, 0); //TODO: implement spherical coordinates for plane
    }
}
