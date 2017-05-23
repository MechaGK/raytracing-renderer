package sauerkraut;

import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static HashMap<String, SceneCreator> scenes = new HashMap<String, SceneCreator>() {
        {
            put("sphere", Scenes::sphere);
        }
    };

    public static void main(String[] args) {
        String outputFile = "test.png";
        int resolutionX = 960;
        int resolutionY = 600;
        Shader shader = new PhongShader();
        boolean stepByStep = false;

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
                .desc("shader to render scene with. 'phong' or 'unlit'")
                .hasArg()
                .argName("phong/unlit")
                .build());
        options.addOption(Option.builder("b")
                .longOpt("benchmark")
                .desc("Benchmark with given number of spheres and point lights")
                .hasArg()
                .numberOfArgs(2)
                .argName("spheres> <point lights")
                .build());

        options.addOption("t", "step", false, "Make multiple images with step by step");
        options.addOption("s", "scene", true, "scene to render");
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

            if (cmd.hasOption("d")) {
                String value = cmd.getOptionValue("d").toLowerCase();

                if (Objects.equals(value, "phong")) {
                    shader = new PhongShader();
                } else if (Objects.equals(value, "unlit")) {
                    shader = new UnlitShader();
                } else {
                    System.out.format("Unknown option for shader '%s'. Accepted values are 'phong' and 'unlit'. Shading using Phong.", value);
                }
            }

            if (cmd.hasOption("s")) {
                if (cmd.hasOption("b")) {
                    System.out.println("Scene option is not compatible with benchmark option");
                }
                else {
                    String value = cmd.getOptionValue("s").toLowerCase();

                    if (scenes.containsKey(value)) {
                        scene = scenes.get(value).create();
                    } else {
                        System.out.printf("%s is not a valid scene\nAvailable scenes: \n", value);
                        scenes.keySet().forEach(s -> System.out.printf("\t%s\n", s));

                        return;
                    }
                }
            }

            if (cmd.hasOption("b")) {
                String[] values = cmd.getOptionValues("b");
                int numberOfSpheres = Integer.parseInt(values[0]);
                int numberOfLights = Integer.parseInt(values[1]);

                scene = Scenes.benchmark(numberOfSpheres, numberOfLights);
            }

            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("sauerkraut", options);
                return;
            }

            stepByStep = cmd.hasOption("t");
        } catch (ParseException e) {
            e.printStackTrace();
            outputFile = "test.png";
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
