package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.shapes.Shape;
import saurkraut.util.ColorUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

public class Raytracer {
    private static class ImageRayHit {
        public final int imageX;
        public final int imageY;
        public final Ray ray;
        public final Vector3D point;
        public final Shape shape;

        private ImageRayHit(int imageX, int imageY, Ray ray, Vector3D point, Shape shape) {
            this.imageX = imageX;
            this.imageY = imageY;
            this.ray = ray;
            this.point = point;
            this.shape = shape;
        }
    }

    private static class ShadingPoint {
        public final int imageX;
        public final int imageY;
        public final Color color;

        private ShadingPoint(int imageX, int imageY, Color color) {
            this.imageX = imageX;
            this.imageY = imageY;
            this.color = color;
        }
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
     * @param scene Scene to render.
     * @param shader Shader used for shading
     * @param resolutionX Horizontal resolution of the final image
     * @param resolutionY Vertical resolution of the final image
     * @return BufferedImage colored after scene contents
     */
    public static BufferedImage renderScene(Scene scene, Shader shader, int resolutionX, int resolutionY) {
        long testingStart, testingEnd;
        long shadingStart, shadingEnd;
        long imageStart, imageEnd;

        testingStart = System.nanoTime();
        ArrayList<ImageRayHit> points = getPoints(scene, resolutionX, resolutionY);
        testingEnd = System.nanoTime();

        shadingStart = System.nanoTime();
        ArrayList<ShadingPoint> shadingPoints = shadePoints(scene, shader, points);
        shadingEnd = System.nanoTime();

        imageStart = System.nanoTime();
        BufferedImage image = createImage(resolutionX, resolutionY, null, shadingPoints);
        imageEnd = System.nanoTime();

        System.out.format("Total render time %d ms\n", ((shadingEnd - testingStart) / 1000000));
        System.out.format("Testing for intersections took %d ms\n", (testingEnd - testingStart) / 1000000);

        System.out.format("Shading took %d ms\n", (shadingEnd - shadingStart) / 1000000);
        System.out.format("Shaded %d points\n", shadingPoints.size());

        System.out.format("Shading took %f ms per 1000nd point\n", ((shadingEnd - shadingStart) / 1000000f) / (shadingPoints.size() / 1000f));

        System.out.format("Creating image took %d ms\n", (imageEnd - imageStart) / 1000000);

        return image;
    }

    public static List<BufferedImage> renderSceneStepByStep(Scene scene, int resolutionX, int resolutionY) {
        ArrayList<ImageRayHit> points = getPoints(scene, resolutionX, resolutionY);

        double distance;
        double maxDistance = Double.MIN_VALUE;
        Vector3D cameraPosition = scene.getCamera().position;
        for (ImageRayHit point : points) {
            distance = cameraPosition.distance(point.point);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        maxDistance = Math.min(maxDistance, 50);

        Shader booleanShader = new BooleanShader(Color.white);
        ArrayList<ShadingPoint> hitShaded = shadePoints(scene, booleanShader, points);

        Shader depthShader = new DepthShader(maxDistance, false);
        ArrayList<ShadingPoint> depthShaded = shadePoints(scene, depthShader, points);

        Shader depthShaderColor = new DepthShader(maxDistance, true);
        ArrayList<ShadingPoint> depthShadedColored = shadePoints(scene, depthShaderColor, points);

        Shader phongShader = new PhongShader();
        ArrayList<ShadingPoint> phongShaded = shadePoints(scene, phongShader, points);

        List<BufferedImage> images = new ArrayList<>();

        images.add(createImage(resolutionX, resolutionY, Color.MAGENTA, hitShaded));
        images.add(createImage(resolutionX, resolutionY, Color.MAGENTA, depthShaded));
        images.add(createImage(resolutionX, resolutionY, Color.MAGENTA, depthShadedColored));
        images.add(createImage(resolutionX, resolutionY, Color.MAGENTA, phongShaded));

        return images;
    }

    public static ArrayList<ImageRayHit> getPoints(Scene scene, int resolutionX, int resolutionY) {
        ArrayList<ImageRayHit> hits = new ArrayList<>();
        Iterator<CameraRay> cameraRays = scene.getCamera().raysIterator(resolutionX, resolutionY);

        CameraRay cameraRay;
        Ray ray;
        while (cameraRays.hasNext()) {
            cameraRay = cameraRays.next();
            ray = cameraRay.ray;

            RayHit rayHit = castRay(scene, ray);

            if (rayHit == null) continue; // Nothing hit continue to next ray

            Shape shape = rayHit.shape;
            Vector3D hit = rayHit.point;

            hits.add(new ImageRayHit(cameraRay.x, cameraRay.y, ray, rayHit.point, shape));
        }

        return hits;
    }

    public static ArrayList<ShadingPoint> shadePoints(Scene scene, Shader shader, List<ImageRayHit> hits) {
        ArrayList<ShadingPoint> shadingPoints = new ArrayList<>();

        Color shapeColor;
        Color lightColor;
        Color finalColor;
        for (ImageRayHit hit : hits) {
            shapeColor = hit.shape.getColor(hit.point);
            lightColor = shader.shade(scene, hit.shape, hit.point, hit.ray.direction);
            finalColor = ColorUtil.multiply(shapeColor, lightColor);

            shadingPoints.add(new ShadingPoint(hit.imageX, hit.imageY, finalColor));
        }

        return shadingPoints;
    }

    public static BufferedImage createImage(int resolutionX, int resolutionY, Color clearColor, List<ShadingPoint> shadingPoints) {
        BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);

        if (clearColor != null) {
            Graphics2D    graphics = image.createGraphics();

            graphics.setPaint ( clearColor );
            graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() );
        }

        for (ShadingPoint shadingPoint : shadingPoints) {
            image.setRGB(shadingPoint.imageX, shadingPoint.imageY, shadingPoint.color.getRGB());
        }

        return image;
    }

    public static RayHit castRay(Scene scene, Ray ray) {
        Vector3D closestHit = null;
        Shape closestShape = null;
        double closestSquareHitDistance = Double.MAX_VALUE;

        Vector3D hit;
        double distance;

        for (Shape shape : scene.getShapes()) {
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
}
