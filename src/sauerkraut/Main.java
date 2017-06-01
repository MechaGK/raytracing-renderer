package sauerkraut;

import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {
    public static HashMap<String, SceneCreator> scenes = new HashMap<String, SceneCreator>() {
        {
            put("sphere", Scenes::sphere);
            put("cuboid", Scenes::cuboidTestScene);
            put("pyramid", Scenes::pyramid);
            put("inappropriate", Scenes::inappropriateScene);
            put("test", Scenes::simpleScene);
            put("problem", Scenes::problem);
            put("colors", Scenes::colors);
            put("teapot", Scenes::teapot);
            put("sphere-shadow", Scenes::sphereShadow);
            put("sphere-shadow-third", Scenes::sphereShadowThird);
            put("scene1", Scenes::scene1);
            put("scene2", Scenes::scene2);
            put("scene3", Scenes::scene3);
            put("illustration", Scenes::illustration);
            put("main", Scenes::mainImage);
        }
    };

    public static void main(String[] args) {
        String outputFile;
        int resolutionX = 960;
        int resolutionY = 600;
        Shader shader = new PhongShader();
        boolean stepByStep;

        // Creating a scene
        Scene scene = Scenes.sphere();

        Options options = new Options();
        options.addOption(Option.builder("o")
                .longOpt("output-file")
                .desc("output file")
                .hasArg()
                .argName("file")
                .build());
        options.addOption(Option.builder("r")
                .longOpt("resolution")
                .desc("resolution of image. 960x600")
                .hasArg()
                .numberOfArgs(2)
                .valueSeparator('x')
                .argName("width>x<height")
                .build());
        options.addOption(Option.builder()
                .longOpt("shader")
                .desc("shader to render scene with")
                .hasArg()
                .argName("shader")
                .build());
        options.addOption(Option.builder("b")
                .longOpt("benchmark")
                .desc("benchmark with given number of spheres and point lights")
                .hasArg()
                .numberOfArgs(2)
                .argName("shapes> <lights")
                .build());
        options.addOption(Option.builder()
                .longOpt("benchmark-shape")
                .desc("shape used for benchmarking")
                .hasArg()
                .argName("shape")
                .build());
        options.addOption(Option.builder()
                .longOpt("benchmark-light")
                .desc("light used for benchmarking")
                .hasArg()
                .argName("light")
                .build());
        options.addOption(Option.builder()
                .longOpt("benchmark-no-default-light")
                .desc("don't add distant light for optics in benchmark scene")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("scene")
                .desc("scene to render")
                .hasArg()
                .argName("scene name")
                .optionalArg(true)
                .build());

        options.addOption("t", "step", false, "Make multiple images with step by step");
        options.addOption("h", "help", false, "show help");

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            outputFile = cmd.getOptionValue("o", "test.png");

            if (cmd.hasOption("r")) {
                String[] valueStrings = cmd.getOptionValues("r");

                resolutionX = Integer.parseInt(valueStrings[0]);
                resolutionY = Integer.parseInt(valueStrings[1]);
            }

            if (cmd.hasOption("shader")) {
                String value = cmd.getOptionValue("shader").toLowerCase();

                if (value.equals("phong")) {
                    shader = new PhongShader();
                } else if (value.equals("unlit")) {
                    shader = new UnlitShader();
                } else if (value.equals("depth")) {
                    shader = new DepthShader(30, false);
                } else if (value.equals("depth-color")) {
                    shader = new DepthShader(30, true);
                } else if (value.equals("black")) {
                    shader = new SingleColorShader(Color.BLACK);
                } else if (value.equals("phong-shadow-red")) {
                    shader = new PhongShadowColorShader(Color.RED);
                } else {
                    System.out.format("Unknown option for shader '%s'.\n" +
                            "Available shaders\n" +
                            "\tphong\n" +
                            "\tunlit\n" +
                            "\tdepth\n" +
                            "\tdepth-color\n" +
                            "\tblack\n" +
                            "\tphong-shadow-red\n", value);

                    System.exit(1); // invalid argument
                    return;
                }
            }

            if (cmd.hasOption("s")) {
                if (cmd.hasOption("b")) {
                    System.out.println("Scene option is not compatible with benchmark option");
                } else {
                    String value = cmd.getOptionValue("s");

                    if (value != null && scenes.containsKey(value.toLowerCase())) {
                        scene = scenes.get(value.toLowerCase()).create();
                    } else {
                        if (value != null) {
                            System.out.printf("%s is not a valid scene\n", value);
                        }

                        System.out.println("Available scenes:");
                        scenes.keySet().forEach(s -> System.out.printf("\t%s\n", s));

                        System.exit(1); // invalid argument
                        return;
                    }
                }
            }

            if (cmd.hasOption("b")) {
                String[] values = cmd.getOptionValues("b");
                int numberOfSpheres = Integer.parseInt(values[0]);
                int numberOfLights = Integer.parseInt(values[1]);

                String shapeName = cmd.getOptionValue("benchmark-shape", "sphere").toLowerCase();
                String lightName = cmd.getOptionValue("benchmark-light", "point").toLowerCase();

                Scenes.BenchmarkShape shapeType = Scenes.BenchmarkShape.Sphere;
                Scenes.BenchmarkLight lightType = Scenes.BenchmarkLight.PointLight;

                if (shapeName.equals("cube") || shapeName.equals("cuboid")) {
                    shapeType = Scenes.BenchmarkShape.Cuboid;
                } else if (!(shapeName.equals("sphere"))) {
                    System.out.printf("Unknown shape %d\n", shapeName);
                    System.out.println("Available shapes:\n\tsphere\n\tcube");

                    System.exit(1); // invalid argument
                    return;
                }

                if (lightName.equals("distant light") || lightName.equals("directional") || lightName.equals("distant")) {
                    lightType = Scenes.BenchmarkLight.DirectionalLight;
                } else if (lightName.equals("spotlight") || lightName.equals("spot")) {
                    lightType = Scenes.BenchmarkLight.Spotlight;
                } else if (!(lightName.equals("point") || lightName.equals("point light"))) {
                    System.out.printf("Unknown light %d\n", lightName);
                    System.out.println("Available lights:\n\tpoint light\n\tdistant light\n\tspotlight");

                    System.exit(1); // invalid argument
                    return;
                }

                scene = Scenes.benchmark(numberOfSpheres, numberOfLights, shapeType, lightType, !cmd.hasOption("benchmark-no-default-light"));
            }

            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("sauerkraut", options);
                return;
            }

            stepByStep = cmd.hasOption("t");
        } catch (ParseException e) {
            e.printStackTrace();

            System.exit(1); // invalid argument
            return;
        }

        if (!stepByStep) {
            // Rendering scene to image and saving to disk
            BufferedImage image = Raytracer.renderScene(scene, shader, resolutionX, resolutionY);
            saveImage(image, outputFile);
        } else {
            java.util.List<BufferedImage> images = Raytracer.renderSceneStepByStep(scene, resolutionX, resolutionY, shader);

            Path file = Paths.get(outputFile);
            if (file.toString().endsWith(".png")) {
                outputFile = outputFile.substring(0, outputFile.length() - 4);
            }

            for (int i = 0; i < images.size(); i++) {
                saveImage(images.get(i), String.format("%s_%03d.png", outputFile, i));
            }
        }
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
