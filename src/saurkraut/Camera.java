package saurkraut;

import java.util.Iterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class Camera extends SceneObject {

    public Camera(Vector3D position, Vector3D eulerRotation) {
        super(position, new Vector3D(1, 1, 1), eulerRotation);
    }
    /**
     * Iterator over rays from camera. Rays are generated based on the given resolution
     * @param resolutionX the vertical resolution
     * @param resolutionY the horizontal resolution
     * @return iterator over rays
     */
    public abstract Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY);
}
