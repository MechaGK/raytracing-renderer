package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;

/**
 * For testing. Doesn't support directions other directions than (0, 0, 1).
 * Origin of raysIterator are not rotated as they should
 */
public class OrthogonalCamera extends Camera {
    private double scale;

    class RaysIterator implements Iterator<CameraRay> {
        private final int resWidth;
        private final int resHeight;
        private final double width;
        private final double height;
        
        private int x;
        private int y;

        public RaysIterator(int resWidth, int resHeight) {
            this.resWidth = resWidth;
            this.resHeight = resHeight;
            double aspectRatio = (double) resWidth / (double) resHeight;
            this.width = scale*aspectRatio;
            this.height = scale;
            
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
                    0);

            Ray ray = rayToWorld(new Ray(rayOrigin, Vector3D.PLUS_K));
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

    public OrthogonalCamera(Vector3D position, Vector3D eulerRotation, double scale) {
        super(position, eulerRotation);
        this.scale = scale;
    }

    @Override
    public Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY) {
        return new RaysIterator(resolutionX, resolutionY);
    }
}
