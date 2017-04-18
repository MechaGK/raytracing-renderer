package raytracing;

import raytracing.lights.Light;
import raytracing.shapes.Shape;

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
}
