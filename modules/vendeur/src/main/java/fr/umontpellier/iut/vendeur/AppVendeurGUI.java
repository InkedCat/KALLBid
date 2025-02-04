package fr.umontpellier.iut.vendeur;

import fr.umontpellier.iut.authclientforge.gui.utils.AuthBooleanObserver;
import fr.umontpellier.iut.authclientforge.gui.AuthStageManager;
import fr.umontpellier.iut.authclientforge.gui.controller.LoginController;
import fr.umontpellier.iut.authclientforge.gui.utils.AuthErrorMessageObserver;
import fr.umontpellier.iut.authclientforge.gui.view.LoginViewFactory;
import fr.umontpellier.iut.authclientforge.gui.SceneManager;
import fr.umontpellier.iut.authclientforge.gui.controller.SignupController;
import fr.umontpellier.iut.authclientforge.gui.view.SignupViewFactory;
import fr.umontpellier.iut.vendeur.gui.VendeurController;
import fr.umontpellier.iut.vendeur.gui.VendeurViewFactory;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AppVendeurGUI extends Application {

    private static AppVendeur appVendeur;

    @Override
    public void start(Stage stage) {
        try {
            appVendeur.startAuction();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VendeurController vendeurController = new VendeurController(appVendeur);
        VendeurViewFactory vendeurView = new VendeurViewFactory(vendeurController);

        Scene scene = new Scene(vendeurView.getRootPane(), 800, 600);
        SceneManager sceneManager = new SceneManager(stage);
        LoginController loginController = new LoginController(appVendeur.getAuthProfileManager(), sceneManager);
        SignupController signupController = new SignupController(appVendeur.getAuthProfileManager(), sceneManager);
        Scene loginScene = new Scene(new LoginViewFactory(loginController).getRootPane(), 800, 600);
        Scene signupScene = new Scene(new SignupViewFactory(signupController).getRootPane(), 800, 600);

        AuthBooleanObserver authObserver = new AuthBooleanObserver(new SimpleBooleanProperty());
        appVendeur.getAuthProfileManager().subscribe(authObserver);

        AuthErrorMessageObserver authErrorObserver = new AuthErrorMessageObserver();
        appVendeur.getAuthProfileManager().subscribeError(authErrorObserver);

        AuthStageManager authStageManager = new AuthStageManager(sceneManager, scene, loginScene, signupScene,
                authObserver, authErrorObserver);

        authStageManager.start();

        stage.setTitle("KALL BID - Vendeur");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.getIcons().add(new Image("images/icon.png"));

        stage.setOnCloseRequest(event -> vendeurController.quitAuction());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setBusinessLogic(AppVendeur app) {
        appVendeur = app;
    }

}