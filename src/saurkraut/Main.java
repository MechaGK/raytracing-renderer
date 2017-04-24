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
    public static Scene createInappropriateScene() {
        Scene inappropriateScene = new Scene();

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

        return inappropriateScene;
    }

    public static Scene createSimpleScene() {
        Scene scene = new Scene();

        //Shape sphere1 = new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(5, 1, 8), new Vector3D(1* Math.PI*2/4, 1* Math.PI*2/4, 1* Math.PI*2/16));
        //System.out.println(sphere1.directionToLocal(new Vector3D(0, 1, 0)));
        
        scene.addShapes(
                new Sphere(new ColoredMaterial(
                        Color.white, 0.18f),
                        new Vector3D(0, 0, 0),
                        2)
        );

        scene.addLights(
                new DistantLight(new Vector3D(0.2, 0.1, 1), 15, new Color(0xff_ff8000)),
                new DistantLight(new Vector3D(-0.1, -0.2, 1), 15, new Color(0xff_0080ff))
        );

        return scene;
    }
    
    public static Scene createCuboidTestScene() {
        Scene scene = new Scene();

        //Shape sphere1 = new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(5, 1, 8), new Vector3D(1* Math.PI*2/4, 1* Math.PI*2/4, 1* Math.PI*2/16));
        //System.out.println(sphere1.directionToLocal(new Vector3D(0, 1, 0)));
        Vector3D[] corners = new Vector3D[] {
            new Vector3D(-1, -1, -1),
            new Vector3D(-1, -1, 1),
            new Vector3D(-1, 1, -1),
            new Vector3D(-1, 1, 1),
            new Vector3D(1, -1, -1),
            new Vector3D(1, -1, 1),
            new Vector3D(1, 1, -1),
            new Vector3D(1, 1, 1),
            new Vector3D(-1, 0, -1),
            new Vector3D(-1, 0, 1),
            new Vector3D(1, 0, -1),
            new Vector3D(1, 0, 1),
            new Vector3D(0, -1, -1),
            new Vector3D(0, -1, 1),
            new Vector3D(0, 1, -1),
            new Vector3D(0, 1, 1),
            new Vector3D(-1, -1, 0),
            new Vector3D(-1, 1, 0),
            new Vector3D(1, -1, 0),
            new Vector3D(1, 1, 0)
            
            
        };
        
        Color[] colors = new Color[] {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.MAGENTA,
            Color.YELLOW,
            Color.CYAN,
            Color.ORANGE
        };
        
        Shape s;
        scene.addShapes(
                (s = new Sphere(new ColoredMaterial(
                        Color.white, 0.18f),
                        new Vector3D(0, 0, 0), // Position
                        new Vector3D(0.8, 1, 1), // Scale
                        new Vector3D(
                                Math.toRadians(15),
                                Math.toRadians(15),
                                Math.toRadians(15)))) // rotation
        );
        
        for (int i = 0; i < corners.length; ++i) {
            scene.add(new Sphere(new ColoredMaterial(
                        colors[i%colors.length], 0.18f),
                        s.pointToWorld(corners[i]), // Position
                        0.05));
        }

        scene.addLights(
                new DistantLight(new Vector3D(0.2, 0.1, 1), 15, new Color(0xff_ff8000)),
                new DistantLight(new Vector3D(-0.1, -0.2, 1), 15, new Color(0xff_0080ff))
        );

        return scene;
    }
    

    public static void main(String[] args) {
        // Creating a scene
        Scene scene = createCuboidTestScene();

        // Setting up camera
        Vector3D cameraOrigin = new Vector3D(0, 0, -10);
        Vector3D cameraRotation = new Vector3D(Math.toRadians(0), Math.toRadians(0), 0);

        // Rendering scene to image and saving to disk
        final int resolutionX = 400;
        final int resolutionY = 400;

        final double aspectRatio = (double) resolutionX / (double) resolutionY;
        final double scale = 5;

        Camera camera = new OrthogonalCamera(cameraOrigin, cameraRotation, scale * aspectRatio, scale);

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

            RayHit rayHit = scene.castRay(ray);

            if (rayHit == null) {
                // No shape hit by ray. Continue to next ray.
                image.setRGB(cameraRay.x, cameraRay.y, Color.MAGENTA.getRGB());
                continue;
            }

            Shape shape = rayHit.shape;
            Vector3D hit = rayHit.point;

            testEnd = System.nanoTime();
            testingTime += testEnd - testStart;


            shadedPoints++;

            shadingStart = System.nanoTime();
            Color shapeColor = shape.getColor(hit);

            Color lightColor = PhongShader.shade(scene, shape, hit, ray.direction);

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
