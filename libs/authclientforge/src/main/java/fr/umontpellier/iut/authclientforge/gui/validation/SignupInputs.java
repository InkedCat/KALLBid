package fr.umontpellier.iut.authclientforge.gui.validation;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignupInputs {

    /* Valide les données des champs du formulaire
       et gére l'affichage des erreurs avec les labels. */
    public boolean validateInputs(TextField usernameField, TextField passwordField, Label usernameInfo, Label passwordInfo, CheckBox checkBox,Label checkboxInfo){
        InputValidation.ValidationResult resultUsername = InputValidation.validateUsername(usernameField.getText());
        if (handleInputErrMessage(usernameInfo, resultUsername)) return false;

        InputValidation.ValidationResult resultPassword =  InputValidation.validatePassword(passwordField.getText());
        if (handleInputErrMessage(passwordInfo, resultPassword)) return false;

        InputValidation.ValidationResult resultCheckbox = InputValidation.validateCheckBox(checkBox);
        if (handleInputErrMessage(checkboxInfo, resultCheckbox)) return false;
        return true;
    }

    // Gére l'affichage des erreurs avec les labels.
    public boolean handleInputErrMessage(Label usernameInfoLabel, InputValidation.ValidationResult resultUsername) {
        if(!resultUsername.isValid()){
            usernameInfoLabel.setText(resultUsername.message());
            usernameInfoLabel.setVisible(true);
            usernameInfoLabel.setManaged(true);
            return true;
        } else {
            usernameInfoLabel.setText("");
            usernameInfoLabel.setVisible(false);
            usernameInfoLabel.setManaged(false);
        }
        return false;
    }
}
