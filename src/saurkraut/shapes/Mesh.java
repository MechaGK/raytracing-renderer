package saurkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.OBJLoader;
import saurkraut.Ray;
import saurkraut.RayHit;
import saurkraut.materials.Material;

import java.awt.*;
import java.util.ArrayList;

public class Mesh extends Shape {
    private ArrayList<Triangle> triangles = new ArrayList<>();
    private Cuboid boundingBox;

    public Mesh(Material material, Vector3D position, OBJLoader objLoader) {
        super(material, position);

        for (int i = 0; i < objLoader.face().size() - 2; i += 3) {
            triangles.add(new Triangle(
                    material,
                    position.add(objLoader.vertex().get(objLoader.face().get(i))),
                    position.add(objLoader.vertex().get(objLoader.face().get(i + 1))),
                    position.add(objLoader.vertex().get(objLoader.face().get(i + 2)))));
        }

        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;
        double zMax = Double.MIN_VALUE;

        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;
        double zMin = Double.MAX_VALUE;

        for (Vector3D vertex : objLoader.vertex()) {
            if (vertex.getX() > xMax) {
                xMax = vertex.getX();
            } else if (vertex.getY() > yMax) {
                yMax = vertex.getY();
            } else if (vertex.getZ() > zMax) {
                zMax = vertex.getZ();
            }

            if (vertex.getX() < xMin) {
                xMin = vertex.getX();
            } else if (vertex.getY() < yMin) {
                yMin = vertex.getY();
            } else if (vertex.getZ() < zMin) {
                zMin = vertex.getZ();
            }
        }

        boundingBox = new Cuboid(null,
                position.add(new Vector3D((xMin + xMax) / 2, (yMin + yMax) / 2, (zMin + zMax) / 2)),
                new Vector3D((Math.abs(xMin) + Math.abs(xMax)) / 2, (Math.abs(yMin) + Math.abs(yMax)) / 2, (Math.abs(zMin) + Math.abs(zMax)) / 2),
                new Vector3D(0, 0, 0));
    }

    @Override
    public RayHit intersect(Ray ray) {
        RayHit boundingBoxHit = boundingBox.intersect(ray);

        if (boundingBoxHit == null) {
            return null;
        }

        ArrayList<RayHit> hits = new ArrayList<>();
        RayHit intersection;

        for (Triangle triangle : triangles) {
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
