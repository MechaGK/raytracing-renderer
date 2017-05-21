package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.shapes.Shape;

import java.awt.*;

public class UnlitShader implements Shader {
    @Override
    public Color shade(Scene scene, Shape shape, Vector3D point, Vector3D normal, Vector3D viewDirection) {
        return shape.getColor(point);
    }
}
