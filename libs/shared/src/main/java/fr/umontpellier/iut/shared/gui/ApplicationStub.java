package fr.umontpellier.iut.shared.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationStub extends Application {
    private String urlLink;
    @Override
    public void start(Stage stage) {
        getHostServices().showDocument(urlLink);
    }

    public void setUrlLink(String urlLink){
        this.urlLink = urlLink;
    }


    public static void main(String[] args) {
        launch();
    }
}