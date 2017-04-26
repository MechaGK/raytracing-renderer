package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.shapes.Shape;

import java.awt.*;

/**
 * Created by Mads Grau Kristensen <madsgrau@gmail.com> on 18/04/2017.
 */
public class UnlitShader {
    public static Color shade(Shape shape, Vector3D point) {
        return shape.getColor(point);
    }
}
