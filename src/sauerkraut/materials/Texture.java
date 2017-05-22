package sauerkraut.materials;

import sauerkraut.util.ColorUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.*;

public class Texture {
  Color[][] texture;
  final int width;
  final int height;
  
  public Texture(BufferedImage image) {
    this.width = image.getWidth();
    this.height = image.getHeight();
    
    texture = toColors(image);
  }
  
  //Heavily inspired from something on Stack Overflow
  //This ripoff assumes that the image has no alpha channel. If in doubt, use .jpg files.
  protected Color[][] toColors(BufferedImage image) {
    final byte[] byteData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    //final int width = image.getWidth();
    //final int height = image.getHeight();
    
    //First index is row, the second index is the pixel on that row. (or column in general)
    Color[][] pixels = new Color[height][width];
    
    //This looks horrendous, but I'm too tired to remodel it.
    for(int dataPoint = 0, row = 0, col = 0; dataPoint < byteData.length; dataPoint += 3) {
     //I love the excessive java byte magic used here.
     pixels[row][col] = new Color((int) byteData[dataPoint+2] & 0xFF, (int) byteData[dataPoint+1] & 0xFF, (int) byteData[dataPoint] & 0xFF);
      
      col++;
      if(col == width) {
        col = 0;
        row++;
      }
      
    }
    
    return pixels;
  }
  
  public Color getColor(int x, int y) {
    return texture[x][y];
  }
}

