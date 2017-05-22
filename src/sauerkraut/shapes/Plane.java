package sauerkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.RayHit;
import sauerkraut.coordinates.Coordinate;
import sauerkraut.coordinates.GridCoordinate;
import sauerkraut.materials.Material;
import sauerkraut.Ray;

import java.awt.*;

public class Plane extends Shape {
    private final Vector3D normal;
    private final Vector3D XAxis;
    private final Vector3D YAxis;
    private final int virtualWidth;
    private final int virtualHeight;

    public Plane(Material material, Vector3D position, Vector3D normal, int width, int height) {
        super(material, position);

        if (normal.getNorm() > 0) {
            this.normal = normal.normalize();
        } else {
            this.normal = new Vector3D(0, 1, 0);
        }
        
        this.YAxis = new Vector3D(this.normal.getY(), this.normal.getZ(), -this.normal.getX()).normalize();
        this.XAxis = Vector3D.crossProduct(this.normal, this.YAxis).normalize();
        
        virtualWidth = width;
        virtualHeight = height;
    }
    
    public Plane(Material material, Vector3D position, Vector3D normal) {
      this(material, position, normal, -1, -1);
    }

    @Override
    public RayHit intersect(Ray ray) {
        double test = ray.direction.dotProduct(normal);

        if (test == 0) {
            return null;
        }

        double scalar = position.subtract(ray.origin).dotProduct(normal);
        scalar = scalar / test;

        if (scalar < 0) {
            return null;
        }

        return new RayHit(ray.getPoint(scalar), this, this.normal);
    }
  
    @Override
    public Color getColor(Vector3D point) {
        
        //THE ABS PART IS A GROSS HACK THAT CAUSES MIRRORED TILING. PLEASE HELP FIX. :(
        int x = ((int)(Vector3D.dotProduct(point, XAxis)));
        int y = ((int)(Vector3D.dotProduct(point, YAxis)));
        Coordinate coord = null;
        
        //Coordinate coord = new GridCoordinate(x, y); //Infinitely large, no width or height.
        
        if (virtualWidth > 0) {
          coord = new GridCoordinate(x, y, virtualWidth, virtualHeight);
        } else {
          coord = new GridCoordinate(x, y);
        }
        
        //System.out.println(coord.getIntegerX() + "   " + coord.getIntegerY());
        
        return material.getColor(coord);
        
        //DEPRECATED! >:D
        //return material.getColor(0, 0); //TODO: implement spherical coordinates for plane
    }
}
