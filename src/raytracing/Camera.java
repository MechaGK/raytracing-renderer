package raytracing;

import java.util.Iterator;

public abstract class Camera {
    /**
     * Iterator over rays from camera
     * @param resolutionX the vertical resolution
     * @param resolutionY the horizontal resolution
     * @return iterator over rays
     */
    public abstract Iterator<CameraRay> rays(int resolutionX, int resolutionY);

}
