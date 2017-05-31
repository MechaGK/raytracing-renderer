package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.shapes.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Raytracer {
    private static class ImageRayHit {
        public final int imageX;
        public final int imageY;
        public final Ray ray;
        public final Vector3D point;
        public final Shape shape;
        public final Vector3D normal;

        private ImageRayHit(int imageX, int imageY, Ray ray, RayHit hit) {
            this.imageX = imageX;
            this.imageY = imageY;
            this.ray = ray;
            this.point = hit.point;
            this.shape = hit.shape;
            this.normal = hit.normal;
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

        int initialIntersectionTests = scene.tests.get();
        int initialIntersectionSaved = scene.boundingboxSavings.get();

        shadingStart = System.nanoTime();
        ArrayList<ShadingPoint> shadingPoints = shadePoints(scene, shader, points);
        shadingEnd = System.nanoTime();

        int shadingIntersectionTests = scene.tests.get() - initialIntersectionTests;
        int shadingIntersectionSaved = scene.boundingboxSavings.get() - initialIntersectionSaved;

        imageStart = System.nanoTime();
        BufferedImage image = createImage(resolutionX, resolutionY, null, shadingPoints);
        imageEnd = System.nanoTime();


        System.out.println("---- Stats");
        System.out.printf("Shapes in scene: %d\n", scene.getShapes().size());
        System.out.printf("Lights in scene: %d\n", scene.getLights().size());
        System.out.printf("Pixels to render: %d\n\n", resolutionX * resolutionY);
        System.out.println("---- Timing");
        System.out.printf("Initial testing for intersections (ms): %d\n", (testingEnd - testingStart) / 1000000);
        System.out.format("Shading (ms): %d\n", (shadingEnd - shadingStart) / 1000000);
        System.out.format("Total render time (ms): %d\n\n", ((shadingEnd - testingStart) / 1000000));
        System.out.println("---- Numbers");
        System.out.printf("Initial intersection tests: %d\n", initialIntersectionTests);
        System.out.printf("Initial intersection tests saved: %d\n", initialIntersectionSaved);
        System.out.printf("Shading intersection tests: %d\n", shadingIntersectionTests);
        System.out.printf("Shading intersection tests saved: %d\n", shadingIntersectionSaved);
        System.out.printf("Total intersection tests: %d\n", initialIntersectionTests + shadingIntersectionTests);
        System.out.printf("Total intersection saved: %d\n\n", initialIntersectionSaved + shadingIntersectionSaved);
        System.out.printf("Points shaded: %d\n\n", shadingPoints.size());
        System.out.println("----------");


        return image;
    }

    public static List<BufferedImage> renderSceneStepByStep(Scene scene, int resolutionX, int resolutionY, Shader shader) {
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

        Shader booleanShader = new SingleColorShader(Color.white);
        ArrayList<ShadingPoint> hitShaded = shadePoints(scene, booleanShader, points);

        Shader depthShader = new DepthShader(maxDistance, false);
        ArrayList<ShadingPoint> depthShaded = shadePoints(scene, depthShader, points);

        Shader depthShaderColor = new DepthShader(maxDistance, true);
        ArrayList<ShadingPoint> depthShadedColored = shadePoints(scene, depthShaderColor, points);

        ArrayList<ShadingPoint> phongShaded = shadePoints(scene, shader, points);

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

            RayHit rayHit = scene.castRay(ray);

            if (rayHit == null) continue; // Nothing hit continue to next ray

            hits.add(new ImageRayHit(cameraRay.x, cameraRay.y, ray, rayHit));
        }

        return hits;
    }

    public static ArrayList<ShadingPoint> shadePoints(Scene scene, Shader shader, List<ImageRayHit> hits) {
        ArrayList<ShadingPoint> shadingPoints = new ArrayList<>();

        Color lightColor;
        for (ImageRayHit hit : hits) {
            lightColor = shader.shade(scene, hit.shape, hit.point, hit.normal, hit.ray.direction);

            shadingPoints.add(new ShadingPoint(hit.imageX, hit.imageY, lightColor));
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
}
