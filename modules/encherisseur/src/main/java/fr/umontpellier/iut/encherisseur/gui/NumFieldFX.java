package fr.umontpellier.iut.encherisseur.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

public class NumFieldFX extends TextField {
    private Label infoLabel;
    private static int maxLength = 9;

    public NumFieldFX() {
        this.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];

            if(getText().length() >= 9) {
                handleMaxLength(t);
            }
            else if (!(ch >= '0' && ch <= '9')) {
                handleNonNumericCharacter(t, ch);
            } else {
                clearInfoLabel();
            }
        });
    }

    private void handleMaxLength(KeyEvent t) {
        if (getText().length() >= maxLength) {
            showError("* La mise doit être comprise entre 0€ et 1 000 000 000€");
        }

        t.consume();
    }

    private void handleNonNumericCharacter(KeyEvent t, char ch) {
        if (!Character.isISOControl(ch)) {
            showError("* Le caractère que vous avez entré n'est pas un chiffre");
        }
        t.consume();
    }

    private void showError(String errorMessage) {
        if (!Objects.equals(null, infoLabel)) {
            infoLabel.setText(errorMessage);
            infoLabel.setManaged(true);
            infoLabel.setVisible(true);
        }
    }

    private void clearInfoLabel() {
        if (!Objects.equals(null, infoLabel)) {
            infoLabel.setManaged(false);
            infoLabel.setVisible(false);
        }
    }

    public void setInfoLabel(Label infoLabel) {
        this.infoLabel = infoLabel;
    }

    public boolean isValid() {
        String trimmedText = getText().trim();
        if (!trimmedText.isEmpty() && !trimmedText.matches("^(0+|0+\\.|\\.).*")) {
            return true;
        }
        showError("* La mise doit être suppérieur à 0");
        return false;
    }
}