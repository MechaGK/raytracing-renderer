package raytracing;

import java.util.Iterator;

public abstract class Camera {
    /**
     * Iterator over rays from camera. Rays are generated based on the given resolution
     * @param resolutionX the vertical resolution
     * @param resolutionY the horizontal resolution
     * @return iterator over rays
     */
    public abstract Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY);
}
