package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.Light;
import saurkraut.shapes.Shape;
import saurkraut.util.ColorUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for holding shapes and lights.
 * Just a container for a ArrayLists but it could grow smarter...
 */
public class Scene {
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Light> lights = new ArrayList<>();
    
    private Camera camera;

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

}
