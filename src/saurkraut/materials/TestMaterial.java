package saurkraut.materials;

import saurkraut.coordinates.Coordinate;
import saurkraut.materials.Material;
import saurkraut.util.ColorUtil;

import java.awt.*;

/**
 * Testing making patterns with material
 */
public class TestMaterial extends Material {
    public TestMaterial(float albedo, float specularExponent, float specularStrength) {
        super(albedo, specularExponent, specularStrength);
    }
    public TestMaterial(float albedo, float specularExponent) {
        super(albedo, specularExponent);
    }
    public TestMaterial(float albedo) {
        super(albedo);
    }

    
    /*@Override
    public Color getColor(double sigma, double theta) {
        Color color = Color.green;

        if (sigma < 0.5d && sigma >= 0.0d) {
            color = ColorUtil.add(color, Color.red);
        }

        if (theta < 0.5d && theta >= 0d) {
            color = ColorUtil.add(color, Color.blue);
        }

        return color;
    }*/
    
    @Override
    public Color getColor(Coordinate coord) {
      Color color = Color.green;
      
      if(coord.getScalarSigma() < 0.5d && coord.getScalarSigma() >= 0.0d) {
        color = ColorUtil.add(color, Color.red);
      }
      
      if(coord.getScalarTheta() < 0.5d && coord.getScalarTheta() >= 0.0d) {
        color = ColorUtil.add(color, Color.blue);
      }
      
      return color;
    }
}
