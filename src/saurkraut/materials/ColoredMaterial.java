package saurkraut.materials;

import saurkraut.coordinates.Coordinate;

import java.awt.*;

public class ColoredMaterial extends Material {
    public ColoredMaterial(Color color, float albedo, float specularExponent, float specularStrength) {
        super(albedo, specularExponent, specularStrength);
        this.color = color;
    }
    public ColoredMaterial(Color color, float albedo, float specularExponent) {
        super(albedo, specularExponent);
        this.color = color;
    }
    public ColoredMaterial(Color color, float albedo) {
        super(albedo);
        this.color = color;
    }
    
    private Color color;

    @Override
    //public Color getColor(double sigma, double theta) {
    public Color getColor(Coordinate coord) {
        return color;
    }
}
