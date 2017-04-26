package sauerkraut.materials;

import java.awt.image.BufferedImage;

/**
 * Class to hold information for rendering materials with textures
 */
public abstract class TexturedMaterial extends Material {

    Texture texture;

    public TexturedMaterial(float albedo, BufferedImage image) {
        super(albedo);
        texture = new Texture(image);
    }

}

