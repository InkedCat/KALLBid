package fr.umontpellier.iut.authclientforge.gui.controller;

import fr.umontpellier.iut.authclientforge.gui.SceneManager;
import fr.umontpellier.iut.authclientforge.AuthController;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;

public class SignupController {
    private final AuthController authController;
    private final SceneManager sceneManager;

    public SignupController(AuthController authController, SceneManager sceneManager) {
        this.authController = authController;
        this.sceneManager = sceneManager;
    }

    public void signup(String username, String password) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    authController.register(username, password);
                } catch (IOException e) {
                    Platform.runLater(() -> sceneManager.showErrorPopup("Erreur de connexion", e.getMessage()));
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void login() {
        sceneManager.changeScene("login");
    }
}
