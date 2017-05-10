package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.shapes.Shape;

import java.awt.*;

public interface Shader {
    Color shade(Scene scene, Shape shape, Vector3D point, Vector3D viewDirection);
}
