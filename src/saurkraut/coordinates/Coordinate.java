package sauerkraut.coordinates;

public abstract class Coordinate {
  
  public abstract double getScalarSigma(); //Returns coordinates in range 0.0-1.0
  public abstract double getScalarTheta(); //Sigma = horizontal (left & right), theta = vertical (up & down)
  
  public abstract int getIntegerX(); //Return integer coordinates.
  public abstract int getIntegerY(); //X = horizontal (left & right), y = vertical (up & down)
  
}

