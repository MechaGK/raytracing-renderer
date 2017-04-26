package saurkraut.coordinates;

import saurkraut.coordinates.Coordinate;

public class SphericalCoordinate extends Coordinate {
  
  double sigma, theta; //Horizontal, vertical
  int width, height; //Used only if integer coordinates are supported.
  
  public SphericalCoordinate(double sigma, double theta) {
    this.sigma = sigma;
    this.theta = theta;
    this.width = -1;
    this.height = -1;
  }
  
  public SphericalCoordinate(double sigma, double theta, int width, int height) {
    this.sigma = sigma;
    this.theta = theta;
    this.width = width;
    this.height = height;
  }
  
  public double getScalarSigma() {
    return sigma;
  }
  
  public double getScalarTheta() {
    return theta;
  }
  
  public int getIntegerX() {
    if(width < 1) {
      return -1;
    }
    
    return ((int)(sigma*width)) % width;
  }
  
  public int getIntegerY() {
    if(height < 1) {
      return -1;
    }
    
    return ((int)(theta*height)) % height;
  }
  
}

