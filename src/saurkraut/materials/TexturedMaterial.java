package saurkraut.materials;

import saurkraut.materials.Material;
import saurkraut.materials.Texture;
import saurkraut.util.ColorUtil;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Class to hold information for rendering materials with textures
 */
public abstract class TexturedMaterial extends Material {
  
  Texture texture;
  
  public TexturedMaterial(float albedo, BufferedImage image) {
    super(albedo);
    texture = new Texture(image);
  }
  
}

