package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.Light;
import saurkraut.util.ColorUtil;
import saurkraut.shapes.Shape;

import java.awt.*;

/**
 * Class for shading based on Phong
 */
public class PhongShader implements Shader {
    /**
     * Calculate shading at point based on phong model
     * Based on
     * https://www.scratchapixel.com/lessons/3d-basic-rendering/phong-shader-BRDF
     *
     * @param scene         Scene the point is in
     * @param shape         Shape to be shaded
     * @param point         Point to be shaded
     * @param normal
     *@param viewDirection Direction where the point is viewed from  @return Color of point based on phong model
     */
    @Override
    public Color shade(Scene scene, Shape shape, Vector3D point, Vector3D normal, Vector3D viewDirection) {
        // Move us away from the shape's surface by 0.00001d
        point = point.add(normal.scalarMultiply(0.00001d));

        Color diffuseColor = Color.BLACK;
        Color specularColor = Color.BLACK;
        
        // Finding all lights which is not blocked at point.
        for (Light light : scene.getLights()) {
            
            Vector3D dirToLight = light.getDirectionFromPoint(point);

            Ray shadowRay = new Ray(point, dirToLight);
            RayHit hit = Raytracer.castRay(scene, shadowRay);
            if (hit == null) {
                 // Calculate diffuse color
                float number = (float) (shape.getMaterial().albedo / Math.PI * light.getIntensity(scene, point)
                        * Math.max(0f, normal.dotProduct(dirToLight)));
                Color perLightColor = ColorUtil.multiply(light.getColor(), number);
                diffuseColor = ColorUtil.add(diffuseColor, perLightColor);

                // Calculate specular color                
                Vector3D R = normal.scalarMultiply(2 * (normal.dotProduct(dirToLight))).subtract(dirToLight);
                double specularValue = Math.pow(R.dotProduct(viewDirection), 50); // Magic number for n
                float fspecular = Math.min((float) specularValue, 1f);

                specularColor = ColorUtil.add(specularColor, new Color(fspecular, fspecular, fspecular));

            }
        }

        float specularStrength = 0.1f; // TODO: Where should this hack live? Properbly not here.
        specularColor = ColorUtil.multiply(specularColor, specularStrength);
        return ColorUtil.add(diffuseColor, specularColor);
    }
}
