package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // For testing purposes. Should probably be done by some classes
        Vector3D cameraOrigin = new Vector3D(0, 0, 0);
        Vector3D cameraDirection = new Vector3D(0, 0, 1);

        int imageResolutionX = 100;
        int imageResolutionY = 100;

        BufferedImage image = new BufferedImage(imageResolutionX, imageResolutionY,
                BufferedImage.TYPE_INT_ARGB);

        ArrayList<Shape> shapes = new ArrayList<>();

        Shape sphere1 = new Sphere(new Material(Color.red), new Vector3D(0, 0, 5), 4);
        Shape sphere2 = new Sphere(new Material(Color.green), new Vector3D(3, 3, 10), 3);

        shapes.add(sphere1);
        shapes.add(sphere2);

        // Rendering scene to image with orthogonal "camera"
        Camera camera = new OrthogonalCamera(cameraOrigin, cameraDirection, 10, 10);

        Iterator<CameraRay> cameraRays = camera.rays(imageResolutionX, imageResolutionY);
        CameraRay cameraRay;
        Ray ray;

        while(cameraRays.hasNext()) {
             cameraRay = cameraRays.next();
             ray = cameraRay.ray;

            Vector3D closestHit = null;
            Shape closestShape = null;
            double closestHitDistance = Double.MAX_VALUE;

            Vector3D hit;
            double distance;

            for (Shape shape : shapes) {
                hit = shape.intersect(ray);

                if (hit == null) continue;

                distance = hit.distance(cameraOrigin);
                if (distance < closestHitDistance) {
                    closestHit = hit;
                    closestShape = shape;
                    closestHitDistance = distance;
                }
            }

            if (closestHit != null) {
                image.setRGB(cameraRay.x, cameraRay.y, closestShape.colorAtPoint(closestHit).getRGB());
            }
        }

        try {
            File outputFile = new File("test.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("saved image to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
