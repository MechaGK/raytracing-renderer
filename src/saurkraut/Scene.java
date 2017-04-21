package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.Light;
import saurkraut.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for holding shapes and lights.
 * Just a container for a ArrayLists but it could grow smarter...
 */
public class Scene {
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Light> lights = new ArrayList<>();

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
    }

    public void addShapes(Shape... shapes) {
        for (Shape shape : shapes) {
            add(shape);
        }
    }

    public void addLights(Light... lights) {
        for (Light light : lights) {
            add(light);
        }
    }

    public void add(Light light) {
        lights.add(light);
    }

    public void remove(Light light) {
        lights.remove(light);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<Light> getLights() {
        return lights;
    }

    /**
     * Find first shape hit by ray (by distance to ray origin). Returns null if no hit
     * @param ray Ray to test with
     * @return RayHit with first shape and where it was hit. Null if nothing was hit
     */
    public RayHit castRay(Ray ray) {
        Vector3D closestHit = null;
        Shape closestShape = null;
        double closestHitDistance = Double.MAX_VALUE;

        Vector3D hit;
        double distance;

        for (Shape shape : getShapes()) {
            hit = shape.intersect(ray);

            if (hit == null) continue;

            distance = hit.distance(ray.origin);
            if (distance < closestHitDistance) {
                closestHit = hit;
                closestShape = shape;
                closestHitDistance = distance;
            }
        }

        if (closestShape == null) {
            // Return null as no shape was hit
            return null;
        }

        return new RayHit(closestHit, closestShape);
    }
}
