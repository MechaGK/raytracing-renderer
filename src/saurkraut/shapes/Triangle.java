package saurkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.Ray;
import saurkraut.materials.Material;

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
    public Vector3D intersect(Ray ray) {
        Vector3D point = super.intersect(ray);

        double alpha = area(point, b, c) / area;
        double beta = area(point, c, a) / area;
        double gamma = 1d - alpha - beta;

        if (inRange(alpha) && inRange(beta) && inRange(gamma)) {
            return point;
        }

        return null;
    }

    public static boolean inRange(double v) {
        return 0 <= v && v <= 1;
    }

    public static double area(Vector3D a, Vector3D b, Vector3D c) {
        return b.subtract(a).crossProduct(c.subtract(a)).getNorm() / 2d;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return super.getNormal(point);
    }

    @Override
    public Color getColor(Vector3D point) {
        // TODO: Texture coordinates for triangle
        return super.getColor(point);
    }
}
