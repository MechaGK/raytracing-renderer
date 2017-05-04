package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.shapes.Shape;
import saurkraut.util.ColorUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class Raytracer {
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
    public static BufferedImage renderScene(Scene scene, int resolutionX, int resolutionY) {
        BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
        Iterator<CameraRay> cameraRays = scene.getCamera().raysIterator(resolutionX, resolutionY);

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

            RayHit rayHit = castRay(scene, ray);

            if (rayHit == null) {
                // No shape hit by ray. Continue to next ray.
                image.setRGB(cameraRay.x, cameraRay.y, Color.MAGENTA.getRGB());
                continue;
            }

            saurkraut.shapes.Shape shape = rayHit.shape;
            Vector3D hit = rayHit.point;

            testEnd = System.nanoTime();
            testingTime += testEnd - testStart;


            shadedPoints++;

            shadingStart = System.nanoTime();
            Color shapeColor = shape.getColor(hit);

            Color lightColor = PhongShader.shade(scene, shape, hit, ray.direction);
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

    public static RayHit castRay(Scene scene, Ray ray) {
        Vector3D closestHit = null;
        Shape closestShape = null;
        double closestHitDistance = Double.MAX_VALUE;

        Vector3D hit;
        double distance;

        for (Shape shape : scene.getShapes()) {
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
