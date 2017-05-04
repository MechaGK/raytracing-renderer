package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.DistantLight;
import saurkraut.lights.Light;
import saurkraut.util.ColorUtil;
import saurkraut.shapes.Shape;

import java.awt.*;
import java.util.ArrayList;
import saurkraut.lights.PointLight;

/**
 * Class for shading based on Phong
 */
public class PhongShader {
    /**
     * Calculates the diffuse for a specific point
     * Only supports distant lights at the moment.
     * Calculations based on diffuse rendering from
     * https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/diffuse-lambertian-shading
     *
     * @param lights Lights to base shading on
     * @param albedo The albedo used for the shading
     * @param normal The normal at the point
     * @return The color of the total illumination
     */
    public static Color diffuse(ArrayList<Light> lights, float albedo, Vector3D normal) {
        

        // TODO: make this handle only one light
        
        
        Color finalColor = new Color(0f, 0f, 0f, 1f);

        DistantLight distantLight;
        Color color;

        for (Light light : lights) {
            if (light instanceof DistantLight) {
                distantLight = (DistantLight) light;

                Vector3D lightIncident = distantLight.direction.scalarMultiply(-1);

                float number = (float) (albedo / Math.PI * light.getIntensity(null, null)
                        * Math.max(0f, normal.dotProduct(lightIncident)));

                color = ColorUtil.multiply(light.getColor(), number);
                finalColor = ColorUtil.add(color, finalColor);
            }
        }

        return finalColor;
    }

    /**
     * Calculates specular for a given point.
     * Only supports distant lights.
     * Based on calculations from
     * https://www.scratchapixel.com/lessons/3d-basic-rendering/phong-shader-BRDF
     *
     * @param lights        List of lights which to shade based upon
     * @param normal        Normal of the given point
     * @param viewDirection The direction which the point is viewed from
     * @return Color of the specular at the point
     */
    public static Color specular(ArrayList<Light> lights, Vector3D normal, Vector3D viewDirection) {
        

        // TODO: make this handle only one light
        
        
        Color color = new Color(0f, 0f, 0f);

        DistantLight distantLight;

        for (Light light : lights) {
            if (light instanceof DistantLight) {
                distantLight = (DistantLight) light;

                Vector3D lightIncident = distantLight.direction.scalarMultiply(-1);

                Vector3D R = normal.scalarMultiply(2 * (normal.dotProduct(lightIncident))).subtract(lightIncident);
                double specular = Math.pow(R.dotProduct(viewDirection), 50); // Magic number for n
                float fspecular = Math.min((float) specular, 1f);

                color = ColorUtil.add(color, new Color(fspecular, fspecular, fspecular));
            }
        }

        return color;
    }

    /**
     * Calculate shading at point based on phong model
     * Based on
     * https://www.scratchapixel.com/lessons/3d-basic-rendering/phong-shader-BRDF
     *
     * @param scene         Scene the point is in
     * @param shape         Shape to be shaded
     * @param point         Point to be shaded
     * @param viewDirection Direction where the point is viewed from
     * @return Color of point based on phong model
     */
    public static Color shade(Scene scene, Shape shape, Vector3D point, Vector3D viewDirection) {
        Vector3D normal = shape.getNormal(point);
        ArrayList<Light> lights = new ArrayList<>();

        DistantLight distantLight;

        // Finding all lights which is not blocked at point.
        for (Light light : scene.getLights()) {
            if (light instanceof DistantLight) {
                distantLight = (DistantLight) light;

                Vector3D lightIncident = distantLight.direction.scalarMultiply(-1);
                
                Ray shadowRay = new Ray(point.add(normal.scalarMultiply(0.00001d)), lightIncident);
                RayHit hit = scene.castRay(shadowRay);
                if (hit == null) {
                    lights.add(light);
                }
            }
        }

        if (lights.isEmpty()) {
            return Color.black;
        }

        Color diffuse = diffuse(lights, shape.getMaterial().albedo, normal);
        Color specular = specular(lights, normal, viewDirection);

        float specularStrength = 0.1f;
        specular = ColorUtil.multiply(specular, specularStrength);
        return ColorUtil.add(diffuse, specular);
    }
}
