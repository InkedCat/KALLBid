package fr.umontpellier.iut.shared.gui.Components.Fields;

import fr.umontpellier.iut.shared.gui.Colors.CustomColor;
import fr.umontpellier.iut.shared.gui.Colors.CustomColorsResolver;
import javafx.geometry.Insets;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class FieldFactory {
    private TextField createBaseField(String placeholder){
        String gray = CustomColorsResolver.resolveColor(CustomColor.GRAY);
        TextField textField = new TextField();
        textField.setPromptText(placeholder);
        textField.setStyle("-fx-border-style: none;-fx-border-insets: 0;-fx-background-color: "+gray+";-fx-background-radius: 8px;-fx-border-width:0px");
        return textField;
    }
    public TextField createField(String placeholderText){
        return createBaseField(placeholderText);
    }


    public PasswordField createPasswordField(String placeholderText){
        String gray = CustomColorsResolver.resolveColor(CustomColor.GRAY);
        PasswordField textField = new PasswordField();
        textField.setPromptText(placeholderText);
        textField.setStyle("-fx-border-style: none;-fx-border-insets: 0;-fx-background-color: "+gray+";-fx-background-radius: 8px;-fx-border-width:0px");
        return textField;
    }

    public TextField createField(String placeholderText, Insets padding){
        TextField textField = createBaseField(placeholderText);
        textField.setPadding(padding);
        return textField;
    }
}
