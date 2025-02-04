package fr.umontpellier.iut.authclientforge.gui.view;

import fr.umontpellier.iut.authclientforge.gui.components.ConditionsContainer;
import fr.umontpellier.iut.authclientforge.gui.components.InputPasswordContainer;
import fr.umontpellier.iut.authclientforge.gui.components.InputUsernameContainer;
import fr.umontpellier.iut.authclientforge.gui.controller.SignupController;
import fr.umontpellier.iut.authclientforge.gui.validation.InputValidation;
import fr.umontpellier.iut.authclientforge.gui.validation.SignupInputs;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.NavbarFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TitleFactory;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SignupViewFactory extends AuthView implements ViewFactory {
    private SignupController signupController;
    private ConditionsContainer conditionsContainer;

    public SignupViewFactory(SignupController signupController) {
        super();
        this.signupController = signupController;
        this.conditionsContainer = new ConditionsContainer();
    }

    @Override
    protected void handleSubmit() {
        InputValidation.validateCheckBox(conditionsContainer.getInput());
        InputUsernameContainer usernameContainer = getInputUsernameContainer();
        InputPasswordContainer passwordContainer = getInputPasswordContainer();


        if((new SignupInputs()).validateInputs(usernameContainer.getInput(),passwordContainer.getInput(),usernameContainer.getInfo(),passwordContainer.getInfo(), conditionsContainer.getInput(), conditionsContainer.getInfo())){
            signupController.signup(getInputUsernameContainer().getInput().getText(),
                    getInputPasswordContainer().getInput().getText());
        }

        usernameContainer.getInput().clear();
    }

    @Override
    protected String getSubmitButtonText() {
        return "s'inscrire";
    }

    @Override
    protected void handleRedirect() {
        signupController.login();
    }

    @Override
    protected String getRedirectButtonText() {
        return "déjà inscrit ?";
    }

    private Pane getTop() {
        return new NavbarFactory().createSimpleNavbar();
    }

    private Pane getCenter() {
        VBox form = new VBox();
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(350);
        form.setSpacing(6);

        Label title = new TitleFactory().createTitle("Enchères électroniques",new Insets(0,0,20,0));

        form.getChildren().addAll(title, getUserFields(), conditionsContainer.getContainer(), getButtons());
        return form;
    }

    @Override
    public Pane getRootPane() {
        BorderPane root = new BorderPane();

        root.setTop(getTop());
        root.setCenter(getCenter());

        return root;
    }
}
