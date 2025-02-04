package fr.umontpellier.iut.shared.gui.Components.Text;

import javafx.geometry.Insets;
import javafx.scene.control.Label;

public class TextFactory {
    public Label createBaseText(String text){
        return new Label(text);
    }
    public Label createText(String text){
        return createBaseText(text);
    }

    public Label createText(String text, Insets padding){
        Label label = createBaseText(text);
        label.setPadding(padding);
        return label;
    }
}
