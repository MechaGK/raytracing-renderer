package sauerkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.Ray;
import sauerkraut.RayHit;
import sauerkraut.materials.Material;

import java.awt.*;

public class Triangle extends Plane {
    private Vector3D a, b, c;
    private double area;

    private Triangle(Material material, Vector3D normal, Vector3D a, Vector3D b, Vector3D c) {
        super(material, a, normal);
        this.a = a;
        this.b = b;
        this.c = c;
        area = area(a, b, c);
    }

    public Triangle(Material material, Vector3D a, Vector3D b, Vector3D c) {
        // TODO: This may not be the correct order for normal
        this(material, b.subtract(a).crossProduct(c.subtract(a)), a, b, c);
    }

    @Override
    protected RayHit intersect(Ray ray) {
        RayHit hit = super.intersect(ray);

        if (hit == null) {
            return null;
        }

        Vector3D point = hit.point;

        if (sameSide(point, a, b, c) && sameSide(point, b, a, c) && sameSide(point, c, a, b)) {
            return hit;
        }

        return null;
    }

    public static boolean sameSide(Vector3D p1, Vector3D p2, Vector3D a, Vector3D b) {
        Vector3D cp1 = b.subtract(a).crossProduct(p1.subtract(a));
        Vector3D cp2 = b.subtract(a).crossProduct(p2.subtract(a));

        return (cp1.dotProduct(cp2) >= 0);
    }



    public static boolean inRange(double v) {
        return 0 <= v && v <= 1;
    }

    public static double area(Vector3D a, Vector3D b, Vector3D c) {
        return (b.subtract(a)).crossProduct(c.subtract(a)).getNorm() / 2d;
    }

    @Override
    public Color getColor(Vector3D point) {
        // TODO: Texture coordinates for triangle
        return super.getColor(point);
    }
}
