package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.DistantLight;
import saurkraut.lights.Light;
import saurkraut.shapes.*;
import saurkraut.util.ColorUtil;
import saurkraut.shapes.Shape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static Scene inappropriateScene;
    public static Scene simpleScene;

    public static void createScenes() {
        inappropriateScene = new Scene();

        Shape sphere1 = new Sphere(new ColoredMaterial(Color.cyan, 0.18f), new Vector3D(0.8, 2, 1.2), 2);
        Shape sphere2 = new Sphere(new ColoredMaterial(Color.cyan, 0.18f), new Vector3D(1, 2, -1.2), 2);

        int i;
        for (i = 0; i < 7; ++i)
            inappropriateScene.add(new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(1 + i, 1 + i * i * 0.04, 0), 1));
        inappropriateScene.add(new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(1 + i, 1 + i * i * 0.04, 0), 1.1f));

        Light light = new DistantLight(new Vector3D(3, 2, 1), 10, Color.pink);
        Light light2 = new DistantLight(new Vector3D(-3, 2, -1), 10, Color.orange);

        inappropriateScene.add(sphere1);
        inappropriateScene.add(sphere2);
        inappropriateScene.add(light);
        inappropriateScene.add(light2);

        simpleScene = new Scene();
        Shape sphere = new Sphere(new ColoredMaterial(Color.white, 0.18f), Vector3D.ZERO, 3);
        light = new DistantLight(new Vector3D(2, -4, 3), 15, Color.white);
        simpleScene.add(sphere);
        simpleScene.add(light);
    }

    public static void main(String[] args) {
        // Creating a scene
        createScenes();

        // Setting up camera
        Vector3D cameraOrigin = new Vector3D(6, 0.5, -10);
        Vector3D cameraDirection = new Vector3D(-0.4, 0, 1);

        // Rendering scene to image and saving to disk
        final int resolutionX = 960;
        final int resolutionY = 600;

        final double aspectRatio = (double) resolutionX / (double) resolutionY;
        final double scale = 10;

        Camera camera = new OrthogonalCamera(cameraOrigin, cameraDirection, scale * aspectRatio, scale);

        BufferedImage image = renderScene(simpleScene, camera, resolutionX, resolutionY);
        saveImage(image, "test.png");
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
     * @param scene       Scene containing the shapes to render
     * @param camera      Camera to render the scene from
     * @param resolutionX Horizontal resolution of the final image
     * @param resolutionY Vertical resolution of the final image
     * @return BufferedImage colored after scene contents
     */
    public static BufferedImage renderScene(Scene scene, Camera camera, int resolutionX, int resolutionY) {
        BufferedImage image = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
        Iterator<CameraRay> cameraRays = camera.raysIterator(resolutionX, resolutionY);

        CameraRay cameraRay;
        Ray ray;

        while (cameraRays.hasNext()) {
            cameraRay = cameraRays.next();
            ray = cameraRay.ray;

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

            if (closestHit != null) {
                Color shapeColor = closestShape.getColor(closestHit);

                Color lightColor = PhongShader.diffuse(scene, closestShape, closestHit);

                Color finalColor = ColorUtil.multiply(shapeColor, lightColor);

                image.setRGB(cameraRay.x, cameraRay.y, finalColor.getRGB());
            }
        }

        return image;
    }


    public static void saveImage(BufferedImage image, String fileName) {
        try {
            File outputFile = new File(fileName);
            ImageIO.write(image, "png", outputFile);
            System.out.println("saved image to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
