package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

public class BooleanShader implements Shader {
    private final Color color;

    public BooleanShader(Color color) {
        this.color = color;
    }

    @Override
    public Color shade(Scene scene, saurkraut.shapes.Shape shape, Vector3D point, Vector3D normal, Vector3D viewDirection) {
        return color;
    }
}
