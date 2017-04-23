package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;

public class PerspectiveCamera extends Camera {
    private Vector3D position;
    private Vector3D direction;

    private double width;

    private Vector3D up;
    private Vector3D right;
    private Vector3D clipOrigin;

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

        return new Ray(position, point.subtract(position));
    }

    public PerspectiveCamera(Vector3D position, Vector3D direction, double fieldOfView, double clipDistance) {
        this.position = position;
        this.direction = direction.normalize();
        this.right = new Vector3D(0,  1, 0).crossProduct(this.direction).normalize();
        this.up = this.direction.crossProduct(right).normalize();
        this.clipOrigin = position.add(this.direction.scalarMultiply(clipDistance));


        this.width = clipDistance * Math.tan(fieldOfView / 2) * 2;
    }
}
