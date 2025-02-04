package fr.umontpellier.iut.authclientforge.gui.utils;

import fr.umontpellier.iut.authclientforge.profile.AuthErrorObserver;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuthErrorMessageObserver implements AuthErrorObserver {
    private StringProperty errorMessageProperty;

    public AuthErrorMessageObserver() {
        errorMessageProperty = new SimpleStringProperty();
    }

    public StringProperty getErrorMessageProperty() {
        return errorMessageProperty;
    }

    public void update(String errorMessage) {
        errorMessageProperty.set(null);
        errorMessageProperty.set(errorMessage);
    }
}
