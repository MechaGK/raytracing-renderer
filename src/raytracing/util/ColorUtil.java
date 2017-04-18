package raytracing.util;

import java.awt.*;

/**
 * Class for Color "extension" methods.
 */
public class ColorUtil {
    /**
     * A method for adding two colors
     *
     * @param color1
     * @param color2
     * @return The addition of color1 and color2
     */
    public static Color add(Color color1, Color color2) {
        return new Color(
                color1.getRed() + color2.getRed(),
                color1.getGreen() + color2.getGreen(),
                color1.getBlue() + color2.getBlue());
    }

    /**
     * A method to multiply a color with a float. Because Java doesn't support extension methods
     *
     * @param color A given color
     * @param value A float
     * @return A new color where every component is multiplied by f
     */
    public static Color multiply(Color color, float value) {
        float[] rgb = color.getRGBColorComponents(null);
        float red = Math.min(rgb[0] * value, 1f);
        float green = Math.min(rgb[1] * value, 1f);
        float blue = Math.min(rgb[2] * value, 1f);

        return new Color(red, green, blue);
    }

    /**
     * A method to multiply a color with a float. Because Java doesn't support extension methods
     *
     * @param color1 A given color
     * @param color2 A float
     * @return A new color where every component is multiplied by f
     */
    public static Color multiply(Color color1, Color color2) {
        float[] rgb1 = color1.getRGBColorComponents(null);
        float[] rgb2 = color2.getRGBColorComponents(null);

        float red = Math.min(rgb1[0] * rgb2[0], 1f);
        float green = Math.min(rgb1[1] * rgb2[1], 1f);
        float blue = Math.min(rgb1[2] * rgb2[2], 1f);

        return new Color(red, green, blue);
    }
}
