package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.lights.Light;
import saurkraut.util.ColorUtil;
import saurkraut.shapes.Shape;

import java.awt.*;
import saurkraut.lights.DistantLight;
import saurkraut.lights.PointLight;
import saurkraut.materials.Material;

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
    public Color shade(Scene scene, Shape shape, Vector3D point, Vector3D normal, Vector3D viewDirection) {
        final Material material = shape.getMaterial();
        
        float contribution;
        Vector3D dirToLight;
        Color cumulativeDiffuse = Color.BLACK;
        float cumulativeSpecular = 0;
        
        // Move us away from the shape's surface by 0.00001d
        point = point.add(normal.scalarMultiply(0.001d));
        
        for (Light light : scene.getLights()) {
            contribution = light.getContribution(scene, point);
            
            if (contribution > 0) {
                
                dirToLight = light.directionFromPoint(point);

                 // Diffuse
                float number = (float) (shape.getMaterial().albedo / Math.PI * contribution
                        * Math.max(0f, normal.dotProduct(dirToLight)));
                Color perLightDiffuse = ColorUtil.multiply(light.getColor(), number);
                cumulativeDiffuse = ColorUtil.add(cumulativeDiffuse, perLightDiffuse);

                // Specular
                Vector3D R = normal.scalarMultiply(2 * (normal.dotProduct(dirToLight))).subtract(dirToLight);
                cumulativeSpecular += (float) Math.pow(R.dotProduct(viewDirection), material.specularExponent);

            }
        }
        cumulativeSpecular = material.specularStrength*Math.min(cumulativeSpecular, 1);
        Color specularColor = new Color(cumulativeSpecular, cumulativeSpecular, cumulativeSpecular);
        return ColorUtil.add(cumulativeDiffuse, specularColor);
    }
}
