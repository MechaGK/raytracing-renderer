package saurkraut;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.Light;
import saurkraut.shapes.Shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import saurkraut.util.ColorUtil;

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
        double closestSquareHitDistance = Double.MAX_VALUE;

        Vector3D hit;
        double distance;

        for (Shape shape : getShapes()) {            
            hit = shape.intersect(ray);

            if (hit == null) continue;
            distance = hit.distanceSq(ray.origin);
            
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

        return new RayHit(closestHit, closestShape);
    }
    
    // TODO: this was hastily implemented, not thought through. Think this through.
    public boolean lineFree(Vector3D a, Vector3D b) {
        Ray rayFromA = new Ray(a, b.subtract(a));
        double squareDistanceAB = a.distanceSq(b);

        Vector3D hit;

        for (Shape shape : getShapes()) {            
            hit = shape.intersect(rayFromA);
            if (hit == null) continue;
            if (hit.distanceSq(a) < squareDistanceAB) return false;
        }
        
        return true;
    }
    
    /**
     * Renders a scene to a image, using given camera and image resolution. This is a very simple
     * renderer only determining pixel color from the color of the first hit shape. The process of
     * getting a pixel's color is:
     * 1. Get a ray from the camera
     * 2. Test if any shape is hit by the ray. If any of them is take the hit closest to the ray's origin
     * 3. If any shape was hit, color the pixel the color of the shape at the point
     * This is done for every ray from the camera. The number of rays are based on the image's resolution
     * <p>
     * Only distant lights are supported
     *
     * @param resolutionX Horizontal resolution of the final image
     * @param resolutionY Vertical resolution of the final image
     * @return BufferedImage colored after scene contents
     */
    public BufferedImage renderScene(int resolutionX, int resolutionY) {
        BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
        Iterator<CameraRay> cameraRays = camera.raysIterator(resolutionX, resolutionY);

        CameraRay cameraRay;
        Ray ray;

        long shadedPoints = 0;

        long shadingTime = 0;
        long shadingStart, shadingEnd;

        long testingTime = 0;
        long testStart, testEnd;

        long startTime = System.nanoTime();

        while (cameraRays.hasNext()) {
            testStart = System.nanoTime();
            cameraRay = cameraRays.next();
            ray = cameraRay.ray;

            RayHit rayHit = castRay(ray);

            if (rayHit == null) {
                // No shape hit by ray. Continue to next ray.
                //image.setRGB(cameraRay.x, cameraRay.y, Color.MAGENTA.getRGB());
                continue;
            }

            Shape shape = rayHit.shape;
            Vector3D hit = rayHit.point;

            testEnd = System.nanoTime();
            testingTime += testEnd - testStart;


            shadedPoints++;

            shadingStart = System.nanoTime();
            Color shapeColor = shape.getColor(hit);

            if (cameraRay.x == 100 && cameraRay.y == 180)
                System.out.println("jjj");
            Color lightColor = PhongShader.shade(this, shape, hit, ray.direction);
            //Color lightColor = UnlitShader.shade(shape, hit);

            //System.out.format("x %d, y %d; %s, %s\n", cameraRay.x, cameraRay.y, shapeColor.toString(), lightColor.toString());

            Color finalColor = ColorUtil.multiply(shapeColor, lightColor);
            shadingEnd = System.nanoTime();
            shadingTime += shadingEnd - shadingStart;

            image.setRGB(cameraRay.x, cameraRay.y, finalColor.getRGB());
        }

        long endTime = System.nanoTime();

        System.out.format("Total render time %d ms\n", ((endTime - startTime) / 1000000));
        System.out.format("Testing for intersections took %d ms\n", testingTime / 1000000);

        System.out.format("Shading took %d ms\n", shadingTime / 1000000);
        System.out.format("Shaded %d points\n", shadedPoints);

        System.out.format("Shading took %f ms per 1000nd point\n", (shadingTime / 1000000f) / (shadedPoints / 1000f));

        return image;
    }
}
