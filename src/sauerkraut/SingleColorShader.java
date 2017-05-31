package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

public class SingleColorShader implements Shader {
    private final Color color;

    public SingleColorShader(Color color) {
        this.color = color;
    }

    @Override
    public Color shade(Scene scene, sauerkraut.shapes.Shape shape, Vector3D point, Vector3D normal, Vector3D viewDirection) {
        return color;
    }
}
