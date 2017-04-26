package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.shapes.Shape;

import java.awt.*;

public class UnlitShader {
    public static Color shade(Shape shape, Vector3D point) {
        return shape.getColor(point);
    }
}
