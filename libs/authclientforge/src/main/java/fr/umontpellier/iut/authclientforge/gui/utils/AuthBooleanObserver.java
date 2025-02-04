package fr.umontpellier.iut.authclientforge.gui.utils;

import fr.umontpellier.iut.authclientforge.profile.AuthProfileObserver;
import javafx.beans.property.BooleanProperty;

public class AuthBooleanObserver implements AuthProfileObserver {

    private BooleanProperty booleanProperty;

    public AuthBooleanObserver(BooleanProperty booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public BooleanProperty getBooleanProperty() {
        return booleanProperty;
    }

    @Override
    public void update(boolean bool) {
        booleanProperty.setValue(bool);
    }
}
