package saurkraut;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;

/**
 * For testing. Doesn't support directions other directions than (0, 0, 1).
 * Origin of raysIterator are not rotated as they should
 */
public class OrthogonalCamera extends Camera {
    private double scale;

    class RaysIterator implements Iterator<CameraRay> {
        private final double xStep;
        private final double yStep;
        private final double halfWidth;
        private final double halfHeight;
        private final int resWidth;
        private final int resHeight;
        
        private int x;
        private int y;

        public RaysIterator(int resWidth, int resHeight) {
            this.resWidth = resWidth;
            this.resHeight = resHeight;
            double aspectRatio = (double) resWidth / (double) resHeight;
            this.halfWidth = 0.5*(scale*aspectRatio);
            this.halfHeight = 0.5*scale;
            this.xStep = 2*halfWidth / resWidth;
            this.yStep = 2*halfHeight / resHeight;
            
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
                    -halfWidth + x*xStep,
                    halfHeight - y*yStep, // flipped y axis
                    0);

            Ray ray = rayToWorld(new Ray(rayOrigin, Vector3D.PLUS_K));
            //System.out.println("origin: " + ray.origin + ", direction: " + ray.direction);
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

    public OrthogonalCamera(Vector3D position, Vector3D eulerAngles, double scale) {
        super(position, eulerAngles);
        this.scale = scale;
    }

    @Override
    public Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY) {
        return new RaysIterator(resolutionX, resolutionY);
    }

    @Override
    public void lookAt(Vector3D point) {
        throw new NotImplementedException("lookAt not supported by orthogonal camera");
    }
}
