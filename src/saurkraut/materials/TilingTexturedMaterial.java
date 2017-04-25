package saurkraut.materials;

import saurkraut.materials.Texture;
import saurkraut.materials.TexturedMaterial;
import saurkraut.util.ColorUtil;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Material used to draw textures in a tiling fashion.
 */
public class TilingTexturedMaterial extends TexturedMaterial {
  
  public TilingTexturedMaterial(float albedo, BufferedImage image) {
    super(albedo, image);
  }
  
  public Color getColor(double sigma, double theta) {
    //return texture.getColor(texture.height-1 - (((int)((theta) * texture.height * 5)) % texture.height), texture.width -1 - (((int)(sigma * texture.width * 5)) % texture.width)); //Proper?
    //return texture.getColor(texture.height-1 - (((int)((theta) * texture.height * 5)) % texture.height), ((int)(sigma * texture.width * 5)) % texture.width); //Proper?
    
    //Why is a horizontal flip neccesary???
    return texture.getColor(((int)((theta) * texture.height * 5)) % texture.height, texture.width -1 - (((int)(sigma * texture.width * 5)) % texture.width)); //Proper!
    
    
    //return texture.getColor(((int)(sigma * texture.width * 5)) % texture.width, ((int)(theta * texture.height * 5)) % texture.height); //Rotate 90 deg to the right.
    //return texture.getColor(((int)(theta * texture.height * 5)) % texture.height, ((int)(sigma * texture.width * 5)) % texture.width); //Horizontal flip.
    //return texture.getColor(((int)(sigma * texture.height * 5)) % texture.height, ((int)(theta * texture.width * 5)) % texture.width); //Rotate 90 deg to the right.
    //return texture.getColor(((int)(theta * texture.width * 5)) % texture.width, ((int)(sigma * texture.height * 5)) % texture.height); //Horizontal flip.
    
    //return Color.white;
  }
  
}
