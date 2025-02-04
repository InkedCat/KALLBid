package fr.umontpellier.iut.encherisseur.gui;

import fr.umontpellier.iut.encherisseur.auction.SecondPriceObserver;
import javafx.beans.property.IntegerProperty;

public class SecondPricePropertyObserver implements SecondPriceObserver {
    private IntegerProperty secondPriceProperty;

    public SecondPricePropertyObserver(IntegerProperty secondPriceProperty) {
        this.secondPriceProperty = secondPriceProperty;
    }

    public IntegerProperty getSecondPriceProperty() {
        return secondPriceProperty;
    }

    public void update(int secondPrice) {
        secondPriceProperty.set(secondPrice);
    }
}
