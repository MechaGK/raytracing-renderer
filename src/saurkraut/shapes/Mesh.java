package saurkraut.shapes;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.OBJLoader;
import saurkraut.Ray;
import saurkraut.RayHit;
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
    public RayHit intersect(Ray ray) {
        // TODO: bounding box test
        ArrayList<RayHit> hits = new ArrayList<>();
        RayHit intersection;

        for(Triangle triangle : triangles) {
            intersection = triangle.intersect(ray);

            if (intersection != null) {
                hits.add(intersection);
            }
        }

        if (hits.isEmpty()) {
            return null;
        }

        if (hits.size() == 1) {
            return hits.get(0);
        }

        RayHit finalHit = null;
        double minDistance = Double.MAX_VALUE;
        double distance;

        for (RayHit hit : hits) {
            distance = Vector3D.distance(ray.origin, hit.point);

            if (distance < minDistance) {
                finalHit = hit;
                minDistance = distance;
            }
        }

        return finalHit;
    }

    @Override
    public Color getColor(Vector3D point) {
        return Color.white; //TODO: Implement method
    }
}
