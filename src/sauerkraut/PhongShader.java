package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import sauerkraut.lights.Light;
import sauerkraut.util.ColorUtil;
import sauerkraut.shapes.Shape;

import java.awt.*;
import sauerkraut.lights.DistantLight;
import sauerkraut.lights.PointLight;
import sauerkraut.materials.Material;

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
        Color diffuseColor = Color.black;

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
                diffuseColor = ColorUtil.multiply(shape.getColor(point), cumulativeDiffuse);

                // Specular
                Vector3D R = normal.scalarMultiply(2 * (normal.dotProduct(dirToLight))).subtract(dirToLight);
                cumulativeSpecular += (float) Math.pow(R.dotProduct(viewDirection), material.specularExponent);

            }
        }
        cumulativeSpecular = material.specularStrength*Math.min(cumulativeSpecular, 1);
        Color specularColor = Color.BLACK;
        try {
            specularColor = new Color(cumulativeSpecular, cumulativeSpecular, cumulativeSpecular);
        } catch (java.lang.IllegalArgumentException exception) {
            System.out.println(material.specularStrength);
            System.out.println(Math.min(cumulativeSpecular, 1));
            System.out.println(cumulativeSpecular);
            System.exit(99);
        }
        return ColorUtil.add(diffuseColor, specularColor);
    }
}
