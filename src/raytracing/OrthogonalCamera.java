package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;

/**
 * For testing. Doesn't support directions other directions than (0, 0, 1).
 * Origin of raysIterator are not rotated as they should
 */
public class OrthogonalCamera extends Camera {
    private double width;
    private double height;

    private Vector3D origin;

    private Vector3D direction;

    class RaysIterator implements Iterator<CameraRay> {
        private final int resWidth;
        private final int resHeight;

        private int x;
        private int y;

        public RaysIterator(int width, int height) {
            this.resWidth = width;
            this.resHeight = height;

            this.x = 0;
            this.y = 0;
        }

        @Override
        public boolean hasNext() {
            return x < resWidth || y + 1 < resHeight;
        }

        @Override
        public CameraRay next() {
            Vector3D rayOrigin = new Vector3D(
                    -(width / 2) + ((width / resWidth) * x),
                    (height / 2) - ((height / resHeight) * y),
                    0).add(origin);

            Ray ray = new Ray(rayOrigin, direction);
            CameraRay cameraRay = new CameraRay(ray, x, y);

            if (x + 1 == resWidth && y + 1 < resHeight) {
                x = 0;
                y++;
            } else {
                x++;
            }

            return cameraRay;
        }
    }

    public OrthogonalCamera(Vector3D origin, Vector3D direction, double width, double height) {
        this.origin = origin;
        this.direction = direction.normalize();
        this.width = width;
        this.height = height;
    }

    @Override
    public Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY) {
        return new RaysIterator(resolutionX, resolutionY);
    }
}
