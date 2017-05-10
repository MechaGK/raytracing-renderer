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
        
        // Move us away from the shape's surface by 0.00001d
        point = point.add(normal.scalarMultiply(0.00001d));

        Color cumulativeDiffuse = Color.BLACK;
        Color cumulativeSpecular = Color.BLACK;
        
        for (Light light : scene.getLights()) {
            boolean noIntersections = true;
            
            Vector3D dirToLight = light.directionFromPoint(point);
            
            // TODO: Refactor, maybe move shadow ray check to Light subclasses?
            if (light instanceof DistantLight) {
                Ray shadowRay = new Ray(point, dirToLight);
                RayHit hit = Raytracer.castRay(scene, shadowRay);
                noIntersections = Raytracer.rayFree(scene, shadowRay);
            }
            else if (light instanceof PointLight) {
                noIntersections = Raytracer.lineFree(scene, point, ((PointLight) light).position);
            }
            
            // We didn't hit anything
            if (noIntersections) {
                
                 // Diffuse color
                float number = (float) (shape.getMaterial().albedo / Math.PI * light.getIntensity(point)
                        * Math.max(0f, normal.dotProduct(dirToLight)));
                Color perLightDiffuse = ColorUtil.multiply(light.getColor(), number);
                cumulativeDiffuse = ColorUtil.add(cumulativeDiffuse, perLightDiffuse);

                // Specular color
                Vector3D R = normal.scalarMultiply(2 * (normal.dotProduct(dirToLight))).subtract(dirToLight);
                float specularValue = (float) Math.min(Math.pow(R.dotProduct(viewDirection), material.specularExponent), 1);
                Color perLightSpecular = new Color(specularValue, specularValue, specularValue);

                cumulativeSpecular = ColorUtil.add(cumulativeSpecular, perLightSpecular);

            }
        }

        cumulativeSpecular = ColorUtil.multiply(cumulativeSpecular, material.specularStrength);
        return ColorUtil.add(cumulativeDiffuse, cumulativeSpecular);
    }
}
