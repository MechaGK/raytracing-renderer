package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.util.ColorUtil;

import java.awt.*;

public class DepthShader implements Shader {
    private final double maxDistance;
    private final boolean addShapeColor;

    public DepthShader(double maxDistance, boolean addShapeColor) {
        this.maxDistance = maxDistance;
        this.addShapeColor = addShapeColor;
    }

    @Override
    public Color shade(Scene scene, saurkraut.shapes.Shape shape, Vector3D point, Vector3D normal, Vector3D viewDirection) {
        Vector3D cameraPosition = scene.getCamera().position;

        float depth = (float) Math.min(Math.max((cameraPosition.distance(point) / maxDistance), 0), 1);

        Color depthColor = new Color(1 - depth, 1 - depth, 1 - depth);

        if (addShapeColor) {
            return ColorUtil.multiply(depthColor, shape.getColor(point));
        }

        return depthColor;
    }
}
