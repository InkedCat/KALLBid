package fr.umontpellier.iut.authclientforge.gui.view;

import fr.umontpellier.iut.authclientforge.gui.controller.LoginController;
import fr.umontpellier.iut.shared.gui.Components.PageLayout.NavbarFactory;
import fr.umontpellier.iut.shared.gui.Components.Text.TitleFactory;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LoginViewFactory extends AuthView implements ViewFactory {
    private LoginController loginController;

    public LoginViewFactory(LoginController loginController) {
        super();
        this.loginController = loginController;
    }

    @Override
    protected void handleSubmit() {
        loginController.login(getInputUsernameContainer().getInput().getText(),
                getInputPasswordContainer().getInput().getText());
    }

    @Override
    protected String getSubmitButtonText() {
        return "se connecter";
    }

    @Override
    protected void handleRedirect() {
        loginController.signup();
    }

    @Override
    protected String getRedirectButtonText() {
        return "pas encore inscrit ?";
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

        form.getChildren().addAll(title, getUserFields(), getButtons());
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
