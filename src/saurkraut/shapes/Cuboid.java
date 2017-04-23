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
        Ray locRay = rayToLocal(ray);
    	
    	double invDirX = locRay.invDirection.getX();
    	double invDirY = locRay.invDirection.getY();
    	double xSign = locRay.direction.getX() < 0 ? -1 : 1;
    	double ySign = locRay.direction.getY() < 0 ? -1 : 1;

    	double xNear, xFar, yNear, yFar;
    	
	xNear = (-xSign - locRay.origin.getX()) * invDirX;
	xFar = (xSign - locRay.origin.getX()) * invDirX;
	yNear = (-ySign - locRay.origin.getY()) * invDirY; 
	yFar = (ySign - locRay.origin.getY()) * invDirY;
    	
    	if (xNear > yFar || yNear > xFar)
    		return null;
    	
    	double tNear = Math.max(xNear, yNear);
    	double tFar = Math.min(xFar, yFar);
    	
    	double zNear, zFar;
        double invDirZ = locRay.invDirection.getZ();
    	double zSign = locRay.direction.getZ() < 0 ? -1 : 1;
    	
    	zNear = (-zSign - locRay.origin.getZ()) * invDirZ;        
    	zFar = (zSign - locRay.origin.getZ()) * invDirZ;
    	
    	if (tNear > zFar || zNear >= tFar)
    		return null;
    	
    	if (zNear > tNear) tNear = zNear;
    	if (zFar < tFar) tFar = zFar;
    
    	return pointToWorld(locRay.getPoint(tNear));
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        Vector3D l = pointToLocal(point);
        double xs = Math.copySign(1, l.getX());
        double ys = Math.copySign(1, l.getY());
        double zs = Math.copySign(1, l.getZ());
        
        double x = Math.abs(l.getX() - xs);
        double y = Math.abs(l.getY() - ys);
        double z = Math.abs(l.getZ() - zs);
        
        Vector3D result;
        
        if (x < y && x < z)
            result = new Vector3D(xs, 0, 0);
        else if (y < x && y < z)
            result = new Vector3D(0, ys, 0);
        else if (z < x && z < y)
            result = new Vector3D(0, 0, zs);
        else
            result = Vector3D.ZERO;
        
        return directionToWorld(result);
    }

    @Override
    public Color getColor(Vector3D point) {
        return material.getColor(0.5, 0.5);
    }
    
}
