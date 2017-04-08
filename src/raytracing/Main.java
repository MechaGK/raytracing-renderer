package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Vector2D viewportSize = new Vector2D(10, 10);

        int imageResolutionX = 500;
        int imageResolutionY = 500;

        BufferedImage image = new BufferedImage(imageResolutionX, imageResolutionY,
                BufferedImage.TYPE_INT_ARGB);

        Shape shape = new Sphere(new Material(Color.red), new Vector3D(0, 0, 5), 4);

        Vector3D raysDirection = new Vector3D(0, 0, 1);

        for (int y = 0; y < imageResolutionY; y++) {
            for (int x = 0; x < imageResolutionX; x++) {
                Vector3D rayOrigin = new Vector3D(
                        -(viewportSize.getX() / 2) + ((viewportSize.getX() / imageResolutionX) * x),
                        -(viewportSize.getY() / 2) + ((viewportSize.getY() / imageResolutionY) * y), 0);

                Ray ray = new Ray(rayOrigin, raysDirection);

                if (shape.intersect(ray) != null) {
                    image.setRGB(x, y, Color.red.getRGB());
                }
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
