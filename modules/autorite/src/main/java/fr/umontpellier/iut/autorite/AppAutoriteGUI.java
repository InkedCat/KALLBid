package fr.umontpellier.iut.autorite;

import fr.umontpellier.iut.autorite.gui.AutoriteController;
import fr.umontpellier.iut.autorite.gui.AutoriteViewFactory;
import fr.umontpellier.iut.shared.gui.ViewFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppAutoriteGUI extends Application {

    private static AppAutorite appAutorite;

    @Override
    public void start(Stage stage) {
        AutoriteController autoriteController = new AutoriteController(appAutorite);
        ViewFactory autoriteView = new AutoriteViewFactory(autoriteController);

        Scene scene = new Scene(autoriteView.getRootPane(), 800, 600);

        stage.setTitle("KALL BID - Autorite de gestion");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.getIcons().add(new Image("images/icon.png"));

        stage.setOnCloseRequest(event -> {
            autoriteController.quitAuction();
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setBusinessLogic(AppAutorite app) {
        appAutorite = app;
    }
}