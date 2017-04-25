package saurkraut.materials;

import saurkraut.materials.Texture;
import saurkraut.materials.TexturedMaterial;
import saurkraut.util.ColorUtil;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Material used to draw textures in a tiling fashion.
 */
public class StretchedTexturedMaterial extends TexturedMaterial {
  
  public StretchedTexturedMaterial(float albedo, BufferedImage image) {
    super(albedo, image);
  }
  
  public Color getColor(double sigma, double theta) {
    return texture.getColor(((int)((theta) * texture.height)) % texture.height, texture.width -1 - (((int)(sigma * texture.width)) % texture.width)); //Proper!
  }
  
  
}
