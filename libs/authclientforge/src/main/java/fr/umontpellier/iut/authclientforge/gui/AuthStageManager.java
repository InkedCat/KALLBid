package fr.umontpellier.iut.authclientforge.gui;

import fr.umontpellier.iut.authclientforge.gui.utils.AuthBooleanObserver;
import fr.umontpellier.iut.authclientforge.gui.utils.AuthErrorMessageObserver;
import javafx.scene.Scene;

public class AuthStageManager {
    private final SceneManager sceneManager;
    private final AuthBooleanObserver observer;
    private final AuthErrorMessageObserver errorObserver;

    private void addAuthStatutListener() {
        observer.getBooleanProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sceneManager.changeScene("final");
            } else {
                sceneManager.changeScene("login");
            }
        });

        errorObserver.getErrorMessageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                sceneManager.showErrorPopup("Erreur d'authentification", newValue);
            }
        });
    }

    public AuthStageManager(SceneManager sceneManager, Scene finalScene, Scene loginScene,
                            Scene signupScene, AuthBooleanObserver authObserver,
                            AuthErrorMessageObserver errorObserver) {
        this.sceneManager = sceneManager;
        this.observer = authObserver;
        this.errorObserver = errorObserver;

        sceneManager.addScene("final", finalScene);
        sceneManager.addScene("login", loginScene);
        sceneManager.addScene("signup", signupScene);
    }

    public void start() {
        sceneManager.changeScene("login");
        addAuthStatutListener();
    }
}
