package raytracing;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Sphere extends Shape {
    protected float radius;

    public Sphere(Material material, Vector3D position, float radius) {
        this.material = material;
        this.position = position;
        this.radius = radius;
    }

    @Override
    public Vector3D intersect(Ray ray) {
        throw new NotImplementedException("Sphere intersect not yet implemented");
    }
}
