package raytracing;

import java.awt.*;

/**
 * Created by Mads Grau Kristensen <madsgrau@gmail.com> on 17/04/2017.
 */
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
