package fr.umontpellier.iut.shared.gui.Colors;

public class ColorManipulator {
    public static String darkenColor(String hexColor, int darknessAmount) {
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

        red = Math.max(0, red - darknessAmount);
        green = Math.max(0, green - darknessAmount);
        blue = Math.max(0, blue - darknessAmount);

        return String.format("#%02X%02X%02X", red, green, blue);
    }
}
