package saurkraut.coordinates;

import saurkraut.coordinates.Coordinate;

public class GridCoordinate extends Coordinate {
  
  int x, y, //Coordinates.
  width, height; //Used only if scalar coordinates are supported, which they should be.
  
  public GridCoordinate(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = -1;
    this.height = -1;
  }
  
  public GridCoordinate(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public double getScalarSigma() {
    if(width < 1) {
      return -1.0;
    }
    
    return ((double)x)/width;
  }
  
  public double getScalarTheta() {
    if(height < 1) {
      return -1.0;
    }
    
    return ((double)y)/height;
  }
  
  public int getIntegerX() {
    return x;
  }
  
  public int getIntegerY() {
    return y;
  }
  
}

