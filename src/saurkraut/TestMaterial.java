package saurkraut;

import saurkraut.util.ColorUtil;

import java.awt.*;

/**
 * Testing making patterns with material
 */
public class TestMaterial extends Material {
    public TestMaterial(float albedo) {
        super(albedo);
    }

    @Override
    public Color getColor(double sigma, double theta) {
        Color color = Color.green;

        if (sigma < 0.5d && sigma >= 0.0d) {
            color = ColorUtil.add(color, Color.red);
        }

        if (theta < 0.5d && theta >= 0d) {
            color = ColorUtil.add(color, Color.blue);
        }

        return color;
    }
}
