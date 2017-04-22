package saurkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.Material;
import saurkraut.Ray;

import java.awt.*;

/**
 * Abstract class for a shape
 */
public abstract class Shape {
    private static final Vector3D UNIT_SCALE = new Vector3D(1, 1, 1);
    
    // Local to world transformation matrix
    private final double
            LW00, LW01, LW02, LW03,
            LW10, LW11, LW12, LW13,
            LW20, LW21, LW22, LW23;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    // World to local transformation matrix
    private final double        
            WL00, WL01, WL02, WL03,
            WL10, WL11, WL12, WL13,
            WL20, WL21, WL22, WL23;
    //      0   , 0   , 0   , 1   <- (implicit)

    protected final Vector3D position;
    protected Material material;
    
    public Shape(Material material, Vector3D position) {
        this(material, position, UNIT_SCALE, Vector3D.ZERO);
    }
    public Shape(Material material, Vector3D position, Vector3D scale) {
        this(material, position, scale, Vector3D.ZERO);
    }

    public Shape(Material material, Vector3D position, Vector3D scale, Vector3D eulerAngles) {
        this.position = position;
        
        double scaleX = scale.getX(), invScaleX = 1/scaleX;
        double scaleY = scale.getY(), invScaleY = 1/scaleY;
        double scaleZ = scale.getZ(), invScaleZ = 1/scaleZ;
        
        double angX = eulerAngles.getX();
        double angY = eulerAngles.getX();
        double angZ = eulerAngles.getX();
        
        double cX = Math.cos(angX);
        double sX = Math.sin(angX);
        double cY = Math.cos(angY);
        double sY = Math.sin(angY);
        double cZ = Math.cos(angZ);
        double sZ = Math.sin(angZ);
        
        double r00, r01, r02, r10, r11, r12, r20, r21, r22;
        r00 = cZ*cY;    r01 = cZ*sX*sY-cX*sZ;   r02 = cX*cZ*sY+sX*sZ;
        r10 = sZ*cY;    r11 = sX*sY*sZ+cX*cZ;   r12 = cX*sY*sZ-cZ*sX;
        r20 = -sY;      r21 = cY*sX;            r22 = cY*cX;

        // LOCAL TO WORLD SPACE TRANSFORM
        // [translation].[scale].[rotation]
        // Applies rotation, then scale, then translation
        LW00 = scaleX*r00; LW01 = scaleX*r01; LW02 = scaleX*r02; LW03 = position.getX();
        LW10 = scaleY*r10; LW11 = scaleY*r11; LW12 = scaleY*r12; LW13 = position.getY();
        LW20 = scaleZ*r20; LW21 = scaleZ*r21; LW22 = scaleZ*r22; LW23 = position.getZ();

        // WORLD TO LOCAL SPACE TRANSFORM
        // The inverse
        // [transpose rotation].[inverse scale].[inverse translation]
        // Applies inverse translation, then inverse scale, then transposed rotation
        WL00 = invScaleX*r00; WL01 = invScaleX*r10; WL02 = invScaleX*r20; WL03 = -WL00*LW03 - WL01*LW13 - WL02*LW23;
        WL10 = invScaleY*r01; WL11 = invScaleY*r11; WL12 = invScaleY*r21; WL13 = -WL10*LW03 - WL11*LW13 - WL12*LW23;
        WL20 = invScaleZ*r02; WL21 = invScaleZ*r12; WL22 = invScaleZ*r22; WL23 = -WL20*LW03 - WL21*LW13 - WL22*LW23;
        
        this.material = material;
    }
    
    public Vector3D getPosition() {
        return position;
    }

    public Vector3D localToWorld(Vector3D localPoint) {
        double lx = localPoint.getX();
        double ly = localPoint.getY();
        double lz = localPoint.getZ();
        
        return new Vector3D(
                lx*LW00 + ly*LW01 + lz*LW02 + LW03,
                lx*LW10 + ly*LW11 + lz*LW12 + LW13,
                lx*LW20 + ly*LW21 + lz*LW22 + LW23
        );
    }
    
    public Vector3D worldToLocal(Vector3D worldPoint) {
        double wx = worldPoint.getX();
        double wy = worldPoint.getY();
        double wz = worldPoint.getZ();
        
        return new Vector3D(
                wx*WL00 + wy*WL01 + wz*WL02 + WL03,
                wx*WL10 + wy*WL11 + wz*WL12 + WL13,
                wx*WL20 + wy*WL21 + wz*WL22 + WL23
        );
    };

    /**
     * Returns the point at which the shape is hit by the ray. If there is multiple points the
     * one closest to the ray's origin is returned
     *
     * @param ray The ray which to test against
     * @return Closest intersection point in front of the ray. Null if none.
     */
    public abstract Vector3D intersect(Ray ray);

    /**
     * Returns the normal at the given point
     *
     * @param point the at where to calculate normal
     * @return normal calculated from point
     */
    public abstract Vector3D getNormal(Vector3D point);

    /**
     * Returns the color at the point based on material only
     *
     * @param point The point to base the color on
     * @return Color of the shape at the specified point
     */
    public abstract Color getColor(Vector3D point);

    /**
     * Getter for shape's material
     *
     * @return The material of the shape
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the shape's material
     *
     * @param material The new material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}