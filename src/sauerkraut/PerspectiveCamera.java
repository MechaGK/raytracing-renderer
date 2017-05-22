package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;

public class PerspectiveCamera extends Camera {
    //private Vector3D position;

    private double width;

    private Vector3D up;
    private Vector3D right;
    private Vector3D clipOrigin;

    private double clipDistance;

    class RaysIterator implements Iterator<CameraRay> {
        private final int resWidth;
        private final int resHeight;

        private final Vector3D bottomLeft;

        private int x;
        private int y;

        public RaysIterator(int width, int height, Vector3D bottomLeft) {
            this.resWidth = width;
            this.resHeight = height;

            this.bottomLeft = bottomLeft;

            this.x = 0;
            this.y = 0;
        }

        @Override
        public boolean hasNext() {
            return x < resWidth || y + 1 < resHeight;
        }

        @Override
        public CameraRay next() {
            Ray ray = getRayFromScreenSpace(x, y, resWidth, resHeight, bottomLeft);
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

    @Override
    public Iterator<CameraRay> raysIterator(int resolutionX, int resolutionY) {
        float aspectRatio = (float)resolutionY / (float)resolutionX;
        double height = aspectRatio * (width / 2);

        Vector3D bottomLeft = clipOrigin.subtract(right.scalarMultiply(width / 2));
        bottomLeft = bottomLeft.subtract(up.scalarMultiply(height));

        return new RaysIterator(resolutionX, resolutionY, bottomLeft);

    }

    public Ray getRayFromScreenSpace(int x, int y, int viewportWidth, int viewportHeight, Vector3D bottomLeft) {
        float baseX = (float)x / (float)viewportWidth;
        float baseY = 1f - ((float)y / (float)viewportHeight);

        float aspectRatio = (float)viewportWidth / (float)viewportHeight;

        double height = (float)width / aspectRatio;

        Vector3D point = bottomLeft.add(up.scalarMultiply(height).scalarMultiply(baseY))
                .add(right.scalarMultiply(width).scalarMultiply(baseX));

        return new Ray(point, point.subtract(position));
    }

    /**
     * Sets direction so the camera is looking at the given point
     * @param position Point to look at
     */
    @Override
    public void lookAt(Vector3D position) {
        Vector3D newDirection = position.subtract(this.position);

        setDirection(newDirection);
    }

    public PerspectiveCamera(Vector3D position, Vector3D direction, double fieldOfView, double clipDistance) {
        super(position, Vector3D.ZERO); // Euler angles hardcoded
        this.position = position;
        this.clipDistance = clipDistance;
        this.width = clipDistance * Math.tan(Math.toRadians(fieldOfView * 0.5)) * 2;

        setDirection(direction); // Clip distance must be set before
    }

    /**
     * Set the direction of the camera
     * @param newDirection The new direction of the camera
     */
    public void setDirection(Vector3D newDirection) {
        // Calculating up and right vector from the given direction.
        // We don't really use the direction itself anywhere
        Vector3D direction = newDirection.normalize();
        this.right = new Vector3D(0,  1, 0).crossProduct(direction).normalize();
        this.up = direction.crossProduct(right).normalize();

        // Origin of the clip plane
        this.clipOrigin = position.add(direction.scalarMultiply(clipDistance));


    }
}
