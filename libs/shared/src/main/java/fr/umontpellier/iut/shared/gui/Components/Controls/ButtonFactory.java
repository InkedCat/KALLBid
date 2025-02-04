package fr.umontpellier.iut.shared.gui.Components.Controls;

import fr.umontpellier.iut.shared.gui.Colors.ColorManipulator;
import fr.umontpellier.iut.shared.gui.Colors.CustomColor;
import fr.umontpellier.iut.shared.gui.Colors.CustomColorsResolver;
import javafx.scene.control.Button;
public class ButtonFactory {
    private Button createBaseButton(String text,String normalColor,String hoverColor,String textColor){
        Button button = new Button(text);

        String baseStyle = "-fx-text-fill:"+textColor+";-fx-background-color: "+normalColor+";-fx-border-style: none;-fx-font-weight: 900;-fx-border-insets: 0;-fx-background-radius: 4px;-fx-border-width:0px";
        String hoverStyle = "-fx-text-fill:"+textColor+";-fx-background-color: "+hoverColor+";-fx-border-style: none;-fx-font-weight: 900;-fx-border-insets: 0;-fx-background-radius: 4px;-fx-border-width:0px";

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e->button.setStyle(hoverStyle));
        button.setOnMouseExited(e->button.setStyle(baseStyle));
        return button;
    }
    public Button createPrimaryButton(String text){
        String blue = CustomColorsResolver.resolveColor(CustomColor.BLUE);
        String darkblue = ColorManipulator.darkenColor(blue,10);
        return createBaseButton(text,blue,darkblue,"white");
    }

    public Button createSecondaryButton(String text){
        String gray = CustomColorsResolver.resolveColor(CustomColor.GRAY);
        String darkgray = ColorManipulator.darkenColor(gray,10);

        return createBaseButton(text,gray,darkgray,"black");
    }
}
