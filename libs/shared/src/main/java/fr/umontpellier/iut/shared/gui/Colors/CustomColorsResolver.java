package fr.umontpellier.iut.shared.gui.Colors;


public class CustomColorsResolver {
    public static String resolveColor(CustomColor color) {
        switch (color) {
            case GRAY:
                return "#ECECEC";
            case BLUE:
                return "#202A44";
            default:
                return "#ECECEC";
        }
    }
}