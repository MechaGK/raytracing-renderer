package sauerkraut;

import java.util.Iterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class Camera extends Transform {

    public Camera(Vector3D position, Vector3D eulerAngles) {
        super(position, new Vector3D(1, 1, 1), eulerAngles);
    }
    /**
     * Iterator over rays from camera. Rays are generated based on the given resolution
     * @param resolutionX the vertical resolution
     * @param resolutionY the horizontal resolution
     * @return iterator over rays
     */
    public abstract Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY);

    /**
     * Set camera to look at point
     * @param point point to look at
     */
    public abstract void lookAt(Vector3D point);
}