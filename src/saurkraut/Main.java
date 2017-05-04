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

        Shape sphere1 = new Sphere(new ColoredMaterial(Color.PINK, 0.18f), new Vector3D(0.8, 0, 1.2), 2);
        Shape sphere2 = new Sphere(new ColoredMaterial(Color.PINK, 0.18f), new Vector3D(1, 0, -1.2), 2);

        int i;
        for (i = 0; i < 8; ++i)
            scene.add(new Sphere(new ColoredMaterial(Color.PINK, 0.18f), new Vector3D(1 + i, 1 + i * i * -0.02, 0), 1));
        scene.add(new Sphere(new ColoredMaterial(Color.PINK, 0.18f), new Vector3D(1 + i, 1 + i * i * -0.02, 0), 1.1f));

        Light light = new DistantLight(new Vector3D(3, -2, 1), 15, Color.yellow);
        Light light2 = new DistantLight(new Vector3D(-3, 2, 1), 15, Color.cyan);

        scene.add(sphere1);
        scene.add(sphere2);
        scene.add(light);
        scene.add(light2);
        
                // Setting up camera
        Vector3D cameraOrigin = new Vector3D(15, 2, -20);
        
        // Is only used for initialization. Real direction is set by lookAt just after creation
        Vector3D cameraDirection = new Vector3D(0, -1, 5);
        PerspectiveCamera camera = new PerspectiveCamera(cameraOrigin, cameraDirection, 120, 0.1);
        camera.lookAt(new Vector3D(4, 0, 0));
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
        Vector3D cameraOrigin = new Vector3D(-5, 4, -10);
        
        // Is only used for initialization. Real direction is set by lookAt just after creation
        Vector3D cameraDirection = new Vector3D(5, -7, 5);
        PerspectiveCamera camera = new PerspectiveCamera(cameraOrigin, cameraDirection, 90, 0.1);
        camera.lookAt(new Vector3D(2, 2, 2));
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
            new Vector3D(1, 1, 1),/*/
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
            new Vector3D(1, 1, 0)//*/
            
            
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
                s = new Cuboid(new ColoredMaterial(
                        Color.white, 0.18f),
                        new Vector3D(0, 0, 0), // Position
                        new Vector3D(1, 1, 1), // Scale
                        new Vector3D(
                                Math.toRadians(0),
                                Math.toRadians(40),
                                Math.toRadians(0))) // rotation
                ,new Cuboid(new ColoredMaterial(
                        Color.red, 0.18f),
                        s.pointToWorld(Vector3D.PLUS_I.scalarMultiply(2)), // Position
                        new Vector3D(1, 1, 1), // Scale
                        s.eulerAngles)
        );
        System.out.println(s.pointToWorld(new Vector3D(100, 200, 300)));
        System.out.println(s.pointToLocal(s.pointToWorld(new Vector3D(100, 200, 300))));
        
        for (int i = 0; i < corners.length; ++i) {
            scene.add(new Cuboid(new ColoredMaterial(
                        colors[i%colors.length], 0.18f),
                        s.pointToWorld(corners[i]), // Position
                        new Vector3D(0.2, 0.2, 0.2),
                        s.eulerAngles));
        }

        scene.addLights(
                new DistantLight(new Vector3D(0.707, 0.707, 0.707), 20, new Color(0xff_ffff80)),
                new DistantLight(new Vector3D(-0.707, -0.707, -0.707), 50, new Color(0xff_80ffff))
        );
    
        Camera orthogonalCamera = new OrthogonalCamera(
                new Vector3D(0,0,-10),
                new Vector3D(Math.toRadians(0),Math.toRadians(0),Math.toRadians(0)),
                10);
        
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(
                new Vector3D(0, 0, -10),
                Vector3D.PLUS_I,
                60,
                0.01);
        perspectiveCamera.lookAt(new Vector3D(0, 0, 0));
        
        scene.setCamera(true ? perspectiveCamera : orthogonalCamera);
        
        return scene;
    }
    

    public static void main(String[] args) {
        // Creating a scene
        Scene scene = createSimpleScene();

        // Rendering scene to image and saving to disk
        final int resolutionX = 400;
        final int resolutionY = 400;
 
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
