package sauerkraut;

import sauerkraut.lights.Light;
import sauerkraut.shapes.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class for holding shapes and lights.
 * Just a container for a ArrayLists but it could grow smarter...
 */
public class Scene {
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Light> lights = new ArrayList<>();
    
    private Camera camera;
    public AtomicInteger tests = new AtomicInteger(0);
    public AtomicInteger boundingboxSavings = new AtomicInteger(0);

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return this.camera;
    }
    
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
    
    public RayHit castRay(Ray ray) {
        RayHit closestHit = null;
        Shape closestShape = null;
        double closestSquareHitDistance = Double.MAX_VALUE;

        RayHit hit;
        double distance;

        for (Shape shape : getShapes()) {
            hit = shape.intersect(ray, this);

            if (hit == null) continue;
            distance = hit.point.distanceSq(ray.origin);

            if (distance < closestSquareHitDistance) {
                closestHit = hit;
                closestShape = shape;
                closestSquareHitDistance = distance;
            }
        }

        if (closestShape == null) {
            // Return null as no shape was hit
            return null;
        }

        return closestHit;
    }
    
    public boolean rayFree(Ray ray) {
        for (Shape shape : getShapes())
            if (shape.intersect(ray, this) != null) return false;
        return true;
    }
    
    public boolean rayFree(Ray ray, double maxDistanceSquared) {
        RayHit hit;
        for (Shape shape : getShapes()) {
            hit = shape.intersect(ray, this);
            if (hit != null && hit.point.distanceSq(ray.origin) < maxDistanceSquared) return false;
        }
        return true;
    }
    
    public boolean lineFree(Vector3D a, Vector3D b) {
        return rayFree(new Ray(a, b.subtract(a)), a.distanceSq(b));
    }
}
