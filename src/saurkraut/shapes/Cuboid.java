package saurkraut.shapes;

import java.awt.Color;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.Material;
import saurkraut.Ray;


public class Cuboid extends Shape {

    public Cuboid(Material material, Vector3D position, Vector3D scale, Vector3D eulerAngles) {
        super(material, position, scale, eulerAngles);
    }

    @Override
    public Vector3D intersect(Ray ray) {
    	Vector3D rayOrigin = pointToLocal(ray.origin);
    	Vector3D rayDir = directionToLocal(ray.direction);
    	
    	double invDirX = 1/rayDir.getX();
    	double invDirY = 1/rayDir.getY();
    	double invDirZ = 1/rayDir.getZ();
    	double xSign = invDirX < 0 ? -1 : 1;
    	double ySign = invDirY < 0 ? -1 : 1;

    	double t0x, t1x, t0y, t1y;
    	
	  	t0x = (-xSign - rayOrigin.getX()) * invDirX; 
	  	t1x = (xSign - rayOrigin.getX()) * invDirX;
	    t0y = (-ySign - rayOrigin.getY()) * invDirY; 
	    t1y = (ySign - rayOrigin.getY()) * invDirY;
    	
    	if (t0x > t1y || t0y > t1x)
    		return null;
    	
    	double t0 = Math.max(t0x, t0y);
    	double t1 = Math.min(t1x, t1y);
    	
    	double t0z, t1z;
    	double zSign = invDirZ < 0 ? -1 : 1;
    	
    	t0z = (-zSign - rayOrigin.getY()) * invDirZ; 
    	t1z = (zSign - rayOrigin.getY()) * invDirZ;
    	
    	if (t0 > t1z || t1 < t0z)
    		return null;
    	
    	if (t0z > t0) t0 = t0z;
    	if (t1z < t1) t1 = t1z;
    
    	return pointToWorld(rayOrigin.add(rayDir.scalarMultiply(t0)));
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        return Vector3D.MINUS_K;
    }

    @Override
    public Color getColor(Vector3D point) {
        return Color.CYAN;
    }
    
}
