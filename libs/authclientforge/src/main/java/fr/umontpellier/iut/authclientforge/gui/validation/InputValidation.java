package fr.umontpellier.iut.authclientforge.gui.validation;

import javafx.scene.control.CheckBox;

import java.util.regex.*;

import java.util.Objects;

public class InputValidation {
    public static ValidationResult validatePassword(String password){
        String pattern ="[\\W]";
        char[] forbiddenChars = {',','\'',':',';'};

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(password);

        if(InputValidation.strContainsForbiddenChars(password,forbiddenChars)){
            return new ValidationResult(false,"* Le mot de passe ne doit pas contenir un de ces caractères (,':;)");
        }

        boolean containsSpecialChar =  matcher.find();

        if (inputIsEmpty(password)) {
            return new ValidationResult(false, "* Le mot de passe ne peut pas être vide");
        } else if (password.length() < 8) {
            return new ValidationResult(false, "* Le mot de passe doit contenir au moins 8 caractères.");
        } else if(!containsSpecialChar){
            System.out.println(containsSpecialChar);
            return new ValidationResult(false, "* Le mot de passe doit contenir au moins un caractère spécial hormis : ,':;");
        }
        return new ValidationResult(true,"");
    }

    public static ValidationResult validateUsername(String username){
        if (inputIsEmpty(username)) {
            return new ValidationResult(false, "* Le nom d'utilisateur ne peut pas être vide");
        } else if (username.length() < 4) {
            return new ValidationResult(false, "* Le nom d'utilisateur doit contenir au moins 4 caractères.");
        }
        return new ValidationResult(true,"");
    }

    public static ValidationResult validateCheckBox(CheckBox checkBox) {
        boolean isChecked = checkBox.isSelected();
        if(!isChecked){
            return new ValidationResult(false, "* Il faut cocher la case pour qu'elle acceptée");
        } else {
            return new ValidationResult(true,"");
        }
    }

    public static boolean strContainsForbiddenChars(String data,char[] chars){
        for(char x:chars){
            if(data.indexOf(x)!=-1){
                return true;
            }
        }
        return false;
    }

    public static boolean inputIsEmpty(String input){
        return Objects.equals(input, null) || input.trim().isEmpty();
    }

    public record ValidationResult(boolean isValid, String message) {
    }
}
