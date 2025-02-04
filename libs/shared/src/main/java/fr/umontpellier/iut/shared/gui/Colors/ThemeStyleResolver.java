package fr.umontpellier.iut.shared.gui.Colors;

public class ThemeStyleResolver {
    public static String resolveTextStyle(Theme theme) {
        switch (theme) {
            case LIGHT:
                return "-fx-text-fill: black;";
            case DARK:
                return "-fx-text-fill: white;";
            default:
                return "-fx-text-fill: black";
            }
        }
}
