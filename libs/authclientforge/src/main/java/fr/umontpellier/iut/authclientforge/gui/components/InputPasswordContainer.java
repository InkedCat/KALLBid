package fr.umontpellier.iut.authclientforge.gui.components;


import fr.umontpellier.iut.shared.gui.Components.Fields.FieldFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TextFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
public class InputPasswordContainer {
    private Label title;
    private TextField input;
    private Label info;
    private VBox container;

    public InputPasswordContainer(){
        this.title = (new TextFactory()).createText("Mot de passe",new Insets(0,0,4,0));
        this.input = ((new FieldFactory()).createPasswordField("Entrez votre mot de passe" ));
        input.setId("passwordField");

        this.info = (new TextFactory()).createText("",new Insets(0,0,4,0));
        info.setId("passwordInfo");
        info.setManaged(false);

        this.container = new InputContainer().buildInputContainer(title,input,info,new Insets(0,0,8,0));
    }

    public VBox getContainer() {
        return container;
    }

    public TextField getInput() {
        return input;
    }

    public Label getInfo() {
        return info;
    }
}
