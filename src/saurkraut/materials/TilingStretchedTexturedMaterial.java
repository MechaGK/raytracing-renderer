package saurkraut.materials;

import saurkraut.coordinates.Coordinate;
import saurkraut.materials.Texture;
import saurkraut.materials.TexturedMaterial;
import saurkraut.util.ColorUtil;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Material used to draw textures in a tiling fashion, but on stretched surfaces.
 */
public class TilingStretchedTexturedMaterial extends TexturedMaterial {
  
  int ytiles, xtiles;
  
  public TilingStretchedTexturedMaterial(float albedo, BufferedImage image, int xtiles, int ytiles) {
    super(albedo, image);
    this.ytiles = ytiles;
    this.xtiles = xtiles;
  }
  
  public Color getColor(Coordinate coord) {
    return texture.getColor(((int)(coord.getScalarTheta() * texture.height * ytiles)) % texture.height, texture.width -1 - (((int)(coord.getScalarSigma() * texture.width * xtiles)) % texture.width));
    //return texture.getColor(((int)(coord.getScalarTheta() * texture.height)) % texture.height, ((int)(coord.getScalarSigma() * texture.width)) % texture.width);
    
    //return texture.getColor(((int)((theta) * texture.height)) % texture.height, texture.width -1 - (((int)(sigma * texture.width)) % texture.width)); //Proper!
  }
  
  
}
