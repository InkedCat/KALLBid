package fr.umontpellier.iut.authclientforge.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Stage stage;
    private Map<String, Scene> scenes = new HashMap<>();

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void changeScene(String name) {
        Platform.runLater(() -> stage.setScene(scenes.get(name)));
    }

    public void showErrorPopup(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void showInfoPopup(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}