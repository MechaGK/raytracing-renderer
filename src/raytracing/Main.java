package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // Creating a scene
        Scene scene = new Scene();

        Shape sphere1 = new Sphere(new Material(Color.red), new Vector3D(0, 0, 5), 4);
        Shape sphere2 = new Sphere(new Material(Color.green), new Vector3D(3, 3, 10), 3);

        scene.add(sphere1);
        scene.add(sphere2);

        // Setting up camera
        Vector3D cameraOrigin = new Vector3D(0, 0, 0);
        Vector3D cameraDirection = new Vector3D(0, 0, 1);

        Camera camera = new OrthogonalCamera(cameraOrigin, cameraDirection, 10, 10);

        // Rendering scene to image and saving to disk
        int resolutionX = 100;
        int resolutionY = 100;

        BufferedImage image = renderScene(scene, camera, resolutionX, resolutionY);
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
     * @param scene Scene containing the shapes to render
     * @param camera Camera to render the scene from
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

            for (Shape shape : scene) {
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
                image.setRGB(cameraRay.x, cameraRay.y, shapeColor.getRGB());
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
