package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.DistantLight;
import saurkraut.lights.Light;
import saurkraut.materials.ColoredMaterial;
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
        Scene scene = new Scene();

        Shape sphere1 = new Sphere(new ColoredMaterial(Color.cyan, 0.18f), new Vector3D(0.8, 2, 1.2), 2);
        Shape sphere2 = new Sphere(new ColoredMaterial(Color.cyan, 0.18f), new Vector3D(1, 2, -1.2), 2);

        int i;
        for (i = 0; i < 7; ++i)
            scene.add(new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(1 + i, 1 + i * i * 0.04, 0), 1));
        scene.add(new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(1 + i, 1 + i * i * 0.04, 0), 1.1f));

        Light light = new DistantLight(new Vector3D(3, 2, 1), 10, Color.pink);
        Light light2 = new DistantLight(new Vector3D(-3, 2, -1), 10, Color.orange);

        scene.add(sphere1);
        scene.add(sphere2);
        scene.add(light);
        scene.add(light2);
        
                // Setting up camera
        Vector3D cameraOrigin = new Vector3D(0, 0, -7);
        
        // Is only used for initialization. Real direction is set by lookAt just after creation
        Vector3D cameraDirection = new Vector3D(0, -1, 5);
        PerspectiveCamera camera = new PerspectiveCamera(cameraOrigin, cameraDirection, 125, 0.1);
        camera.lookAt(new Vector3D(0, 0, 0));
        scene.setCamera(camera);

        return scene;
    }

    public static Scene createSimpleScene() {
        Scene scene = new Scene();

        //Shape sphere1 = new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(5, 1, 8), new Vector3D(1* Math.PI*2/4, 1* Math.PI*2/4, 1* Math.PI*2/16));
        //System.out.println(sphere1.directionToLocal(new Vector3D(0, 1, 0)));
        
        scene.addShapes(
                new Plane(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(0, 1, 0)),
                new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(-4, 2d, 5), 2),
                new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(-7d, 2d, 0), 2),
                new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(8d, 2d, -2), 2),
                new Cuboid(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 4, 0),
                        new Vector3D(2, 2, 2), new Vector3D(1* Math.PI*2/8, 1* Math.PI*2/8, 1* Math.PI*2/8).scalarMultiply(1))
        );

        scene.addLights(
                new DistantLight(new Vector3D(0.2, 0.1, 1), 15, new Color(0xff_ff8000)),
                new DistantLight(new Vector3D(-0.1, -0.2, 1), 15, new Color(0xff_0080ff))
        );

                // Setting up camera
        Vector3D cameraOrigin = new Vector3D(0, 0, -7);
        
        // Is only used for initialization. Real direction is set by lookAt just after creation
        Vector3D cameraDirection = new Vector3D(0, -1, 5);
        PerspectiveCamera camera = new PerspectiveCamera(cameraOrigin, cameraDirection, 125, 0.1);
        camera.lookAt(new Vector3D(0, 0, 0));
        scene.setCamera(camera);
        
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
    
        // Setting up camera
        Vector3D cameraOrigin = new Vector3D(0,0,0);
        Vector3D cameraAngles = new Vector3D(Math.toRadians(0),Math.toRadians(0),Math.toRadians(0));
        double scale = 3.5;
        Camera camera = new OrthogonalCamera(cameraOrigin, cameraAngles, scale);
        scene.setCamera(camera);
        
        return scene;
    }
    

    public static void main(String[] args) {
        // Creating a scene
        Scene scene = createCuboidTestScene();

        // Rendering scene to image and saving to disk
        final int resolutionX = 600;
        final int resolutionY = 600;
 
        BufferedImage image = scene.renderScene(resolutionX, resolutionY);
        saveImage(image, "test.png");
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
