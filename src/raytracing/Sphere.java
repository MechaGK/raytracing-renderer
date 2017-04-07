package raytracing;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Sphere extends Shape {
    public Sphere(Material material) {
        this.material = material;
    }

    @Override
    public Vector3D intersect(Ray ray) {
        throw new NotImplementedException("Sphere intersect not yet implemented");
    }
}
