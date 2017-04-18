package raytracing;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import raytracing.util.ColorUtil;

import java.awt.*;

/**
 * Class for shading based on Phong
 */
public class PhongShader {

    /**
     * Calculates the diffuse for a specific point
     * Only supports distant lights at the moment and no shadows.
     * Calculations based on diffuse rendering from
     * https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/diffuse-lambertian-shading
     *
     * @param scene The scene
     * @param shape The shape which the diffuse is calculated for
     * @param point The point at which to find the illumination
     * @return The color of the total illumination
     */
    public static Color diffuse(Scene scene, Shape shape, Vector3D point) {
        Vector3D normal = shape.getNormal(point);
        Material material = shape.getMaterial();

        Color finalColor = new Color(0f, 0f, 0f);

        DistantLight distantLight;
        Color color;

        for (Light light : scene.getLights()) {
            if (light instanceof DistantLight) {
                distantLight = (DistantLight) light;

                Vector3D lightIncident = distantLight.direction.scalarMultiply(-1);

                float number = (float) (material.albedo / Math.PI * light.intensity
                        * Math.max(0f, normal.dotProduct(lightIncident)));

                color = ColorUtil.multiply(light.color, number);
                finalColor = ColorUtil.add(color, finalColor);
            }
        }

        return finalColor;
    }
}
