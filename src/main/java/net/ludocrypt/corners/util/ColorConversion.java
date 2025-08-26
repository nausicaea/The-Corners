package net.ludocrypt.corners.util;

public abstract class ColorConversion {
    public static float[] hexToRGBA(String hex) {
        float[] rgba = new float[4];
        hex = hex.replace("#", "");
        hex = hex.replace(" ", "");

        if (hex.length() == 6) {
            rgba[0] = Integer.parseInt(hex.substring(0, 2), 16) / 255f; // Red
            rgba[1] = Integer.parseInt(hex.substring(2, 4), 16) / 255f; // Green
            rgba[2] = Integer.parseInt(hex.substring(4, 6), 16) / 255f; // Blue
            rgba[3] = 1.0f; // Alpha (fully opaque)
        } else if (hex.length() == 8) {
            rgba[0] = Integer.parseInt(hex.substring(0, 2), 16) / 255f; // Red
            rgba[1] = Integer.parseInt(hex.substring(2, 4), 16) / 255f; // Green
            rgba[2] = Integer.parseInt(hex.substring(4, 6), 16) / 255f; // Blue
            rgba[3] = Integer.parseInt(hex.substring(6, 8), 16) / 255f; // Alpha
        } else {
            throw new IllegalArgumentException("Invalid hexadecimal color format.");
        }

        return rgba;
    }
}
