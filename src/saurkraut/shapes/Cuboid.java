package saurkraut.shapes;

import java.awt.Color;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.RayHit;
import saurkraut.coordinates.SphericalCoordinate;
import saurkraut.materials.Material;
import saurkraut.Ray;


public class Cuboid extends Shape {

    public Cuboid(Material material, Vector3D position, Vector3D halfDimensions, Vector3D eulerAngles) {
        super(material, position, halfDimensions, eulerAngles);
    }

    @Override
    public RayHit intersect(Ray ray) {
        Ray locRay = rayToLocal(ray);

        double invDirX = locRay.invDirection.getX();
        double invDirY = locRay.invDirection.getY();
        double xSign = Math.copySign(1, locRay.direction.getX());
        double ySign = Math.copySign(1, locRay.direction.getY());

        double normalX = 0, normalY = 0, normalZ = 0;
        double xNear, xFar, yNear, yFar;

        xNear = (-xSign - locRay.origin.getX()) * invDirX;
        xFar = (xSign - locRay.origin.getX()) * invDirX;
        yNear = (-ySign - locRay.origin.getY()) * invDirY;
        yFar = (ySign - locRay.origin.getY()) * invDirY;

        if (xNear > yFar || yNear > xFar) // || Double.isNaN(xNear) || Double.isNaN(xFar) || Double.isNaN(yNear) || Double.isNaN(yFar))
            return null;
        
        double t, tFar;
        
        // near hit on x/y plane
        if (xNear > yNear) {
            t = xNear;
            normalX = -xSign;
        }
        else {
            t = yNear;
            normalY = -ySign;
        }

        // far hit on x/y plane
        if (xFar < yFar) {
            tFar = xFar;
        }
        else {
            tFar = yFar;
        }

        double zNear, zFar;
        double invDirZ = locRay.invDirection.getZ();
        double zSign = Math.copySign(1, locRay.direction.getZ());

        zNear = (-zSign - locRay.origin.getZ()) * invDirZ;
        zFar = (zSign - locRay.origin.getZ()) * invDirZ;

        if (t > zFar || zNear >= tFar)
            return null; // We miss on the z-axis
        
        // We hit on the z-axis

        if (zNear > t) {
            t = zNear;
            normalX = normalY = 0;
            normalZ = -zSign;
        }
        
        if (t < 0) {
            return null; 
        }
        //if (zFar < tFar) tFar = zFar;

        //Vector3D normalOld = getNormal(pointToWorld(locRay.getPoint(t)));
        Vector3D normal = directionToWorld(new Vector3D(normalX, normalY, normalZ));

        //System.out.println(""+ normalOld +", "+ normal +"\n"); YES! THEY ARE THE SAME!
        
        
        return new RayHit(pointToWorld(locRay.getPoint(t)), this, normal);
    }

    public Vector3D getNormal(Vector3D point) {
        Vector3D l = pointToLocal(point);
        double xs = Math.copySign(1, l.getX());
        double ys = Math.copySign(1, l.getY());
        double zs = Math.copySign(1, l.getZ());

        double x = Math.abs(l.getX() - xs);
        double y = Math.abs(l.getY() - ys);
        double z = Math.abs(l.getZ() - zs);

        Vector3D result;

        if (x <= y && x <= z)
            result = new Vector3D(xs, 0, 0);
        else if (y <= x && y <= z)
            result = new Vector3D(0, ys, 0);
        else if (z <= x && z <= y)
            result = new Vector3D(0, 0, zs);
        else
            result = Vector3D.ZERO;

        return directionToWorld(result);
    }

    @Override
    public Color getColor(Vector3D point) {
        //Replace this with a sensical GridCoordinate.
        return material.getColor(new SphericalCoordinate(0.5, 0.5));

        //return material.getColor(0.5, 0.5);
    }

}
