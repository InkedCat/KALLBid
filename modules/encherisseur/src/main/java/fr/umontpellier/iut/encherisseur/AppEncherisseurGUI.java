package fr.umontpellier.iut.encherisseur;

import fr.umontpellier.iut.authclientforge.gui.utils.AuthBooleanObserver;
import fr.umontpellier.iut.authclientforge.gui.AuthStageManager;
import fr.umontpellier.iut.authclientforge.gui.controller.LoginController;
import fr.umontpellier.iut.authclientforge.gui.utils.AuthErrorMessageObserver;
import fr.umontpellier.iut.authclientforge.gui.view.LoginViewFactory;
import fr.umontpellier.iut.authclientforge.gui.SceneManager;
import fr.umontpellier.iut.authclientforge.gui.controller.SignupController;
import fr.umontpellier.iut.authclientforge.gui.view.SignupViewFactory;
import fr.umontpellier.iut.encherisseur.gui.EncherisseurController;
import fr.umontpellier.iut.encherisseur.gui.EncherisseurViewFactory;
import fr.umontpellier.iut.encherisseur.gui.SecondPricePropertyObserver;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppEncherisseurGUI extends Application {

    private static AppEncherisseur appEncherisseur;

    @Override
    public void start(Stage stage) {
        EncherisseurController encherisseurController = new EncherisseurController(appEncherisseur);
        ViewFactory encherisseurView = new EncherisseurViewFactory(encherisseurController);

        Scene scene = new Scene(encherisseurView.getRootPane(), 800, 450);
        SceneManager sceneManager = new SceneManager(stage);
        LoginController loginController = new LoginController(appEncherisseur.getAuthProfileManager(), sceneManager);
        SignupController signupController = new SignupController(appEncherisseur.getAuthProfileManager(), sceneManager);
        Scene loginScene = new Scene(new LoginViewFactory(loginController).getRootPane(), 800, 450);
        Scene signupScene = new Scene(new SignupViewFactory(signupController).getRootPane(), 800, 450);

        AuthBooleanObserver authObserver = new AuthBooleanObserver(new SimpleBooleanProperty());
        appEncherisseur.getAuthProfileManager().subscribe(authObserver);

        AuthErrorMessageObserver authErrorObserver = new AuthErrorMessageObserver();
        appEncherisseur.getAuthProfileManager().subscribeError(authErrorObserver);

        SecondPricePropertyObserver secondPricePropertyObserver = new SecondPricePropertyObserver(new SimpleIntegerProperty());
        appEncherisseur.subscribeSecondPrice(secondPricePropertyObserver);

        secondPricePropertyObserver.getSecondPriceProperty().addListener((observable, oldValue, newValue) -> {
            sceneManager.showInfoPopup("Enchère gagnante", "Le prix que devra payer le gagnant est " + newValue + "€");
        });

        AuthStageManager authStageManager = new AuthStageManager(sceneManager, scene, loginScene, signupScene,
                authObserver, authErrorObserver);
        authStageManager.start();

        stage.setTitle("KALL BID - Encherisseur");
        stage.setMinWidth(800);
        stage.setMinHeight(450);
        stage.getIcons().add(new Image("images/icon.png"));

        stage.setOnCloseRequest(event -> encherisseurController.quitAuction());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setBusinessLogic(AppEncherisseur app) {
        appEncherisseur = app;
    }
}