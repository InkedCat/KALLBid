package fr.umontpellier.iut.authclientforge.gui.view;

import fr.umontpellier.iut.authclientforge.gui.components.InputPasswordContainer;
import fr.umontpellier.iut.authclientforge.gui.components.InputUsernameContainer;
import fr.umontpellier.iut.shared.gui.Components.Controls.ButtonFactory;
import fr.umontpellier.iut.shared.gui.Components.Controls.ButtonGroupFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public abstract class AuthView {
    private InputUsernameContainer inputUsernameContainer;
    private InputPasswordContainer inputPasswordContainer;
    private Button submitButton;
    private Button redirectButton;

    private void addListeners() {
        submitButton.setOnAction(e -> {
            handleSubmit();
            inputPasswordContainer.getInput().clear();
        });

        redirectButton.setOnAction(e -> {
            handleRedirect();
        });
    }

    public AuthView() {
        this.inputUsernameContainer = new InputUsernameContainer();
        this.inputPasswordContainer = new InputPasswordContainer();
        this.submitButton = new ButtonFactory().createPrimaryButton(getSubmitButtonText());
        this.redirectButton = new ButtonFactory().createSecondaryButton(getRedirectButtonText());

        addListeners();
    }

    public VBox getUserFields() {
        VBox userFields = new VBox();
        userFields.setAlignment(Pos.CENTER);
        userFields.getChildren().addAll(inputUsernameContainer.getContainer(), inputPasswordContainer.getContainer());
        return userFields;
    }

    public HBox getButtons() {
        HBox buttons = new ButtonGroupFactory().createButtonFillGroup(List.of(submitButton, redirectButton));
        buttons.setAlignment(Pos.CENTER);

        return buttons;
    }

    protected InputUsernameContainer getInputUsernameContainer() {
        return inputUsernameContainer;
    }
    protected InputPasswordContainer getInputPasswordContainer() {
        return inputPasswordContainer;
    }

    protected abstract void handleSubmit();
    protected abstract String getSubmitButtonText();

    protected abstract void handleRedirect();
    protected abstract String getRedirectButtonText();
}
