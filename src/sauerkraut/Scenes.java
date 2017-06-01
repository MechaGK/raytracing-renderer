package sauerkraut;

import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.lights.DistantLight;
import sauerkraut.lights.Light;
import sauerkraut.lights.PointLight;
import sauerkraut.lights.SpotLight;
import sauerkraut.materials.ColoredMaterial;
import sauerkraut.materials.Material;
import sauerkraut.materials.TilingStretchedTexturedMaterial;
import sauerkraut.materials.TilingTexturedMaterial;
import sauerkraut.shapes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class Scenes {
    public static Scene mainImage() {
        Scene scene = new Scene();

        Vector3D[] walls = new Vector3D[]{
                new Vector3D(0, 2.5, 5),
                new Vector3D(5, 2.5, 0),
                new Vector3D(0, 2.5, -5),
        };

        BufferedImage tapet = null;

        try {
            tapet = ImageIO.read(new File("tapet.jpg"));
        } catch (IOException e) {
            System.out.println(":(");
            System.exit(1234);
        }

        //Material wallMaterial = new TilingStretchedTexturedMaterial(0.35f, tapet, 2, 4);
        Material wallMaterial = new ColoredMaterial(Color.white, 0.35f, 10000, 0f);

        for (Vector3D wallPosition : walls) {
            scene.add(new Cuboid(wallMaterial, wallPosition, new Vector3D(0.25 + wallPosition.getZ(), 2.5, 0.25 + wallPosition.getX()), Vector3D.ZERO));
        }

        scene.addShapes(new Cuboid(wallMaterial, new Vector3D(-5, 2.5, -2), new Vector3D(0.25, 2.5, 3), Vector3D.ZERO));
        scene.addShapes(new Cuboid(wallMaterial, new Vector3D(-5, 2.5, 4.5), new Vector3D(0.25, 2.5, 1), Vector3D.ZERO));

        scene.addShapes(new Cuboid(wallMaterial, new Vector3D(-5, 1, 2.25), new Vector3D(0.25, 1, 1.25), Vector3D.ZERO));
        scene.addShapes(new Cuboid(wallMaterial, new Vector3D(-5, 4.75, 2.25), new Vector3D(0.25, 0.5, 1.25), Vector3D.ZERO));

        scene.addShapes(new Cuboid(wallMaterial, new Vector3D(-5, 3.125, 2.25), new Vector3D(0.25, 0.1, 1.25), Vector3D.ZERO));
        scene.addShapes(new Cuboid(wallMaterial, new Vector3D(-5, 3.125, 2.25), new Vector3D(0.25, 1.125, 0.1), Vector3D.ZERO));

        BufferedImage floor = null;

        try {
            floor = ImageIO.read(new File("floor.jpg"));
        } catch (IOException e) {
            System.out.println(":(");
            System.exit(1234);
        }

        //Material floorMaterial = new ColoredMaterial(new Color(0xFFB74D), 0.35f);
        Material floorMaterial = new TilingStretchedTexturedMaterial(0.35f, floor, 2, 2);
        Material roofMaterial = new ColoredMaterial(new Color(0xFFB74D), 0.1f);
        scene.addShapes(
                new Cuboid(floorMaterial, new Vector3D(0, 0, 0), new Vector3D(5.25, 0.1, 5.25), Vector3D.ZERO),
                new Cuboid(roofMaterial, new Vector3D(0, 5, 0), new Vector3D(5.25, 0.1, 5.25), Vector3D.ZERO)
        );

        scene.setCamera(new PerspectiveCamera(new Vector3D(0, 2.8, -4.8), new Vector3D(0, -3.5, 10), 90, 0.1));

        scene.add(new DistantLight(new Vector3D(10, -3, 1), 200, Color.white));
        scene.add(new PointLight(new Vector3D(0, 4.5, 0), 90, Color.white));

        scene.add(new Plane(new ColoredMaterial(new Color(0x2196F3), 1, 1, 0),
                new Vector3D(0, 0, 12), new Vector3D(0, 0, -1)));

        scene.add(new DistantLight(new Vector3D(0, 0, 1), 500, Color.white));

        scene.addShapes(
                new Sphere(new ColoredMaterial(Color.getHSBColor(1f / 4f, 1, 1), 0.18f), new Vector3D(-3, 0.7, -1), 0.7),
                new Sphere(new ColoredMaterial(Color.getHSBColor(2f / 4f, 1, 1), 0.18f), new Vector3D(-1, 0.7, -2), 0.7),
                new Sphere(new ColoredMaterial(Color.getHSBColor(3f / 4f, 1, 1), 0.18f), new Vector3D(1, 0.7, -2), 0.7),
                new Sphere(new ColoredMaterial(Color.getHSBColor(4f / 4f, 1, 1), 0.18f), new Vector3D(3, 0.7, -1), 0.7)
        );

        Cuboid tabletop;
        Cuboid chair;

        BufferedImage polstring = null;

        try {
            polstring = ImageIO.read(new File("polstring.png"));
        } catch (IOException e) {
            System.out.println(":(");
            System.exit(1234);
        }

        Material polstringMaterial = new TilingStretchedTexturedMaterial(0.2f, polstring, 10, 10);

        scene.addShapes(
                tabletop = new Cuboid(new ColoredMaterial(new Color(0x795548), 0.35f), new Vector3D(-1, 1.9, 2), new Vector3D(1, 0.1, 1), new Vector3D(0, 5, 0)),
                new Cuboid(new ColoredMaterial(new Color(0x795548), 0.35f), tabletop.pointToWorld(new Vector3D(0.8, -10, 0.8)), new Vector3D(0.1, 1, 0.1), new Vector3D(0, 5, 0)),
                new Cuboid(new ColoredMaterial(new Color(0x795548), 0.35f), tabletop.pointToWorld(new Vector3D(-0.8, -10, 0.8)), new Vector3D(0.1, 1, 0.1), new Vector3D(0, 5, 0)),
                new Cuboid(new ColoredMaterial(new Color(0x795548), 0.35f), tabletop.pointToWorld(new Vector3D(0.8, -10, -0.8)), new Vector3D(0.1, 1, 0.1), new Vector3D(0, 5, 0)),
                new Cuboid(new ColoredMaterial(new Color(0x795548), 0.35f), tabletop.pointToWorld(new Vector3D(-0.8, -10, -0.8)), new Vector3D(0.1, 1, 0.1), new Vector3D(0, 5, 0)),
                chair = new Cuboid(new ColoredMaterial(new Color(0xA1887F), 0.35f), new Vector3D(1, 0.6, 2), new Vector3D(0.4, 0.6, 0.4), new Vector3D(0, -5, 0)),
                new Sphere(polstringMaterial, chair.pointToWorld(new Vector3D(0, 1, 0)), new Vector3D(0.5, 0.2, 0.5), Vector3D.ZERO)
        );


        return scene;
    }

    public static Scene illustration() {
        Scene scene = new Scene();

        scene.addShapes(
                new Cuboid(new ColoredMaterial(new Color(0x0000FF), 0.18f), Vector3D.ZERO, cubeSize, cubeRotation),
                new Cuboid(new ColoredMaterial(new Color(0xFF0000), 0.18f), new Vector3D(0, 0, 5), cubeSize, cubeRotation)
        );

        scene.addLights(
                new PointLight(new Vector3D(0, 5, 5), 240, Color.white)
        );

        scene.setCamera(new PerspectiveCamera(new Vector3D(0, 5, -5), new Vector3D(0, 0, 1), 55, 1));
        scene.getCamera().lookAt(Vector3D.ZERO);

        return scene;
    }


    static Vector3D cubeSize = new Vector3D(1, 1, 1);
    static Vector3D cubeRotation = new Vector3D(0, 0, 0);

    static Color[] colors = null;


    public static Scene scene1() {
        Scene scene = baseScene();

        scene.add(new Cuboid(new ColoredMaterial(colors[0], 0.18f, 1000), new Vector3D(0, 1, 0), cubeSize, cubeRotation));
        return scene;
    }

    public static Scene scene2() {
        Scene scene = baseScene();

        scene.add(new Cuboid(new ColoredMaterial(colors[0], 0.18f, 1000), new Vector3D(0, 3, 0), cubeSize, cubeRotation));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                scene.add(new Cuboid(new ColoredMaterial(colors[1 + i + (j * 2)], 0.18f, 1000), new Vector3D(-1 + (i * 2), 1, -1 + (j * 2)), cubeSize, cubeRotation));
            }
        }

        return scene;
    }

    public static Scene scene3() {
        Scene scene = baseScene();

        scene.add(new Cuboid(new ColoredMaterial(colors[0], 0.18f, 1000), new Vector3D(0, 5, 0), cubeSize, cubeRotation));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                scene.add(new Cuboid(new ColoredMaterial(colors[1 + i + (j * 2)], 0.18f, 1000), new Vector3D(-1 + (i * 2), 3, -1 + (j * 2)), cubeSize, cubeRotation));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                scene.add(new Cuboid(new ColoredMaterial(colors[5 + i + (j * 3)], 0.18f, 1000), new Vector3D(-2 + (i * 2), 1, -2 + (j * 2)), cubeSize, cubeRotation));
            }
        }

        return scene;
    }


    public static Scene baseScene() {
        Scene scene = new Scene();

        if (colors == null) {
            colors = new Color[14];

            for (int i = 0; i < 14; i++) {
                colors[i] = Color.getHSBColor((float) i / (float) 13, 1, 1);
            }
        }


        Vector3D lightDirection = new Vector3D(-2, -5, 10);

        scene.addShapes(
                new Plane(new ColoredMaterial(Color.white, 0.18f, 1000), Vector3D.ZERO, new Vector3D(0, 1, 0))
        );

        scene.addLights(
                new PointLight(new Vector3D(-3, 4.5, -3), 100, new Color(0xF44336)),
                new PointLight(new Vector3D(3, 4.5, -3), 100, new Color(0x4CAF50)),
                new PointLight(new Vector3D(3, 4.5, 3), 100, new Color(0x2196F3)),
                new PointLight(new Vector3D(-3, 4.5, 3), 100, new Color(0xFFC107))
        );

        scene.add(new DistantLight(lightDirection, 8, Color.white));

        scene.setCamera(new PerspectiveCamera(new Vector3D(6, 8, -6), new Vector3D(1, -1, 1), 90, 1));
        scene.getCamera().lookAt(new Vector3D(0, 1, 0));

        return scene;
    }

    public static Scene sphereShadow() {
        Scene scene = new Scene();

        scene.addShapes(
                //new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), 1),
                new Cuboid(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(1, 1, 1), Vector3D.ZERO),
                new Plane(new ColoredMaterial(Color.GRAY, 0.18f), new Vector3D(0, 0, 3), new Vector3D(0, 0, -1)));
        scene.add(new DistantLight(new Vector3D(0, -1, 2), 10, Color.white));

        scene.setCamera(new PerspectiveCamera(new Vector3D(0, 0, -5), new Vector3D(0, 0, 2), 55, 1));

        return scene;
    }

    public static Scene sphereShadowThird() {
        Scene scene = sphereShadow();

        scene.addShapes(
                new Sphere(new ColoredMaterial(Color.RED, 0.18f), scene.getCamera().position, 0.1));

        scene.setCamera(new PerspectiveCamera(new Vector3D(-6, 0, -1), new Vector3D(10, -1, 0), 90, 1));

        return scene;
    }

    public static Scene colors() {
        Scene scene = new Scene();

        int length = 18;
        Vector3D[] points = new Vector3D[length];

        for (int i = 0; i < length; i++) {
            double v = ((float) i / (float) length) * Math.PI * 2;

            points[i] = new Vector3D(Math.sin(v) * 5, Math.cos(v) * 5, 0);
        }

        for (int i = 0; i < length; i++) {
            float h = ((float) i / (float) length);

            int i2 = (i + (length / 3)) % length;
            int i3 = (i + (2 * length / 3)) % length;

            Color color = Color.getHSBColor(h, 1, 1);

            scene.add(new Triangle(new ColoredMaterial(color, 0.18f),
                    new Vector3D(points[i].getX(), points[i].getY(), -1),
                    points[i2], points[i3]));
        }

        scene.addLights(
                new DistantLight(new Vector3D(0, 0, 10), 20, Color.white)
        );

        scene.setCamera(new PerspectiveCamera(new Vector3D(0, 0, -10), new Vector3D(0, 0, 1), 90, 1));

        return scene;
    }

    public static Scene problem() {
        Scene scene = new Scene();

        scene.addShapes(
                new Plane(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(0, 0, -1)),
                new Plane(new ColoredMaterial(new Color(100, 100, 100), 0.18f), new Vector3D(0, 0, -0.5), new Vector3D(0, 0, -1)),
                new Triangle(new ColoredMaterial(Color.blue, 0.18f), new Vector3D(1, 1, -1), new Vector3D(0, -1, -1), new Vector3D(-1, 1, -1))
        );

        scene.addLights(
                new DistantLight(new Vector3D(0, 0, 10), 20, Color.white)
        );

        scene.setCamera(new PerspectiveCamera(new Vector3D(0, 0, -10), new Vector3D(0, 0, 1), 90, 1));

        return scene;
    }

    public enum BenchmarkShape {
        Sphere, Cuboid
    }

    public enum BenchmarkLight {
        DirectionalLight, Spotlight, PointLight
    }

    public static Scene benchmark(int numberOfShapes, int numberOfLights, BenchmarkShape shapeType, BenchmarkLight lightType, boolean defaultLight) {
        Scene scene = new Scene();

        Random random = new Random();

        for (int i = 0; i < numberOfShapes; i++) {
            Color color = new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256));

            Vector3D position = new Vector3D(
                    (random.nextFloat() - 0.5f) * 10,
                    (random.nextFloat() - 0.5f) * 10,
                    (random.nextFloat() - 0.5f) * 10);

            switch (shapeType) {
                case Sphere:
                    scene.add(new Sphere(new ColoredMaterial(color, 0.18f), position, 0.5));
                    break;
                case Cuboid:
                    scene.add(new Cuboid(
                            new ColoredMaterial(color, 0.18f),
                            position,
                            new Vector3D(0.2, 0.2, 0.2),
                            position));
                    break;
            }
        }

        for (int i = 0; i < numberOfLights; i++) {
            Vector3D position = new Vector3D(
                    (random.nextFloat() - 0.5f) * 11,
                    (random.nextFloat() - 0.5f) * 11,
                    (random.nextFloat() - 0.5f) * 11);

            Color color = new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256));

            switch (lightType) {
                case DirectionalLight:
                    scene.add(new DistantLight(position, (random.nextFloat() + 0.2f) * 20, color));
                    break;
                case Spotlight:
                    scene.add(new SpotLight(position, position, (random.nextFloat() * 15) + 5, (random.nextFloat() * 60) + 30, color, (random.nextFloat() + 0.2f) * 20));
                    break;
                case PointLight:
                    scene.add(new PointLight(position, (random.nextFloat() + 0.2f) * 20, color));
                    break;
            }
        }

        scene.setCamera(
                new PerspectiveCamera(new Vector3D(0, 0, -10), new Vector3D(0, 0, 1), 90, 1)
        );

        if (defaultLight) {
            scene.addLights(
                    new DistantLight(new Vector3D(0, -1, 3), 10, Color.white)
            );
        }

        return scene;
    }

    public static Scene sphere() {
        Scene scene = new Scene();

        scene.addShapes(
                new Sphere(new ColoredMaterial(Color.blue, 0.18f), Vector3D.ZERO, 3)
        );

        scene.addLights(
                new DistantLight(new Vector3D(-1, 0, 2), 10, Color.white)
        );

        scene.setCamera(
                new PerspectiveCamera(new Vector3D(0, 0, -6), new Vector3D(0, 0, 1), 75, 0.1)
        );

        return scene;
    }

    public static Scene pyramid() {
        Scene scene = new Scene();

        scene.addShapes(
                new Triangle(new ColoredMaterial(Color.orange, 0.2f, 2, 0.2f), new Vector3D(0, 5, 0), new Vector3D(0, 0, -4), new Vector3D(-4, 0, 0)),
                new Triangle(new ColoredMaterial(Color.orange, 0.2f, 2, 0.2f), new Vector3D(0, 5, 0), new Vector3D(4, 0, 0), new Vector3D(0, 0, -4)),
                new Triangle(new ColoredMaterial(Color.orange, 0.2f, 2, 0.2f), new Vector3D(0, 5, 0), new Vector3D(0, 0, 4), new Vector3D(4, 0, 0)),
                new Triangle(new ColoredMaterial(Color.orange, 0.2f, 2, 0.2f), new Vector3D(0, 5, 0), new Vector3D(4, 0, 0), new Vector3D(0, 0, 4)),
                new Plane(new ColoredMaterial(Color.orange, 0.3f), new Vector3D(0, 0, 0), new Vector3D(0, 1, 0))
        );

        scene.addLights(
                new DistantLight(new Vector3D(1, -1, 2), 20, new Color(255, 240, 240))
        );

        scene.setCamera(
                new PerspectiveCamera(new Vector3D(3, 2, -10), new Vector3D(0, 0, 1), 75, 1)
        );

        scene.getCamera().lookAt(new Vector3D(0, 1, 0));

        return scene;
    }

    public static Scene teapot() {
        Scene scene = new Scene();

        double cameraAngle = Math.PI * 0.85;

        scene.setCamera(
                new PerspectiveCamera(new Vector3D(Math.sin(cameraAngle) * 7, 0, Math.cos(cameraAngle) * 7), new Vector3D(0, 0, -1), 60, 1)
        );

        scene.getCamera().lookAt(Vector3D.ZERO);

        scene.addLights(
                new DistantLight(new Vector3D(0, 0, 1), 30, Color.white)
        );

        try {
            OBJLoader objLoader = new OBJLoader(new File("teapot.obj"));

            scene.addShapes(
                    new Mesh(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, -1.5, 0), objLoader)
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scene;
    }

    public static Scene inappropriateScene() {
        Scene scene = new Scene();

        sauerkraut.shapes.Shape sphere1 = new Sphere(new ColoredMaterial(Color.PINK, 0.18f), new Vector3D(0.8, 0, 1.2), 2);
        sauerkraut.shapes.Shape sphere2 = new Sphere(new ColoredMaterial(Color.PINK, 0.18f), new Vector3D(1, 0, -1.2), 2);

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

    public static Scene simpleScene() {
        Scene scene = new Scene();

        //Shape sphere1 = new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(5, 1, 8), new Vector3D(1* Math.PI*2/4, 1* Math.PI*2/4, 1* Math.PI*2/16));
        //System.out.println(sphere1.directionToLocal(new Vector3D(0, 1, 0)));

        sauerkraut.shapes.Shape proj;
        sauerkraut.shapes.Shape proj2;
        Material mat = new ColoredMaterial(Color.white, 0.18f);
        Material redMat = new ColoredMaterial(Color.red, 0.18f);
        scene.addShapes(
                new Plane(mat, new Vector3D(0, 0, 0), new Vector3D(0, 1, 0)),
                new Plane(mat, new Vector3D(0, 10, 0), new Vector3D(0, -1, 0)),
                new Plane(mat, new Vector3D(0, 0, 10), new Vector3D(0, 0, -1)),
                new Plane(mat, new Vector3D(10, 0, 0), new Vector3D(-1, 0, 0)),
                new Sphere(mat, new Vector3D(-4, 2d, 5), 2),
                new Sphere(new ColoredMaterial(new Color(0xff_ff00ff), 0.1f, 100, 1f), new Vector3D(-7d, 2d, 0), 2),
                new Sphere(mat, new Vector3D(8d, 2d, -2), 2),
                proj = new Cuboid(redMat, new Vector3D(2, 4, -5),
                        new Vector3D(0.4, 1, 0.4),
                        new Vector3D(
                                Math.toRadians(110),
                                Math.toRadians(-38),
                                Math.toRadians(0))),
                proj2 = new Cuboid(redMat, new Vector3D(-5, 0.4, -5),
                        new Vector3D(0.4, 1, 0.4),
                        new Vector3D(
                                Math.toRadians(90),
                                Math.toRadians(45),
                                Math.toRadians(0)))
                //new Sphere(mat, cub.pointToWorld(new Vector3D(1.2, 0, 0)), 0.4)
        );

        scene.addLights(
                new PointLight(new Vector3D(-2.2, 8, -2.2), 1000, new Color(0xff_ff8000)),
                new SpotLight(proj.pointToWorld(new Vector3D(0, 1.001, 0)), proj.directionToWorld(Vector3D.MINUS_J), 50, 55, new Color(0xff_0080ff), 100),
                new SpotLight(proj2.pointToWorld(new Vector3D(0, 1.001, 0)), proj2.directionToWorld(Vector3D.MINUS_J), 60, 80, new Color(0xff_ffffff), 50)
        );

        // Setting up camera
        Vector3D cameraOrigin = new Vector3D(-5, 5, -12);

        // Is only used for initialization. Real direction is set by lookAt just after creation
        Vector3D cameraDirection = new Vector3D(5, -7, 5);
        PerspectiveCamera camera = new PerspectiveCamera(cameraOrigin, cameraDirection, 90, 0.1);
        camera.lookAt(new Vector3D(3, 0, 10));
        scene.setCamera(camera);

        return scene;
    }

    public static Scene cuboidTestScene() {
        Scene scene = new Scene();

        //Shape sphere1 = new Sphere(new ColoredMaterial(Color.white, 0.18f), new Vector3D(0, 0, 0), new Vector3D(5, 1, 8), new Vector3D(1* Math.PI*2/4, 1* Math.PI*2/4, 1* Math.PI*2/16));
        //System.out.println(sphere1.directionToLocal(new Vector3D(0, 1, 0)));
        Vector3D[] corners = new Vector3D[]{
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

        Color[] colors = new Color[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.MAGENTA,
                Color.YELLOW,
                Color.CYAN,
                Color.ORANGE
        };

        sauerkraut.shapes.Shape s;
        scene.addShapes(
                s = new Cuboid(new ColoredMaterial(
                        Color.white, 0.18f),
                        new Vector3D(0, 0, 0), // Position
                        new Vector3D(1, 1, 1), // Scale
                        new Vector3D(
                                Math.toRadians(0),
                                Math.toRadians(0),
                                Math.toRadians(0))) // rotation
                /*,new Cuboid(new ColoredMaterial(

                        Color.red, 0.18f),
                        s.pointToWorld(Vector3D.PLUS_I.scalarMultiply(1)), // Position
                        new Vector3D(0.8, 0.8, 0.8), // Scale
                        s.eulerAngles)*/
        );

        scene.addLights(
                new PointLight(s.pointToWorld(new Vector3D(-5.1, -4.1, -3.1)), 500, Color.white)
                //new DistantLight(new Vector3D(0.707, 0.707, 0.707), 20, new Color(0xff_ffff80)),
                //new DistantLight(new Vector3D(-0.707, -0.707, -0.707), 50, new Color(0xff_80ffff))
        );

        /*
        for (int i = 0; i < corners.length; ++i) {
            scene.add(new Sphere(new ColoredMaterial(
                        colors[i%colors.length], 1f),
                        s.pointToWorld(corners[i]), // Position
                        new Vector3D(0.05, 0.05, 0.05),
                        s.eulerAngles));
        }//*/

        Camera orthogonalCamera = new OrthogonalCamera(
                new Vector3D(0, 0, -10),
                new Vector3D(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)),
                10);

        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(
                new Vector3D(-5, -5, -5),
                Vector3D.PLUS_I,
                25,
                0.01);
        perspectiveCamera.lookAt(new Vector3D(0, 0, 0));

        scene.setCamera(true ? perspectiveCamera : orthogonalCamera);

        return scene;
    }
}
