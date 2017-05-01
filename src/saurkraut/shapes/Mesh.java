package saurkraut.shapes;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.OBJLoader;
import saurkraut.Ray;
import saurkraut.materials.ColoredMaterial;
import saurkraut.materials.Material;

import java.awt.*;
import java.util.ArrayList;

public class Mesh extends Shape {
    private ArrayList<Triangle> triangles;

    public Mesh(Material material, Vector3D position, OBJLoader objLoader) {
        super(material, position);

        for (int i = 0; i < objLoader.face().size() - 3; i++) {
            triangles.add(new Triangle(
                    material,
                    objLoader.vertex().get(objLoader.face().get(i)),
                    objLoader.vertex().get(objLoader.face().get(i + 1)),
                    objLoader.vertex().get(objLoader.face().get(i + 2))));
        }
    }

    @Override
    public Vector3D intersect(Ray ray) {
        // TODO: bounding box test
        ArrayList<Vector3D> points = new ArrayList<>();
        Vector3D intersection;

        for(Triangle triangle : triangles) {
            intersection = triangle.intersect(ray);

            if (intersection != null) {
                points.add(intersection);
            }
        }

        if (points.isEmpty()) {
            return null;
        }

        if (points.size() == 1) {
            return points.get(0);
        }

        Vector3D finalPoint = null;
        double minDistance = Double.MAX_VALUE;
        double distance;

        for (Vector3D point : points) {
            distance = Vector3D.distance(ray.origin, point);

            if (distance < minDistance) {
                finalPoint = point;
                minDistance = distance;
            }
        }

        return finalPoint;
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return Vector3D.ZERO; //TODO: Implement method
    }

    @Override
    public Color getColor(Vector3D point) {
        return Color.white; //TODO: Implement method
    }
}
