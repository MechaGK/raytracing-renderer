package sauerkraut.materials;

import sauerkraut.materials.Material;
import sauerkraut.materials.Texture;
import sauerkraut.util.ColorUtil;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Class to hold information for rendering materials with textures
 */
public abstract class TexturedMaterial extends Material {
  
  Texture texture;
  
  public TexturedMaterial(float albedo, BufferedImage image) {
      this(albedo, image, 50);
  }
  public TexturedMaterial(float albedo, BufferedImage image, float specularExponent) {
    super(albedo, specularExponent);
    texture = new Texture(image);
  }
  
}

