package raytracing;

import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

/**
 * Abstract class for a shape
 */
public abstract class Shape {
    protected Vector3D position;
    protected Material material;

    /**
     * Returns the point at which the shape is hit by the ray. If there is multiple points the
     * one closest to the ray's origin is returned
     * @param ray The ray which to test against
     * @return Closest intersection point in front of the ray. Null if none.
     */
    public abstract Vector3D intersect(Ray ray);

    /**
     * Returns the normal at the given point
     * @param point the at where to calculate normal
     * @return normal calculated from point
     */
    public abstract Vector3D getNormal(Vector3D point);

    /**
     * Returns the color at the point based on material only
     * @param point The point to base the color on
     * @return Color of the shape at the specified point
     */
    public abstract Color getColor(Vector3D point);

    /**
     * Getter for shape's material
     * @return The material of the shape
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the shape's material
     * @param material The new material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}
