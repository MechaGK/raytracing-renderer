package saurkraut.materials;

import saurkraut.materials.Material;

import java.awt.*;

public class ColoredMaterial extends Material {
    private Color color;

    public ColoredMaterial(Color color, float albedo) {
        super(albedo);
        this.color = color;
    }

    @Override
    public Color getColor(double sigma, double theta) {
        return color;
    }
}
