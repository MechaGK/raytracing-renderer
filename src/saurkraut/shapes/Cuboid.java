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
      //Everything in here should be in local space. If it isn't, then something is going to break.
      
      //Get the normal from the surface that is hit.
      Vector3D SurfaceNormal = directionToLocal(getNormal(point));
      
      //The local axes for the face.
      Vector3D YAxis = new Vector3D(SurfaceNormal.getY(), SurfaceNormal.getZ(), -SurfaceNormal.getX()).normalize();
      Vector3D XAxis = Vector3D.crossProduct(SurfaceNormal, YAxis).normalize();
      
      //The scalar coordinates.
      //The +1.0 moves the -1.0 - 1.0 range into the 0.0 - 2.0 range, and dividing by two reduces that to a 0.0 - 1.0 range, which is what the SphericalCoordinate class works with. This also stretches the texture over the whole cuboid face, thus in a way uses the cuboid diameter instead of the cuoid radius.
      double x = (Vector3D.dotProduct(pointToLocal(point), XAxis)+1.0)/2;
      double y = (Vector3D.dotProduct(pointToLocal(point), YAxis)+1.0)/2;
      
      return material.getColor(new SphericalCoordinate(x, y));
    }

}
