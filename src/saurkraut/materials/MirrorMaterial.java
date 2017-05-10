package saurkraut.materials;

import saurkraut.coordinates.Coordinate;

import java.awt.*;

public class MirrorMaterial extends Material {
    public MirrorMaterial(float albedo) {
        super(albedo);
    }

    @Override
    public Color getColor(Coordinate coord) {
        return null;
    }
}
