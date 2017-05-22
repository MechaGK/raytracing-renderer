package sauerkraut.shapes;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import sauerkraut.RayHit;
import sauerkraut.materials.Material;
import sauerkraut.Ray;

import java.awt.*;
import sauerkraut.Transform;

/**
 * Abstract class for a shape
 */
public abstract class Shape extends Transform {
    private static final Vector3D UNIT_SCALE = new Vector3D(1, 1, 1);
    
    protected Material material;
    
    public Shape(Material material, Vector3D position) {
        super(position, UNIT_SCALE, Vector3D.ZERO);
        this.material = material;
    }
    
    public Shape(Material material, Vector3D position, Vector3D scale) {
        super(position, scale, Vector3D.ZERO);
        this.material = material;
    }

    public Shape(Material material, Vector3D position, Vector3D scale, Vector3D eulerAngles) {
        super(position, scale, eulerAngles);
        this.material = material;
    }

    /**
     * Returns the point at which the shape is hit by the ray. If there is multiple points the
     * one closest to the ray's origin is returned
     *
     * @param ray The ray which to test against
     * @return Closest intersection point in front of the ray. Null if none.
     */
    public abstract RayHit intersect(Ray ray);

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