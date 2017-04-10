package raytracing;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for holding shapes. Just a container for a ArrayList but it could grow smarter...
 */
public class Scene implements Iterable<Shape> {
    private ArrayList<Shape> shapes = new ArrayList<>();

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }
}
