package fr.umontpellier.iut.authclientforge.gui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InputContainer {

    public VBox buildInputContainer(Label containerTitle, TextField field, Label info, Insets padding){
        VBox container = new VBox();
        container.setPadding(padding);
        container.getChildren().addAll(containerTitle,field,info);
        return container;
    }
}
