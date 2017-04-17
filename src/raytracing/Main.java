package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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

                Vector3D hitNormal = closestShape.getNormal(closestHit);

                Color lightColor = getIllumination(scene, closestShape.material, closestHit, hitNormal);

                Color finalColor = colorMultiply(shapeColor, lightColor);

                image.setRGB(cameraRay.x, cameraRay.y, finalColor.getRGB());
            }
        }

        return image;
    }

    /**
     * Finds the illumination at a specific point in the scene with a given normal.
     * Only supports distant lights at the moment and no shadows.
     * Calculations based on diffuse rendering from
     * https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/diffuse-lambertian-shading
     *
     * @param scene    The scene
     * @param material The material of the object which the point is on
     * @param point    The point at which to find the illumination
     * @param normal   The normal to base the illumination of
     * @return The color of the total illumination
     */
    public static Color getIllumination(Scene scene, Material material, Vector3D point, Vector3D normal) {
        normal = normal.normalize();

        Color finalColor = new Color(0f, 0f, 0f);

        DistantLight distantLight;
        Color color;

        for (Light light : scene.getLights()) {
            if (light instanceof DistantLight) {
                distantLight = (DistantLight) light;

                Vector3D lightIncident = distantLight.direction.scalarMultiply(-1);

                float number = (float) (material.albedo / Math.PI * light.intensity
                        * Math.max(0f, normal.dotProduct(lightIncident)));

                color = colorMultiply(light.color, number);
                finalColor = colorAdd(color, finalColor);
            }
        }

        return finalColor;
    }

    /**
     * A method for adding two colors
     *
     * @param color1
     * @param color2
     * @return The addition of color1 and color2
     */
    private static Color colorAdd(Color color1, Color color2) {
        return new Color(
                color1.getRed() + color2.getRed(),
                color1.getGreen() + color2.getGreen(),
                color1.getBlue() + color2.getBlue());
    }


    /**
     * A method to multiply a color with a float. Because Java doesn't support extension methods
     *
     * @param color A given color
     * @param value A float
     * @return A new color where every component is multiplied by f
     */
    public static Color colorMultiply(Color color, float value) {
        float[] rgb = color.getRGBColorComponents(null);
        float red = Math.min(rgb[0] * value, 1f);
        float green = Math.min(rgb[1] * value, 1f);
        float blue = Math.min(rgb[2] * value, 1f);

        return new Color(red, green, blue);
    }

    /**
     * A method to multiply a color with a float. Because Java doesn't support extension methods
     *
     * @param color1 A given color
     * @param color2 A float
     * @return A new color where every component is multiplied by f
     */
    public static Color colorMultiply(Color color1, Color color2) {
        float[] rgb1 = color1.getRGBColorComponents(null);
        float[] rgb2 = color2.getRGBColorComponents(null);

        float red = Math.min(rgb1[0] * rgb2[0], 1f);
        float green = Math.min(rgb1[1] * rgb2[1], 1f);
        float blue = Math.min(rgb1[2] * rgb2[2], 1f);

        return new Color(red, green, blue);
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
