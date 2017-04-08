package raytracing;

public class CameraRay {
    public final Ray ray;
    public final int x;
    public final int y;

    public CameraRay(Ray ray, int x, int y) {
        this.ray = ray;
        this.x = x;
        this.y = y;
    }
}
