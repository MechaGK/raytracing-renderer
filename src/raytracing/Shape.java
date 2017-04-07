package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Abstract class for a shape
 */
public abstract class Shape {
    protected Material material;

    /**
     * Returns the point at which the shape is hit by the ray.
     * @param ray The ray which to test against
     * @return Point where shape is hit. Returns null if not hit.
     */
    public abstract Vector3D intersect(Ray ray);

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
